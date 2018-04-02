package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.magicwerk.brownies.collections.BigList;

/**
 * This class represents the Model, as it contains all game of life elements.
 * Implementation of {@link ConwayCellMap}.
 * 
 */
public class ConwayCellMapImpl implements ConwayCellMap {
	
	private final Dimension mapDimension;
	private long generation;
	
	private static final int STATE_BIT = 4;
	private static final int NEIGHBORS_MASK = 0x07;
	private static final int STATE_MASK = 0x10;
	private static final int MIN_NEIGHBORS = 0;
	private static final int MAX_NEIGHBORS = 8;
	
	/*
	 * Cell map format to store a byte for each cell, with the byte storing
	 * not only the cell state but also the count of neighboring on-cells for
	 * that cell.
	 * The first 4 bit store the number of on-neighbors and the next one is the
	 * cell's state.
	 * 
	 */
	private byte[] cells;
	private byte[] nextCells;
	
	private BigList<Point> cellsToEvaluate;
	private long computedCells;
	
	
	/**
	 * Conway's cell map constructor.
	 * 
	 * @param width
	 * 		the width of the cell map
	 * @param height
	 * 		the height of the cell map
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
		this.cells = new byte[width * height];
		
		// Creates an unaltered version of the cell map from which to work
		this.nextCells = new byte[width * height];
		
		// Creates the list with the cells to evaluate for the current generation
		this.cellsToEvaluate = new BigList<>();
				
		// Initializes number of generations
		this.generation = 0;
		
		// Initializes number of computed cells
		this.computedCells = 0;
	}
	
	@Override
	public Dimension getCellMapDimension() {
		return new Dimension(this.mapDimension);
	}
	
	@Override
	public long getGenerationNumber() {
		return this.generation;
	}
	
	@Override
	public boolean[][] getCellMapStates() {
		final int height = this.mapDimension.height;
		final int width = this.mapDimension.width;
		final boolean[][] res = new boolean[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				res[i][j] = getState(this.cells[encode(j, i)]);
			}
		}
		return res;
	}
	
	/*
	 * Returns the state of the specified cell.
	 */
	private boolean getState(final byte cell) {
		return (cell & (1 << STATE_BIT)) != 0;
	}
	
	/*
	 * Returns the number of on-neighbors for the specified cell.
	 */
	private byte getCellOnNeighborCount(final byte cell) {
		return (byte)(cell & NEIGHBORS_MASK);
	}
	
	/*
	 * Converts 2D array index to 1D, according to the dimension of the cell map.
	 */
	private int encode(final int x, final int y) {
		return y * this.mapDimension.width + x;
	}
	
	/*
	 * Calculates the cells to evaluate for the current generation
	 * (it excludes off-cells with no alive neighbor).
	 */
	private void calculatesCellsToEvaluate() {
		this.cellsToEvaluate.clear();
		final int height = this.mapDimension.height;
		final int width = this.mapDimension.width;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				final byte cell = this.cells[encode(j, i)];
				final boolean state = getState(cell);
				if (state || (!state && getCellOnNeighborCount(cell) > 0)) {
					this.cellsToEvaluate.add(new Point(j, i));
				}
			}
		}
	}
	
	@Override
	public BigList<Point> getCellsToEvaluate() {
		return this.cellsToEvaluate;
	}
	
	/*
	 * Turns an off-cell on, incrementing the on-neighbor count for
	 * the eight neighboring cells.
	 */
	private void setCellStateOn(final int x, final int y) {
		final byte cell = this.cells[encode(x, y)];
		final boolean state = getState(cell);
		if (!state) {
			final int height = this.mapDimension.height;
			final int width = this.mapDimension.width;
			// Turns on the cell
			this.nextCells[encode(x, y)] |= (1 << STATE_BIT);
			// Increments the on-neighbor count for each neighbor
			for (int i = y - 1; i <= y + 1; i++) {
				for (int j = x - 1; j <= x + 1; j++) {
					if (j >= 0 && j < width && i >= 0 && i < height && (i != y || j != x)) {
						synchronized (this.nextCells) {
							final byte nextCell = this.nextCells[encode(j, i)];
							final byte nextNeighborsCounter = (byte)(Math.min(getCellOnNeighborCount(nextCell) + 1, MAX_NEIGHBORS));
							this.nextCells[encode(j, i)] &= STATE_MASK;
							this.nextCells[encode(j, i)] |= nextNeighborsCounter;
						}
					}
				}
			}
		}
	}
	
	/*
	 * Turns an on-cell off, decrementing the on-neighbor count for
	 * the eight neighboring cells.
	 */
	private void setCellStateOff(int x, int y) {
		final byte cell = this.cells[encode(x, y)];
		final boolean state = getState(cell);
		if (state) {
			final int height = this.mapDimension.height;
			final int width = this.mapDimension.width;
			// Turns off the cell
			this.nextCells[encode(x, y)] &= ~(1 << STATE_BIT);
			// Decrements the on-neighbor count for each neighbor
			for (int i = y - 1; i <= y + 1; i++) {
				for (int j = x - 1; j <= x + 1; j++) {
					if (j >= 0 && j < width && i >= 0 && i < height && (i != y || j != x)) {
						synchronized (this.nextCells) {
							final byte nextCell = this.nextCells[encode(j, i)];
							final byte nextNeighborsCounter = (byte)(Math.max(getCellOnNeighborCount(nextCell) - 1, MIN_NEIGHBORS));
							this.nextCells[encode(j, i)] &= STATE_MASK;
							this.nextCells[encode(j, i)] |= nextNeighborsCounter;
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean computeCell(final int x, final int y) {
		byte cell = this.cells[encode(x, y)];
		boolean state = getState(cell);
		boolean nextState = state;
		byte onNeighborCount = getCellOnNeighborCount(cell);
		if (state) {
			if ((onNeighborCount < 2) || (onNeighborCount > 3)) {
				setCellStateOff(x, y);
				nextState = false;
			}
		} else {
			if (onNeighborCount == 3) {
				setCellStateOn(x, y);
				nextState = true;
			}
		}
		this.computedCells++;
		return nextState;
	}
	
	@Override
	public double getPercentageCompletion() {
		return this.computedCells / this.cellsToEvaluate.size();
	}
	
	@Override
	public void nextGeneration() {
		// Sets current cell map = next cell map
		System.arraycopy(this.nextCells, 0, this.cells, 0, this.cells.length);
		// Calculates cells to evaluate in the new generation
		calculatesCellsToEvaluate();
		// Increments generation number
		this.generation++;
	}
	
	@Override
	public void clear() {
		Arrays.fill(this.cells, (byte)0);
		Arrays.fill(this.nextCells, (byte)0);
		this.cellsToEvaluate.clear();
		this.generation = 0;
		this.computedCells = 0;
	}
	
	@Override
	public void randomInitCell() {
		final int x = ThreadLocalRandom.current().nextInt(0, this.mapDimension.width);
		final int y = ThreadLocalRandom.current().nextInt(0, this.mapDimension.height);
		synchronized (this.nextCells) {
			if (!getState(this.nextCells[encode(x, y)])) {
				setCellStateOn(x, y);
			}
		}
	}
	
	@Override
	public String toString() {
		final StringBuilder res = new StringBuilder();
		res.append("Cell map at generation " + this.generation + "\n");
		final int height = this.mapDimension.height;
		final int width = this.mapDimension.width;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				final byte cell = this.cells[encode(j, i)];
				res.append(" " + (getState(cell) ? "O" : "X"));
				res.append("(" + getCellOnNeighborCount(cell) + ")");
			}
			res.append("\n");
		}
		return res.toString();
	}
}
