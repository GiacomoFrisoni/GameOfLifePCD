package controller;

import java.awt.Point;
import java.util.Optional;
import java.util.concurrent.Callable;

import model.ConwayCellMap;

public class ComputeTask implements Callable<Optional<Boolean>> {

	private final ConwayCellMap model;
	private final Point cell;
	
	public ComputeTask(final ConwayCellMap model, final Point cell) {
		this.model = model;
		this.cell = cell;
	}
	
	@Override
	public Optional<Boolean> call() {
		return this.model.computeCell(this.cell);
	}

}
