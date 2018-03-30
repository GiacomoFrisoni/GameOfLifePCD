package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	Map<Point,Boolean> getCellMap();
	
	/**
	 * @return current generation number.
	 */
	long getGenerationNumber();
	
	/**
	 * @return the cells to evaluate for current generation completion.
	 */
	Set<Point> getCellsToEvaluate();
	
	/**
	 * Applies game of life rules to the specified cell.
	 * The computation is performed only if the cell is among those
	 * to be evaluated in the current generation.
	 * 
	 * @param cell
	 * 		cell to compute
	 * @return the next status of the cell after the computation.
	 */
	Optional<Boolean> computeCell(Point cellPosition);
	
	/**
	 * Goes to the next generation of the game.
	 * The transition is carried out only if there are no more cells to be
	 * evaluated for the current generation.
	 * 
	 * @return true if the transition is performed, false otherwise.
	 */
	boolean nextGeneration();
	
	/**
	 * @return the cells updated in the last generation.
	 */
	Map<Point,Boolean> getLastUpdatedCells();
	
	/**
	 * Resets all data.
	 */
	void clear();
	
}
