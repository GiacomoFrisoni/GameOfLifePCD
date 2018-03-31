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
	
	private byte[] cells;
	private byte[] nextCells;
	
	private BigList<Point> cellsToEvaluate;
	
	
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
		this.cells = new byte[width * height];
		
		// Creates an unaltered version of the cell map from which to work
		this.nextCells = new byte[width * height];
		
		// Creates the set with the cells to evaluate for the current generation
		this.cellsToEvaluate = new BigList<>();
				
		// Initializes number of generations
		this.generation = 0;
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
	public byte[] getCellMap() {
		return this.cells;
	}
	
	private void calculatesCellsToEvaluate() {
		this.cellsToEvaluate.clear();
		final int height = this.mapDimension.height;
		final int width = this.mapDimension.width;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int index = encode(j, i);
				byte cell = this.cells[index];
				boolean state = getState(cell); 
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
	
	private void setCellStateOn(final byte cell, int x, int y) {
		boolean state = getState(cell);
		if (!state) {
			// Turns on the cell
			this.nextCells[encode(x, y)] = (byte) (cell | (1 << STATE_BIT));
			// Increments the on-neighbor count for each neighbor
			for (int i = y - 1; i <= y + 1; i++) {
				for (int j = x - 1; j <= x + 1; j++) {
					if (j >= 0 && j < this.mapDimension.width && i >= 0 && i < this.mapDimension.height && (i != y || j != x)) {
						byte nextCounter = (byte)(Math.min(getCellOnNeighborCount(cell) + 1, 8));
						this.nextCells[encode(j, i)] = (byte) (((state ? 1 : 0) << STATE_BIT) | nextCounter);
					}
				}
			}
		}
	}
	
	private void setCellStateOff(final byte cell, int x, int y) {
		boolean state = getState(cell);
		if (!state) {
			// Turns off the cell
			this.nextCells[encode(x, y)] = (byte) (cell & ~(1 << STATE_BIT));
			// Decrements the on-neighbor count for each neighbor
			for (int i = y - 1; i < y + 1; i++) {
				for (int j = x - 1; x < x + 1; j++) {
					if (i != y && j != x) {
						byte nextCounter = (byte)(Math.max(getCellOnNeighborCount(cell) - 1, 0));
						this.nextCells[encode(x, y)] = (byte) (((state ? 1 : 0) << STATE_BIT) | nextCounter);
					}
				}
			}
		}
	}
	
	private boolean getState(final byte cell) {
		return (cell & (1 << STATE_BIT)) != 0;
	}
	
	private byte getCellOnNeighborCount(final byte cell) {
		return (byte)(cell & 0x07);
	}
	
	private int encode(final int x, final int y) {
		return y * this.mapDimension.width + x;
	}
	
	@Override
	public boolean computeCell(final int x, final int y) {
		byte cell = this.cells[encode(x, y)];
		boolean state = getState(cell);
		boolean nextState = state;
		byte onNeighborCount = getCellOnNeighborCount(cell);
		if (state) {
			if ((onNeighborCount < 2) || (onNeighborCount > 3)) {
				setCellStateOff(cell, x, y);
				nextState = false;
			}
		} else {
			if (onNeighborCount == 3) {
				setCellStateOn(cell, x, y);
				nextState = true;
			}
		}
		return nextState;
	}
	
	@Override
	public boolean nextGeneration() {
		// Sets current cell map = next cell map
		System.arraycopy(this.nextCells, 0, this.cells, 0, this.cells.length);
		// Calculates cells to evaluate in the new generation
		calculatesCellsToEvaluate();
		// Increments generation number
		this.generation++;
		return true;
	}
	
	@Override
	public void clear() {
		Arrays.fill(this.cells, (byte)0);
		Arrays.fill(this.nextCells, (byte)0);
		this.cellsToEvaluate.clear();
		this.generation = 0;
	}
	
	@Override
	public void randomInitCell() {
		int x = ThreadLocalRandom.current().nextInt(0, this.mapDimension.width);
		int y = ThreadLocalRandom.current().nextInt(0, this.mapDimension.height);
		synchronized (this.nextCells) {
			if (!getState(this.nextCells[encode(x, y)])) {
				setCellStateOn(this.cells[encode(x, y)], x, y);
			}
		}
	}
}
