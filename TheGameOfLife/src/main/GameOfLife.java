package main;

import javax.swing.SwingUtilities;

import controller.GameController;
import controller.GameControllerImpl;
import model.ConwayCellMap;
import model.ConwayCellMapImpl;
import view.GameOfLifeFrame;
import view.GameOfLifeFrameImpl;

/**
 * This is the launcher class for GameOfLife with MVC implementation.
 */
public final class GameOfLife {


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
		
		final GameController controller = new GameControllerImpl(model, view);
		controller.reset();
		view.setObserver(controller);
		view.initView();
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		view.showView();
            }
        });
	}
}
