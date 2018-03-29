package controller;

import java.util.Optional;
import java.util.concurrent.Callable;

import model.ConwayCell;
import model.ConwayCellMap;

public class ComputeTask implements Callable<Optional<Boolean>> {

	private final ConwayCellMap model;
	private final ConwayCell cell;
	
	public ComputeTask(final ConwayCellMap model, final ConwayCell cell) {
		this.model = model;
		this.cell = cell;
	}
	
	@Override
	public Optional<Boolean> call() {
		return this.model.computeCell(this.cell);
	}

}
