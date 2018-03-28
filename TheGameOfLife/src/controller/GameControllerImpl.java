package controller;

import model.ConwayCellMap;
import view.GameOfLifeFrame;

public class GameControllerImpl {
	
	private final ConwayCellMap model;
	private final GameOfLifeFrame view;
	private Flag stopFlag;
	
	public GameControllerImpl(final ConwayCellMap model, final GameOfLifeFrame view) {
		this.model = model;
		this.view = view;
	}
	
	public void start() {
		stopFlag = new Flag();
		new GameOfLifeService(model, view, stopFlag).start();
	}
	
	public void stop() {
		stopFlag.setOn();
	}
}
