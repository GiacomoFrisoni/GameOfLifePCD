package model;

import java.awt.Point;

public class ConwayCellImpl implements ConwayCell {

	private Point position;
	private boolean state;
	private short onNeighborCount;
	
	public ConwayCellImpl(final int x, final int y) {
		this.position = new Point(x, y);
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
}
