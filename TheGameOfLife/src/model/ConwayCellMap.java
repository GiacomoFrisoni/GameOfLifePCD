package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

public interface ConwayCellMap {

	Dimension getCellMapDimension();
	
	Set<ConwayCell> getLastUpdatedCells();
	
	Set<ConwayCell> getCellsInRange(Point startPoint, int rangeDimension);
	
	void nextGeneration();
	
}
