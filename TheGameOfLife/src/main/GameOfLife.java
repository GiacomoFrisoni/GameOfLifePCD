package main;

import javax.swing.SwingUtilities;

import controller.GameController;
import controller.GameControllerImpl;
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
		final GameOfLifeFrame view = new GameOfLifeFrameImpl();
		
		final GameController controller = new GameControllerImpl(view);
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
