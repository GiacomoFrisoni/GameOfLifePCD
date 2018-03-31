package model;

import java.awt.Dimension;
import java.awt.Point;

import org.magicwerk.brownies.collections.BigList;

/**
 * This interface handles a cell map for the Game Of Life.
 *
 */
public interface ConwayCellMap {

	/**
	 * Randomly initializes the status of the cells in the map.
	 */
	void randomInitCell();
	
	/**
	 * @return the dimension (width and height) of the cell map.
	 */
	Dimension getCellMapDimension();
	
	/**
	 * @return all the cells in the cell map.
	 */
	byte[] getCellMap();
	
	/**
	 * @return the cells to evaluate for current generation completion.
	 */
	BigList<Point> getCellsToEvaluate();
	
	/**
	 * @return current generation number.
	 */
	long getGenerationNumber();
	
	/**
	 * Applies game of life rules to the specified cell.
	 * The computation is performed only if the cell is among those
	 * to be evaluated in the current generation.
	 * 
	 * @param cell
	 * 		cell to compute
	 * @return the next status of the cell after the computation.
	 */
	boolean computeCell(int x, int y);
	
	/**
	 * Goes to the next generation of the game.
	 * The transition is carried out only if there are no more cells to be
	 * evaluated for the current generation.
	 * 
	 * @return true if the transition is performed, false otherwise.
	 */
	boolean nextGeneration();
	
	/**
	 * Resets all data.
	 */
	void clear();
	
}
