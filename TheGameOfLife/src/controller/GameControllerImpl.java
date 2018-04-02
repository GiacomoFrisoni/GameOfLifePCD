package controller;

import java.awt.Dimension;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.magicwerk.brownies.collections.BigList;

import model.ConwayCellMap;
import model.ConwayCellMapImpl;
import model.GenerationResult;
import view.GameOfLifeFrame;
import view.ProgressType;

/**
 * Implementation of {@link GameController}.
 */
public class GameControllerImpl implements GameController {

	private static final int BUFFER_SIZE = 100;
	
	private ConwayCellMap model;
	private final GameOfLifeFrame view;
	private final BlockingQueue<GenerationResult> queue;
	private final ExecutorService executor;
	private final Flag stopFlag;
	private boolean isStarted;
	private boolean isInitialized;
	
	/**
	 * Constructs a new game controller.
	 * 
	 * @param view
	 * 		the Game Of Life view
	 */
	public GameControllerImpl(final GameOfLifeFrame view) {
		this.view = view;
		this.isStarted = false;
		this.isInitialized = false;
		// Calculates the pool size for tasks executor, according to the processors number
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
		// Initializes the stop flag
		this.stopFlag = new Flag();
		// Creates the producer / consumer queue
		this.queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
	}
	
	private void initCellMap() {
		this.model.clear();
		view.setProgress(ProgressType.INDETERMINATE, "Initializing...");
		try {
			// Randomly initializes the cell map to about 50% on-cells
			final BigList<Callable<Void>> initTasks = new BigList<>();
			final Dimension cellMapDimension = this.model.getCellMapDimension();
			int initLength = (cellMapDimension.width * cellMapDimension.height) / 2;
			do {
				initTasks.add(new InitTask(model));
			} while (--initLength > 0);
			this.executor.invokeAll(initTasks);
		} catch (InterruptedException e) {
			// View -> initFailed
			e.printStackTrace();
		}
		
		view.setProgress(ProgressType.INDETERMINATE, "Computing...");
		this.model.nextGeneration();
		this.isInitialized = true;
	}
	
	private boolean init() {
		final Optional<Dimension> d = view.getMapDimension();
		if (d.isPresent()) 
			this.model = new ConwayCellMapImpl(d.get().width, d.get().height);
		return d.isPresent();
	}
	
	@Override
	public void start() {
		if (init()) {
			reset();
			
			if (this.isInitialized && !this.isStarted) {
				stopFlag.setOff();
				new GameOfLifeService(this.queue, this.executor, this.model, this.view, this.stopFlag).start();
				isStarted = true;
				view.setStarted();
			}
		} else {
			System.out.println("Failed to init controller");
		}
	}
	
	@Override
	public void stop() {
		stopFlag.setOn();
		isStarted = false;
		view.setStopped();
	}

	@Override
	public void reset() {
		stopFlag.setOn();
		isStarted = false;
		this.queue.clear();
		initCellMap();
		view.reset();
	}
	
	@Override
	public Dimension getCellMapDimension() {
		return this.model.getCellMapDimension();
	}
	
}
