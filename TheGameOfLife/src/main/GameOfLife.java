package main;

import javax.swing.SwingUtilities;

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
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new MenuController();
            }
        });
	}
}
