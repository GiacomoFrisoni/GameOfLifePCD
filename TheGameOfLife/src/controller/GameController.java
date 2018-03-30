package controller;

import java.awt.Dimension;

public interface GameController {
	
	/**
	 * Starts the game.
	 */
	void start();
	
	/**
	 * Stops the game.
	 */
	void stop();
	
	/**
	 * Resets the game.
	 */
	void reset();
	
	/**
	 * This method returns the dimension of the cell map.
	 * @return the dimension of the cell map.
	 */
	Dimension getCellMapDimension();
	
}
