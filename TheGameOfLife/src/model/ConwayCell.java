package model;

import java.awt.Point;

/**
 * This interface handles a Game Of Life cell.
 *
 */
public interface ConwayCell {

	/**
	 * @return the position of the cell.
	 */
	Point getPosition();
	
	/**
	 * @return the status of the cell.
	 */
	boolean isAlive();
	
	/**
	 * Sets the cell alive.
	 */
	void setStateOn();
	
	/**
	 * Sets the cell death.
	 */
	void setStateOff();
	
	/**
	 * @return the number of alive neighbors.
	 */
	short getOnNeighborCount();
	
	/**
	 * Resets the number of alive neighbors.
	 */
	void resetOnNeighborCount();
	
	/**
	 * Increments the number of alive neighbors.
	 */
	void incOnNeighborCount();
	
	/**
	 * Decrements the number of death neighbors.
	 */
	void decOnNeighborCount();
	
}
