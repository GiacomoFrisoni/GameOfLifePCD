package model;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ConwayCellMapImpl implements ConwayCellMap {

	private Dimension mapDimension;
	private long generation;
	
	private ConwayCell[] cells;
	private ConwayCell[] tmpCells;
	
	/**
	 * Conway's cell map constructor.
	 * 
	 * @param width
	 * 		width of the cell map
	 * @param height
	 * 		height of the cell map
	 */
	public ConwayCellMapImpl(int width, int height) {
		// Sets cell map dimension
		this.mapDimension = new Dimension(width, height);
		
		// Creates cell maps
		cells = new ConwayCell[width * height];
		tmpCells = new ConwayCell[width * height];
		
		// Initializes number of generations
		this.generation = 0;
		
		// Randomly initializes the cell map
		randomInit();
	}
	
	private ConwayCell getCellByCoordinates(int x, int y) {
		return this.cells[(y * (int)this.mapDimension.getWidth()) + x];
	}
	
	private ConwayCell getCellByCoordinates(int x, int y, int offset) {
		return this.cells[(y * (int)this.mapDimension.getWidth()) + x + offset];
	}
	
	private Set<ConwayCell> getCellNeighbors(int x, int y) {
		int xoleft, xoright, yoabove, yobelow;
		
		/*
		 * Calculates the offsets to the eight neighboring cells,
		 * accounting for wrapping around at the edges of the cell map.
		 */
		if (x == 0) {
			xoleft = (int)this.mapDimension.getWidth() - 1;
		} else {
			xoleft = -1;
		}
		if (y == 0) {
			yoabove = (int)((this.mapDimension.getWidth() * this.mapDimension.getHeight()) -
					this.mapDimension.getWidth());
		} else {
			yoabove = - (int)this.mapDimension.getWidth();
		}
		if (x == (int)this.mapDimension.getWidth() - 1) {
			xoright = - ((int)this.mapDimension.getWidth() - 1);
		} else {
			xoright = 1;
		}
		if (y == ((int)this.mapDimension.getHeight() - 1)) {
			yobelow = - (int)((this.mapDimension.getWidth() * this.mapDimension.getHeight()) -
					this.mapDimension.getWidth());
		} else {
			yobelow = (int)this.mapDimension.getWidth();
		}
		
		return new HashSet<ConwayCell>(Arrays.asList(getCellByCoordinates(x, y, yoabove + xoleft),
				getCellByCoordinates(x, y, yoabove),
				getCellByCoordinates(x, y, yoabove + xoright),
				getCellByCoordinates(x, y, xoleft),
				getCellByCoordinates(x, y, xoright),
				getCellByCoordinates(x, y, yobelow + xoleft),
				getCellByCoordinates(x, y, yobelow),
				getCellByCoordinates(x, y, yobelow + xoright)));
	}
	
	/*
	 * Turns an off-cell on, incrementing the on-neighbor count for
	 * the eight neighboring cells.
	 * 
	 * @param x
	 * 		x coordinate of the cell
	 * @param y
	 * 		y coordinate of the cell
	 */
	private void setCellStateOn(int x, int y) {
		if (!getCellByCoordinates(x, y).isAlive()) {
			// Turns on the cell state
			getCellByCoordinates(x, y).setStateOn();
			// Increments the on-neighbor count for each neighbor
			getCellNeighbors(x, y).forEach(c -> c.incOnNeighborCount());
		}
	}
	
	/*
	 * Turns an on-cell off, decrementing the on-neighbor count for
	 * the eight neighboring cells.
	 * 
	 * @param x
	 * 		x coordinate of the cell
	 * @param y
	 * 		y coordinate of the cell
	 */
	private void setCellStateOff(int x, int y) {
		if (getCellByCoordinates(x, y).isAlive()) {
			// Turns off the cell state
			getCellByCoordinates(x, y).setStateOff();
			// Decrements the on-neighbor count for each neighbor
			getCellNeighbors(x, y).forEach(c -> c.decOnNeighborCount());
		}
	}
	
	/**
	 * Calculates the next generation of current map.
	 */
	public void nextGeneration() {
		// Increments generations number
		generation++;
		
		// Creates an unaltered version of the cell map from which to work
		tmpCells = Arrays.copyOf(cells, cells.length);
		
		// Processes all cells in the cell map
		rowDone:
		for (int i = 0, j = 0; i < (int)this.mapDimension.getHeight(); i++) {
			// Processes all cells in the current row of the cell map
			do {
				// Goes quickly through as many off-cells with no neighbors as possible
				while (!getCellByCoordinates(i, j).isAlive()) {
					if (++j >= (int)this.mapDimension.getWidth()) {
						continue rowDone;
					}
				}
				/*
				 * Checks if the state of the cell (which is either on or has
				 * on-neighbors) needs to be changed.
				 */
				short count = getCellByCoordinates(i, j).getOnNeighborCount();
				if (getCellByCoordinates(i, j).isAlive()) {
					// Cell is on; turns it off if it doesn't have 2 or 3 neighbors
					if ((count != 2) && (count != 3)) {
						setCellStateOff(i, j);
						// drawPixel
					} else {
						// Cell is off; turns it on if it has exactly 3 neighbors
						if (count == 3) {
							setCellStateOn(i, j);
							// drawPixel
						}
					}
				}
			} while (++j < this.mapDimension.getWidth());
		}
	}
	
	/**
	 * Randomly initializes the cell map to about 50% on-cells.
	 */
	private void randomInit() {
		int x, y;
		int initLength = (int)((this.mapDimension.getHeight() * this.mapDimension.getWidth()) / 2);
		do {
			x = ThreadLocalRandom.current().nextInt(0, (int)this.mapDimension.getWidth());
			y = ThreadLocalRandom.current().nextInt(0, (int)this.mapDimension.getHeight());
			setCellStateOn(x, y);
		} while (--initLength > 0);
	}
}
