package controller;

import java.util.concurrent.Callable;

import model.ConwayCellMap;

public class InitTask implements Callable<Void> {

	private final ConwayCellMap model;
	
	public InitTask(final ConwayCellMap model) {
		this.model = model;
	}
	
	@Override
	public Void call() throws Exception {
		this.model.randomInitCell();
		return null;
	}

}
