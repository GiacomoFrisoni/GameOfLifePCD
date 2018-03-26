package model;

import java.awt.Dimension;

public interface ConwayCellMap {
	
	ConwayCell[][] getGrid();
	
	Dimension getGridDimension();
	
	void update();
	
	void clear();
	
}
