package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

public interface ConwayCellMap {

	Dimension getCellMapDimension();
	
	List<ConwayCell> getLastCellUpdated();
	
	List<ConwayCell> getCellsInRange(Point startPoint, int rangeDimension);
	
	void nextGeneration();
	
}
