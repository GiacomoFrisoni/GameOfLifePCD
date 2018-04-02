package controller;

import java.awt.Dimension;
import java.util.Objects;
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
import view.MessageViewer;
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
	private boolean isMapInitialized;
	
	
	/**
	 * Constructs a new game controller.
	 * 
	 * @param view
	 * 		the Game Of Life view
	 */
	public GameControllerImpl(final GameOfLifeFrame view) {
		Objects.requireNonNull(view);
		this.view = view;
		this.isMapInitialized = false;
		// Calculates the pool size for tasks executor, according to the processors number
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
		// Initializes the stop flag
		this.stopFlag = new Flag();
		this.stopFlag.setOn();
		// Creates the producer / consumer queue
		this.queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
	}
	
	
	/*
	 * Randomly initializes the model cell map to about 50% on-cells.
	 * Creates tasks for the initialization and awaits their end. 
	 */
	private void initCellMap() {
		Objects.requireNonNull(this.model);
		this.model.clear();
		this.view.setProgress(ProgressType.INDETERMINATE, "Initializing...");
		try {
			final BigList<Callable<Void>> initTasks = new BigList<>();
			final Dimension cellMapDimension = this.model.getCellMapDimension();
			int initLength = (cellMapDimension.width * cellMapDimension.height) / 2;
			do {
				initTasks.add(new InitTask(model));
			} while (--initLength > 0);
			this.executor.invokeAll(initTasks);
		} catch (InterruptedException e) {
			MessageViewer.showException(
					"Init failed",
					"Failed to do the init", 
					e.getMessage());
		}
		this.model.nextGeneration();
		this.isMapInitialized = true;
		this.view.reset();
		this.view.drawCells(this.model.getCellMapStates());
	}
	
	
	/*
	 * Gets the map dimension specified as input from the view and
	 * initializes the model. Return true if the operation is successful,
	 * false otherwise.
	 */
	private boolean initModel() {
		final Optional<Dimension> mapDimension = view.getMapDimension();
		if (mapDimension.isPresent()) {
			this.model = new ConwayCellMapImpl(mapDimension.get().width, mapDimension.get().height);
			return true;
		}
		return false;
	}
	
	
	@Override
	public void start() {
		if (initModel()) {
			if (this.stopFlag.isOn()) {
				if (!this.isMapInitialized)
					initCellMap();

				this.stopFlag.setOff();
				
				// Starts producer and consumer threads
				new GameOfLifeProducer(this.queue, this.executor, this.model, this.stopFlag).start();
				new GameOfLifeConsumer(this.queue, this.view, this.stopFlag).start();
				
				this.view.setStarted();
			}
		} else {
			MessageViewer.showException(
					"Init failed", 
					"Failed to do the init");
		}
	}
	
	@Override
	public void stop() {
		this.stopFlag.setOn();
		this.view.setStopped();
	}

	@Override
	public void reset() {
		this.stopFlag.setOn();
		this.queue.clear();
		this.isMapInitialized = false;
		this.view.reset();
	}
	
	@Override
	public Dimension getCellMapDimension() {
		return this.model.getCellMapDimension();
	}
	
}
