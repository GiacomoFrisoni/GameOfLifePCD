package model;

public class ConwayCellImpl implements ConwayCell {

	private boolean state;
	private short onNeighborCount;
	
	public ConwayCellImpl() {
		this.state = false;
		this.onNeighborCount = 0;
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
