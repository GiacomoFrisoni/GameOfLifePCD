package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
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
	
	public long compute() throws InterruptedException, ExecutionException {
		long cellsAlive = 0;
		final Set<ConwayCell> cellsToEvaluate = this.model.getCellsToEvaluate();
		final Set<Callable<Optional<Boolean>>> tasks = new HashSet<>();
		for (final ConwayCell cell : cellsToEvaluate) {
			if (this.stopFlag.isOn())
				break;
			tasks.add(new ComputeTask(model, cell));
		}
		final List<Future<Optional<Boolean>>> res = this.executor.invokeAll(tasks);
		for (Future<Optional<Boolean>> f : res) {
			if (f.get().isPresent()) {
				if (f.get().get()) {
					cellsAlive++;
				}
			}
		}
		return cellsAlive;
	}
	
}
