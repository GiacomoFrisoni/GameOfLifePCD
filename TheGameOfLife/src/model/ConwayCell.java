package model;

public interface ConwayCell {

	boolean isAlive();
	
	void setStateOn();
	
	void setStateOff();
	
	short getOnNeighborCount();
	
	void incOnNeighborCount();
	
	void decOnNeighborCount();
	
}
