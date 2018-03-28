package controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import model.ConwayCell;
import model.ConwayCellMap;

public class Master {

	private final ExecutorService executor;
	private final ConwayCellMap model;
	private final Flag stopFlag;
	
	public Master(final ConwayCellMap model, final int poolSize, final Flag stopFlag) {
		this.executor = Executors.newFixedThreadPool(poolSize);
		this.model = model;
		this.stopFlag = stopFlag;
	}
	
	public int compute() throws InterruptedException, ExecutionException {
		int cellsAlive = 0;
		final Set<ConwayCell> cellsToEvaluate = new HashSet<>(this.model.getCellsToEvaluate());
		for (final ConwayCell cell : cellsToEvaluate) {
			if (this.stopFlag.isOn())
				break;
			final Future<Optional<Boolean>> res = this.executor.submit(new ComputeTask(model, cell));
			if (res.get().isPresent()) {
				if (res.get().get()) {
					cellsAlive++;
				}
			}
		}
		return cellsAlive;
	}
	
}
