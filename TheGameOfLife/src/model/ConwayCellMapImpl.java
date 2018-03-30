package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * This class represents the Model, as it contains all game of life elements.
 * Implementation of {@link ConwayCellMap}.
 * 
 */
public class ConwayCellMapImpl implements ConwayCellMap {
	
	private final Dimension mapDimension;
	private long generation;
	
	private boolean[] cells;
	private boolean[] nextCells;
	private byte[] neighborgs;
	
	private Map<Point,Boolean> lastUpdatedCells;
	private Set<Point> cellsToEvaluate;
	
	
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
		this.cells = new boolean[width * height];
		
		// Creates an unaltered version of the cell map from which to work
		this.nextCells = new boolean[width * height];
		
		// Creates an array with the neighbors of the cells
		this.neighborgs = new byte[width * height];
		
		// Creates a map with the cells updated in the last generation
		this.lastUpdatedCells = new HashMap<>();
		
		// Creates the set with the cells to evaluate for the current generation
		this.cellsToEvaluate = Collections.synchronizedSet(new HashSet<>());
		
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
	public Map<Point,Boolean> getCellMap() {
		final Map<Point,Boolean> cellMap = new HashMap<>();
		for (int i = 0; i < this.mapDimension.height; i++) {
			for (int j = 0; j < this.mapDimension.width; j++) {
				cellMap.put(new Point(j, i), getCellStatusByPosition(this.cells, i, j).get());
			}
		}
		return cellMap;
	}
	
	/*
	 * Calculates the cells to be evaluated for the current generation completion.
	 * Excludes off-cells with no neighbors.
	 */
	private void calculateCellsToEvaluate() {
		this.cellsToEvaluate.clear();
		for (int i = 0; i < this.mapDimension.getHeight(); i++) {
			for (int j = 0; j < this.mapDimension.getWidth(); j++) {
				int index = getOneDimensionalIndex(j, i);
				if (this.cells[index] || (!this.cells[index] && this.neighborgs[index] > 0)) {
					this.cellsToEvaluate.add(new Point(j, i));
				}
			}
		}
	}
	
	@Override
	public Set<Point> getCellsToEvaluate() {
		final Set<Point> res = new HashSet<>();
		for (final Point cell : this.cellsToEvaluate) {
			res.add(new Point(cell));
		}
		return Collections.unmodifiableSet(res);
	}
	
	/*
	 * Check if a specified position is inside the cell map.
	 */
	private boolean isInsideCellMap(final int x, final int y) {
		return x >= 0 && x < this.mapDimension.width && y >= 0 && y < this.mapDimension.height;
	}
	
	private int getOneDimensionalIndex(final int x, final int y) {
		return (y * this.mapDimension.width) + x;
	}
	
	private Optional<Boolean> getCellStatusByPosition(final boolean[] cellMap, final int x, final int y) {
		if (isInsideCellMap(x, y)) {
			return Optional.of(cellMap[getOneDimensionalIndex(x, y)]);
		} else {
			return Optional.empty();
		}
	}
	
	/*
	 * Turns an off-cell on, incrementing the on-neighbor count for
	 * the eight neighboring cells.
	 * 
	 * @param cellPosition
	 * 		the position of the cell to set on
	 */
	private void setCellStateOn(final Point cellPosition) {
		getCellStatusByPosition(this.cells, cellPosition.x, cellPosition.y).ifPresent(status -> {
			if (!status) {
				// Turns on the cell state
				this.nextCells[getOneDimensionalIndex(cellPosition.x, cellPosition.y)] = true;
				// Increments the on-neighbor count for each neighbor
				for (int yOffset = -1; yOffset < 1; yOffset++) {
					for (int xOffset = -1; xOffset < 1; xOffset++) {
						int neighborX = cellPosition.x + xOffset;
						int neighborY = cellPosition.y + yOffset;
						getCellStatusByPosition(this.cells, neighborX, neighborY)
						.ifPresent(val -> this.neighborgs[getOneDimensionalIndex(neighborX, neighborY)]++);
					}
				}
			}
		});
	}
	
	/*
	 * Turns an on-cell off, decrementing the on-neighbor count for
	 * the eight neighboring cells.
	 * 
	 * @param cellPosition
	 * 		the position of the cell to set off
	 */
	private void setCellStateOff(final Point cellPosition) {
		getCellStatusByPosition(this.cells, cellPosition.x, cellPosition.y).ifPresent(status -> {
			if (status) {
				// Turns on the cell state
				this.nextCells[getOneDimensionalIndex(cellPosition.x, cellPosition.y)] = false;
				// Increments the on-neighbor count for each neighbor
				for (int yOffset = -1; yOffset < 1; yOffset++) {
					for (int xOffset = -1; xOffset < 1; xOffset++) {
						int neighborX = cellPosition.x + xOffset;
						int neighborY = cellPosition.y + yOffset;
						getCellStatusByPosition(this.cells, neighborX, neighborY)
						.ifPresent(val -> this.neighborgs[getOneDimensionalIndex(neighborX, neighborY)]--);
					}
				}
			}
		});
	}
	
	@Override
	public Optional<Boolean> computeCell(final Point cellPosition) {
		Objects.requireNonNull(cellPosition);
		Optional<Boolean> state = getCellStatusByPosition(this.cells, cellPosition.x, cellPosition.y);
		if (state.isPresent()) {
			boolean nextState = state.get();
			int index = getOneDimensionalIndex(cellPosition.x, cellPosition.y);
			byte onNeighborCount = this.neighborgs[index];
			if (state.get()) {
				if ((onNeighborCount < 2) || (onNeighborCount > 3)) {
					setCellStateOff(cellPosition);
					this.lastUpdatedCells.put(cellPosition, false);
					nextState = false;
				}
			} else {
				if (onNeighborCount == 3) {
					this.nextCells[getOneDimensionalIndex(cellPosition.x, cellPosition.y)] = true;
					this.lastUpdatedCells.put(cellPosition, true);
					nextState = true;
				}
			}
			return Optional.of(nextState);
		}
		return Optional.empty();
	}
	
	@Override
	public boolean nextGeneration() {
		if (this.cellsToEvaluate.isEmpty()) {
			// Set current cell map = next cell map
			System.arraycopy(this.nextCells, 0, this.cells, 0, this.cells.length);
			// Clears last updated cells
			this.lastUpdatedCells.clear();
			// Calculates cells to evaluate in the new generation
			calculateCellsToEvaluate();
			// Increments generation number
			this.generation++;
			return true;
		}
		return false;
	}
	
	@Override
	public void clear() {
		Arrays.fill(this.cells, false);
		Arrays.fill(this.nextCells, false);
		this.cellsToEvaluate.clear();
		this.lastUpdatedCells.clear();
		this.generation = 0;
	}
	
	@Override
	public void randomInitCell() {
		int x = ThreadLocalRandom.current().nextInt(0, this.mapDimension.width);
		int y = ThreadLocalRandom.current().nextInt(0, this.mapDimension.height);
		final Point initPoint = new Point(x, y);
		synchronized(this.lastUpdatedCells) {
			if (!this.lastUpdatedCells.containsKey(initPoint)) {
				setCellStateOn(initPoint);
				this.lastUpdatedCells.put(initPoint, true);
			}
		}
	}
	
	@Override
	public Map<Point,Boolean> getLastUpdatedCells() {
		return Collections.unmodifiableMap(this.lastUpdatedCells);
	}
}
