package model;

import java.awt.Point;

/**
 * Implementation of {@link ConwayCell}.
 */
public class ConwayCellImpl implements ConwayCell {

	private final Point position;
	private boolean state;
	private byte onNeighborCount;
	
	/**
	 * Creates a Conway's cell.
	 * 
	 * @param position
	 * 		the cell position
	 */
	public ConwayCellImpl(final Point position) {
		this.position = position;
		this.state = false;
		this.onNeighborCount = 0;
	}
	
	@Override
	public Point getPosition() {
		return this.position;
	}
	
	@Override
	public boolean isAlive() {
		return this.state;
	}
	
	@Override
	public void setStateOn() {
		this.state = true;
	}
	
	@Override
	public void setStateOff() {
		this.state = false;
	}
	
	@Override
	public short getOnNeighborCount() {
		return this.onNeighborCount;
	}

	@Override
	public void incOnNeighborCount() {
		this.onNeighborCount++;
	}
	
	@Override
	public void decOnNeighborCount() {
		this.onNeighborCount--;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + onNeighborCount;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + (state ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof ConwayCellImpl
				&& this.position.getX() == ((ConwayCellImpl)obj).getPosition().getX()
				&& this.position.getY() == ((ConwayCellImpl)obj).getPosition().getY();
	}
	
	@Override
	public String toString() {
		return "Cell at (" + this.position.getX() + ", " + this.position.getY() + ") with "
				+ this.onNeighborCount + " neighbor" + (this.onNeighborCount == 1 ? "" : "s");
	}
}
