package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	private List<ConwayCell> cells;
	private List<ConwayCell> nextCells;
	
	private Set<ConwayCell> cellsToEvaluate;
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
		
		// Creates an unaltered version of the cell map from which to work
		this.nextCells = new ArrayList<>(this.cells);
		
		// Creates a set with the cells updated in the last generation
		this.lastUpdatedCells = new HashSet<>();
		
		// Initializes the set with the cells to evaluate for the current generation
		this.cellsToEvaluate = new HashSet<>();
		
		// Randomly sets cell status in the map
		randomInit();
		
		// Initializes number of generations
		this.generation = 0;
	}
	
	@Override
	public Dimension getCellMapDimension() {
		return this.mapDimension;
	}
	
	@Override
	public Set<ConwayCell> getCellMap() {
		return new HashSet<>(this.cells);
	}
	
	@Override
	public long getGenerationNumber() {
		return this.generation;
	}
	
	/*
	 * Calculates the cells to be evaluated for the current generation completion.
	 * Excludes off-cells with no neighbors.
	 */
	private void calculateCellsToEvaluate() {
		this.cellsToEvaluate.clear();
		this.cellsToEvaluate.addAll(this.cells.stream()
				.filter(c -> (c.isAlive() || (!c.isAlive() && c.getOnNeighborCount() > 0)))
				.collect(Collectors.toSet()));
	}
	
	@Override
	public Set<ConwayCell> getCellsToEvaluate() {
		return this.cellsToEvaluate;
	}
	
	/*
	 * Check if a specified position is inside the cell map.
	 */
	private boolean isInsideCellMap(final int x, final int y) {
		return x >= 0 && x < this.mapDimension.width && y >= 0 && y < this.mapDimension.height;
	}
	
	/*
	 * Returns the cell of the specified cell map with the indicated position.
	 * If the point is outside the grid, it returns an empty optional.
	 */
	private Optional<ConwayCell> getCellByPosition(final List<ConwayCell> cellMap, final int x, final int y) {
		if (isInsideCellMap(x, y)) {
			return Optional.of(cellMap.get((y * this.mapDimension.width) + x));
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
		getCellByPosition(this.cells, cellPosition.x, cellPosition.y).ifPresent(cell -> {
			if (!cell.isAlive()) {
				// Turns on the cell state
				getCellByPosition(this.nextCells, cellPosition.x, cellPosition.y).get().setStateOn();
				// Increments the on-neighbor count for each neighbor
				for (final CellNeighbor neighborDir : CellNeighbor.values()) {
					getCellByPosition(this.nextCells,
							cell.getPosition().x + neighborDir.getXOffset(),
							cell.getPosition().y + neighborDir.getYOffset()).ifPresent(c -> c.incOnNeighborCount());
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
		getCellByPosition(this.cells, cellPosition.x, cellPosition.y).ifPresent(cell -> {
			if (cell.isAlive()) {
				// Turns off the cell state
				getCellByPosition(this.nextCells, cellPosition.x, cellPosition.y).get().setStateOff();
				// Decrements the on-neighbor count for each neighbor
				for (final CellNeighbor neighborDir : CellNeighbor.values()) {
					getCellByPosition(this.nextCells,
							cell.getPosition().x + neighborDir.getXOffset(),
							cell.getPosition().y + neighborDir.getYOffset()).ifPresent(c -> c.decOnNeighborCount());
				}
			}
		});
	}
	
	@Override
	public Optional<Boolean> computeCell(final ConwayCell cell) {
		if (this.cellsToEvaluate.contains(cell)) {
			Boolean nextStatus = cell.isAlive();
			final short count = cell.getOnNeighborCount();
			if (cell.isAlive()) {
				// Cell is on; turns it off if it doesn't have 2 or 3 neighbors
				if ((count < 2) && (count > 3)) {
					setCellStateOff(cell.getPosition());
					this.lastUpdatedCells.add(cell);
					nextStatus = false;
				}
			} else {
				// Cell is off; turns it on if it has exactly 3 neighbors
				if (count == 3) {
					setCellStateOn(cell.getPosition());
					this.lastUpdatedCells.add(cell);
					nextStatus = true;
				}
			}
			this.cellsToEvaluate.remove(cell);
			return Optional.of(nextStatus);
		}
		return Optional.empty();
	}
	
	@Override
	public boolean nextGeneration() {
		if (this.cellsToEvaluate.isEmpty()) {
			// Swaps cell map references for next generation
			final List<ConwayCell> tmp = this.nextCells;
			this.nextCells = this.cells;
			this.cells = tmp;
			// Clears last updated cells
			this.lastUpdatedCells.clear();
			// Calculate cells to evaluate in the new generation
			calculateCellsToEvaluate();
			// Increments generation number
			this.generation++;
			return true;
		}
		return false;
	}
	
	/*
	 * Resets all data.
	 */
	private void clear() {
		this.cells.forEach(c -> { c.setStateOff(); c.resetOnNeighborCount(); });
		this.nextCells = new ArrayList<>(this.cells);
		this.cellsToEvaluate.clear();
		this.lastUpdatedCells.clear();
		this.generation = 0;
	}
	
	@Override
	public void randomInit() {
		int x;
		int y;
		int initLength = (this.mapDimension.height * this.mapDimension.width) / 2;
		clear();
		// Randomly initializes the cell map to about 50% on-cells
		do {
			x = ThreadLocalRandom.current().nextInt(0, this.mapDimension.width);
			y = ThreadLocalRandom.current().nextInt(0, this.mapDimension.height);
			final ConwayCell cell = getCellByPosition(this.cells, x, y).get();
			if (!cell.isAlive()) {
				setCellStateOn(cell.getPosition());
				this.lastUpdatedCells.add(cell);
			}
		} while (--initLength > 0);
		// Calculates the cells to evaluate at start
		calculateCellsToEvaluate();
	}

	@Override
	public Set<ConwayCell> getLastUpdatedCellsInRegion(final Point startPoint, final Dimension regionDimension) {
		final Set<ConwayCell> getLastUpdatedCellsInRegion = new HashSet<>();
		if (!isInsideCellMap(startPoint.x, startPoint.y)) {
			throw new IllegalArgumentException("The start point must be inside the grid");
		}
		for (int i = startPoint.y; i < Math.min(startPoint.y + regionDimension.height, this.mapDimension.height); i++) {
			for (int j = startPoint.x; j < Math.min(startPoint.x + regionDimension.width, this.mapDimension.width); j++) {
				final ConwayCell cell = getCellByPosition(this.cells, i, j).get();
				if (lastUpdatedCells.contains(cell)) {
					getLastUpdatedCellsInRegion.add(cell);
				}
			}
		}
		return getLastUpdatedCellsInRegion;
	}
}
