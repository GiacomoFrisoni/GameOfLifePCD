package controller;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.magicwerk.brownies.collections.BigList;

import model.ConwayCellMap;
import model.GenerationResult;

/**
 * This class models a Game Of Life Producer.
 * It enumerates the updated cells for each game generation and then
 * put the results on the queue.
 *
 */
public class GameOfLifeProducer extends Thread {
	
	private final BlockingQueue<GenerationResult> queue;
	private final ExecutorService executor;
	private final ConwayCellMap model;
	private final Flag stopFlag;
	
	/**
	 * Constructs a new Game Of Life producer.
	 * 
	 * @param queue
	 * 		the producer / consumer queue
	 * @param executor
	 * 		the executor service
	 * @param model
	 * 		the application model
	 * @param stopFlag
	 * 		the stop flag
	 */
	public GameOfLifeProducer(final BlockingQueue<GenerationResult> queue, final ExecutorService executor,
			final ConwayCellMap model, final Flag stopFlag) {
		this.queue = queue;
		this.executor = executor;
		this.model = model;
		this.stopFlag = stopFlag;
	}
	
	@Override
	public void run() {
		try {
			final Chrono cron = new Chrono();
			long cellsAlive;
			while (!stopFlag.isOn()) {
				cron.start();
				cellsAlive = 0;
				// Creates and collects the computational tasks
				final BigList<Callable<Boolean>> tasks = new BigList<>();
				final BigList<Point> cellsToEvaluate = this.model.getCellsToEvaluate();
				for (final Point cell : cellsToEvaluate) {
					tasks.add(new ComputeTask(model, cell.x, cell.y));
				}
				// Waits for tasks' results
				final List<Future<Boolean>> res = this.executor.invokeAll(tasks);
				for (final Future<Boolean> f : res) {
					if (f.get()) {
						cellsAlive++;
					}
				}
				cron.stop();
				// Prepares the next generation of the game
				this.model.nextGeneration();
				// Saves the generation results and statistics
				final GenerationResult generationResult = new GenerationResult(this.model.getGenerationNumber(),
						this.model.getCellMapStates(), cellsAlive, cron.getTime());
				/*
				 * The put() method will block if the queue is full, waiting for space becomes available.
				 * While waiting, it will throw InterruptedException if the current thread is interrupted.
				 */
				queue.put(generationResult);
			}
		} catch (InterruptedException | ExecutionException ie) {
			ie.printStackTrace();
			// Stop + view notification
		}
	}
}
