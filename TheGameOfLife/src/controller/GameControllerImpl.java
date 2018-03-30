package controller;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.ConwayCellMap;
import model.GenerationResult;
import view.GameOfLifeFrame;

public class GameControllerImpl implements GameController {

	private static final int BUFFER_SIZE = 100;
	
	private final ConwayCellMap model;
	private final GameOfLifeFrame view;
	private final BlockingQueue<GenerationResult> queue;
	private final ExecutorService executor;
	private final Flag stopFlag;
	private boolean isStarted;
	private boolean isInitialized;
	
	public GameControllerImpl(final ConwayCellMap model, final GameOfLifeFrame view) {
		this.model = model;
		this.view = view;
		this.isStarted = false;
		this.isInitialized = false;
		/*
		 * Calculates the pool size for tasks executor, according to the processors
		 * number.
		 */
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
		// Initializes the stop flag
		this.stopFlag = new Flag();
		// Creates the producer / consumer queue
		this.queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
	}
	
	private void initCellMap() {
		// Initializes the cell map
		this.model.clear();
		try {
			final Set<Callable<Void>> initTasks = new HashSet<>();
			final Dimension cellMapDimension = this.model.getCellMapDimension();
			// Randomly initializes the cell map to about 50% on-cells
			int initLength = (cellMapDimension.width * cellMapDimension.height) / 2;
			do {
				initTasks.add(new InitTask(model));
			} while (--initLength > 0);
			this.executor.invokeAll(initTasks);
		} catch (InterruptedException e) {
			// View -> initFailed
		}
		this.model.nextGeneration();
		this.isInitialized = true;
	}
	
	@Override
	public void start() {
		if (this.isInitialized && !this.isStarted) {
			stopFlag.setOff();
			new GameOfLifeService(this.queue, this.executor, this.model, this.view, this.stopFlag).start();
			isStarted = true;
			view.setStarted();
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
