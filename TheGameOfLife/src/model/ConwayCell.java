package model;

import java.awt.Point;

public interface ConwayCell {

	Point getPosition();
	
	boolean isAlive();
	
	void setStateOn();
	
	void setStateOff();
	
	short getOnNeighborCount();
	
	void incOnNeighborCount();
	
	void decOnNeighborCount();
	
}
