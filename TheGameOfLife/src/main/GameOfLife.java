package main;

import controller.GameControllerImpl;
import model.ConwayCellMap;
import model.ConwayCellMapImpl;
import view.GameOfLifeFrame;
import view.GameOfLifeFrameImpl;

/**
 * This is the launcher class for GameOfLife with MVC implementation.
 */
public final class GameOfLife {

	private static final int CELLMAP_WIDTH = 4;
	private static final int CELLMAP_HEIGHT = 4;

	private GameOfLife() { }
	
	/**
	 * The entry point for the application.
	 * 
	 * @param args
	 * 		not used
	 */
	public static void main(final String... args) {
		final ConwayCellMap model = new ConwayCellMapImpl(CELLMAP_WIDTH, CELLMAP_HEIGHT);
		final GameOfLifeFrame view = new GameOfLifeFrameImpl();
		new GameControllerImpl(model, view).start();
	}
}
