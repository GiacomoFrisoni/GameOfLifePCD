package controller;

import java.awt.Dimension;

public interface GameController {
	/**
	 * This method returns the dimension of the cell map.
	 * @return the dimension of the cell map.
	 */
	Dimension getCellMapDimension();
	
	void start();
	
	void stop();
	
	void reset();
}
