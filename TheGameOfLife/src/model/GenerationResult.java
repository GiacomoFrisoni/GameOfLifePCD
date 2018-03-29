package model;

import java.util.Set;

public class GenerationResult {
	
	private final long generationNumber;
	private final Set<ConwayCell> updatedCells;
	private final long cellsAlive;
	private final long computationTime;
	
	public GenerationResult(final long generationNumber, final Set<ConwayCell> updatedCells,
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
	
	public Set<ConwayCell> getUpdatedCells() {
		return this.updatedCells;
	}
	
	public long getComputationTime() {
		return this.computationTime;
	}
	
}
