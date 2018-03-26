package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ConwayCellMapImpl implements ConwayCellMap {

	private final Dimension mapDimension;
	private long generation;
	
	private List<ConwayCell> cells;
	private List<ConwayCell> nextCells;
	
	private Set<ConwayCell> lastUpdatedCells;
	
	/**
	 * Conway's cell map constructor.
	 * 
	 * @param width
	 * 		width of the cell map
	 * @param height
	 * 		height of the cell map
	 */
	public ConwayCellMapImpl(final int width, final int height) {
		// Checks cell map dimension
		if (width < 1) {
			throw new IllegalArgumentException("Cell map width must be positive");
		}
		if (height < 1) {
			throw new IllegalArgumentException("Cell map height must be positive");
		}
		
		// Sets cell map dimension
		this.mapDimension = new Dimension(width, height);
		
		// Creates the cell map
		this.cells = new ArrayList<>(width * height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells.add(i * width + j, new ConwayCellImpl(new Point(i, j)));
			}
		}
		
		// Creates a set with the cells updated in the last generation
		this.lastUpdatedCells = new HashSet<>();
		
		// Randomly sets cell status in the map
		randomInit();
				
		// Creates an unaltered version of the cell map from which to work
		this.nextCells = new ArrayList<>(this.cells);
		
		// Initializes number of generations
		this.generation = 0;
	}
	
	/*
	 * Returns the cell of the specified cell map with the indicated position.
	 */
	private ConwayCell getCellByPosition(final List<ConwayCell> cellMap, final int x, final int y) {
		return cellMap.get((y * (int)this.mapDimension.getWidth()) + x);
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
	private void setCellStateOn(final int x, final int y) {
		if (!getCellByPosition(this.cells, x, y).isAlive()) {
			// Turns on the cell state
			getCellByPosition(this.nextCells, x, y).setStateOn();
			// Increments the on-neighbor count for each neighbor
			for (CellNeighbor neighborDir : CellNeighbor.values()) {
				getCellByPosition(this.nextCells, x + neighborDir.getXOffset(), y + neighborDir.getYOffset()).incOnNeighborCount();
			}
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
	private void setCellStateOff(final int x, final int y) {
		if (getCellByPosition(this.cells, x, y).isAlive()) {
			// Turns off the cell state
			getCellByPosition(this.nextCells, x, y).setStateOff();
			// Decrements the on-neighbor count for each neighbor
			for (CellNeighbor neighborDir : CellNeighbor.values()) {
				getCellByPosition(this.nextCells, x + neighborDir.getXOffset(), y + neighborDir.getYOffset()).decOnNeighborCount();
			}
		}
	}
	
	/**
	 * Calculates the next generation of current map.
	 */
	public void nextGeneration() {
		// Increments generation number
		this.generation++;
		
		// Resets updated cells
		this.lastUpdatedCells.clear();
		
		// Processes all cells in the cell map
		rowDone:
		for (int i = 0, j = 0; i < (int)this.mapDimension.getHeight(); i++) {
			// Processes all cells in the current row of the cell map
			do {
				ConwayCell tmpCell;
				
				// Goes quickly through as many off-cells with no neighbors as possible
				tmpCell = getCellByPosition(this.cells, i, j);
				while (!tmpCell.isAlive() && tmpCell.getOnNeighborCount() == 0) {
					if (++j >= (int)this.mapDimension.getWidth()) {
						continue rowDone;
					}
					tmpCell = getCellByPosition(this.cells, i, j);
				}
				/*
				 * Checks if the state of the cell (which is either on or has
				 * on-neighbors) needs to be changed.
				 */
				final short count = tmpCell.getOnNeighborCount();
				if (tmpCell.isAlive()) {
					// Cell is on; turns it off if it doesn't have 2 or 3 neighbors
					if ((count != 2) && (count != 3)) {
						setCellStateOff(i, j);
						this.lastUpdatedCells.add(tmpCell);
					} else {
						// Cell is off; turns it on if it has exactly 3 neighbors
						if (count == 3) {
							setCellStateOn(i, j);
							this.lastUpdatedCells.add(tmpCell);
						}
					}
				}
			} while (++j < this.mapDimension.getWidth());
		}
		
		// Swap cell map references for next generation
		final List<ConwayCell> tmp = this.nextCells;
		this.nextCells = this.cells;
		this.cells = tmp;
	}
	
	/*
	 * Randomly initializes the cell map to about 50% on-cells.
	 */
	private void randomInit() {
		int x;
		int y;
		int initLength = (int)((this.mapDimension.getHeight() * this.mapDimension.getWidth()) / 2);
		this.lastUpdatedCells.clear();
		do {
			x = ThreadLocalRandom.current().nextInt(0, (int)this.mapDimension.getWidth());
			y = ThreadLocalRandom.current().nextInt(0, (int)this.mapDimension.getHeight());
			final ConwayCell cell = getCellByPosition(this.cells, x, y);
			if (!cell.isAlive()) {
				setCellStateOn(x, y);
				this.lastUpdatedCells.add(cell);
			}
		} while (--initLength > 0);
	}

	@Override
	public Dimension getCellMapDimension() {
		return this.mapDimension;
	}

	@Override
	public Set<ConwayCell> getLastUpdatedCells() {
		return this.lastUpdatedCells;
	}

	@Override
	public Set<ConwayCell> getCellsInRange(Point startPoint, int rangeDimension) {
		// TODO Auto-generated method stub
		return null;
	}
}
