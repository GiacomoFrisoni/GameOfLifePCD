package controller;

import java.util.concurrent.Callable;

import model.ConwayCellMap;

public class ComputeTask implements Callable<Boolean> {

	private final ConwayCellMap model;
	private final int cellX;
	private final int cellY;
	
	public ComputeTask(final ConwayCellMap model, final int cellX, final int cellY) {
		this.model = model;
		this.cellX = cellX;
		this.cellY = cellY;
	}
	
	@Override
	public Boolean call() {
		return this.model.computeCell(this.cellX, this.cellY);
	}

}
