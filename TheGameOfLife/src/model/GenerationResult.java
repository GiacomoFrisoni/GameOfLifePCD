package model;

import java.awt.Point;
import java.util.Map;

public class GenerationResult {
	
	private final long generationNumber;
	private final Map<Point,Boolean> updatedCells;
	private final long cellsAlive;
	private final long computationTime;
	
	public GenerationResult(final long generationNumber, final Map<Point,Boolean> updatedCells,
			final long cellsAlive, final long computationTime) {
		this.generationNumber = generationNumber;
		this.updatedCells = updatedCells;
		this.cellsAlive = cellsAlive;
		this.computationTime = computationTime;
	}
	
	public long getGenerationNumber() {
		return this.generationNumber;
	}
	
	public long getCellsAlive() {
		return this.cellsAlive;
	}
	
	public Map<Point,Boolean> getUpdatedCells() {
		return this.updatedCells;
	}
	
	public long getComputationTime() {
		return this.computationTime;
	}
	
}
