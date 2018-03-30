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

public class GameOfLifeService extends Thread {

	private static final int BUFFER_SIZE = 100;
	
	private BlockingQueue<GenerationResult> queue;
	private ExecutorService executor;
	private final ConwayCellMap model;
	private final GameOfLifeFrame view;
	private final Flag stopFlag;
	
	public GameOfLifeService(final ConwayCellMap model, final GameOfLifeFrame view, final Flag stopFlag) {
		this.model = model;
		this.view = view;
		this.stopFlag = stopFlag;
	}
	
	@Override
	public void run() {
		if (this.model.getCellMap().isEmpty()) {
			this.stopFlag.setOn();
			//view.changeState("No point in Matrix!");
		}
		
		/*
		 * Calculates the poolSize for tasks executor, according to the processors
		 * number.
		 */
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		
		// Initializes the executor
		this.executor = Executors.newFixedThreadPool(poolSize);
		
		// Initializes the cell map
		try {
			initCellMap();
		} catch (InterruptedException e) {
			// View -> initFailed
		}
		this.model.nextGeneration();
		
		// Creates the producer / consumer queue
		this.queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
		
		// Starts producer and consumer threads
		new GameOfLifeProducer(this.queue, this.executor, this.model, this.stopFlag).start();
		new GameOfLifeConsumer(this.queue, this.view, this.stopFlag).start();
	}
	
	public void initCellMap() throws InterruptedException {
		final Set<Callable<Void>> initTasks = new HashSet<>();
		final Dimension cellMapDimension = this.model.getCellMapDimension();
		// Randomly initializes the cell map to about 50% on-cells
		int initLength = (cellMapDimension.width * cellMapDimension.height) / 2;
		do {
			initTasks.add(new InitTask(model));
		} while (--initLength > 0);
		this.executor.invokeAll(initTasks);
	}
}
