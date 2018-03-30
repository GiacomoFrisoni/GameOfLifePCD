package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import model.ConwayCell;
import model.ConwayCellMap;
import model.GenerationResult;

/**
 * This producer enumerates the updated cells for each
 * GameOfLive generation and then put the result on the queue.
 *
 */
public class GameOfLifeProducer extends Thread {
	
	private final BlockingQueue<GenerationResult> queue;
	private final ExecutorService executor;
	private final ConwayCellMap model;
	private final Flag stopFlag;
	
	public GameOfLifeProducer(final BlockingQueue<GenerationResult> queue,
			ConwayCellMap model, int poolSize, Flag stopFlag) {
		this.queue = queue;
		this.executor = Executors.newFixedThreadPool(poolSize);
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
				final Set<ConwayCell> cellsToEvaluate = this.model.getCellsToEvaluate();
				final Set<Callable<Optional<Boolean>>> tasks = new HashSet<>();
				for (final ConwayCell cell : cellsToEvaluate) {
					if (this.stopFlag.isOn())
						break;
					tasks.add(new ComputeTask(model, cell));
				}
				final List<Future<Optional<Boolean>>> res = this.executor.invokeAll(tasks);
				for (final Future<Optional<Boolean>> f : res) {
					if (f.get().isPresent()) {
						if (f.get().get()) {
							cellsAlive++;
						}
					}
				}
				cron.stop();
				final GenerationResult generationResult = new GenerationResult(this.model.getGenerationNumber(),
						this.model.getCellMap(), cellsAlive, cron.getTime());
				/*
				 * The put() method will block if the queue is full, waiting for space becomes available.
				 * While waiting, it will throw InterruptedException if the current thread is interrupted.
				 */
				queue.put(generationResult);
				// Prepare nextGeneration
				this.model.nextGeneration();
			}
		} catch (InterruptedException | ExecutionException ie) {
			ie.printStackTrace();
			// Stop + view notification
		}
	}
}
