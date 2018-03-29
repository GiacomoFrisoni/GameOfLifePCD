package model;

public enum CellNeighbor {
	
	TOP_LEFT(-1, -1),
	TOP(0, -1),
	TOP_RIGHT(1, -1),
	RIGHT(1, 0),
	BOTTOM_RIGHT(1, 1),
	BOTTOM(0, 1),
	BOTTOM_LEFT(-1, 1),
	LEFT(-1, 0);
	
	private final int xOffset;
	private final int yOffset;
	
	private CellNeighbor(final int xOffset, final int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public int getXOffset() {
		return this.xOffset;
	}
	
	public int getYOffset() {
		return this.yOffset;
	}
}
