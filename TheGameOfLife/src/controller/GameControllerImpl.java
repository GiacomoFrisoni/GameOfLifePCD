package controller;

import java.awt.Dimension;

import model.ConwayCellMap;
import view.GameOfLifeFrame;

public class GameControllerImpl implements GameController {
	
	private final ConwayCellMap model;
	private final GameOfLifeFrame view;
	private Flag stopFlag;
	private boolean isStarted = false;
	
	public GameControllerImpl(final ConwayCellMap model, final GameOfLifeFrame view) {
		this.model = model;
		this.view = view;
	}
	
	public void start() {
		if (!isStarted) {
			stopFlag = new Flag();
			new GameOfLifeService(model, view, stopFlag).start();
			isStarted = true;
			view.setStarted();
		}
	}
	
	public void stop() {
		stopFlag.setOn();
		isStarted = false;
		view.setStopped();
	}
	
	public void reset() {
		stopFlag.setOn();
		isStarted = false;
		view.reset();
	}

	@Override
	public Dimension getCellMapDimension() {
		return this.model.getCellMapDimension();
	}
}
