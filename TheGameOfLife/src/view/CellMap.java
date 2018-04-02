package view;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CellMap extends Canvas {
	
	private static final int CELL_SIZE = 9;
	private static final int CELL_OFFSET = CELL_SIZE + 1;
	private static final Color BACKGROUND_COLOR = new Color(0.23, 0.23, 0.23, 1);
	private static final Color ALIVE_CELL_COLOR = Color.AQUA;
	
	private CellMapViewer container;
	private boolean[][] cells;
	
	private int xPosition, yPosition;
	
	
	public CellMap() { }
	
	
	/**
	 * Set cells to draw on screen
	 * @param cells
	 * 		cells to draw
	 */
	public void setCellsToDraw (final boolean[][] cells) {
		this.cells = cells;
		draw();
	}
	
	/**
	 * Set the container of the CellMap
	 * @param container
	 * 		container of the CellMap
	 */
	public void setContainer(final CellMapViewer container) {
		this.container = container;
		this.setWidth(this.container.getCenterPanelX());
		this.setHeight(this.container.getCenterPanelY());
	}
	
	/**
	 * Clear all drawing in the CellMap
	 */
	private void clear() {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				//Create the graphics
				final GraphicsContext gc = getGraphicsContext2D();
				gc.fillRect(0, 0, getWidth(), getHeight());
			}
		});
	}
	
	/**
	 * Update current preview position of the map
	 * @param x
	 * 		x position of preview
	 * @param y
	 * 		y position of preview
	 */
	public void updatePosition(final int x, final int y) {
		this.xPosition = x;
		this.yPosition = y;
		clear();
		draw();
	}
	
	/**
	 * Draw the cells considering current position
	 */
	private void draw() {
		if (cells != null) {
			//Getting current position of preview (of total map)
			final int containerXposition = this.xPosition;
			final int containerYposition = this.yPosition;
			
			//Getting how many squares I can draw in X and Y
			final int drawableXCells = getDrawableXCellsNumber();
			final int drawableYCells = getDrawableYCellsNumber();
			
			//X value of inferior limit of cells I'm able to draw
			final int xOffset = containerXposition * drawableXCells;
			final int yOffset = containerYposition * drawableYCells;
			
			//X value of superior limit of cells I'm able to draw
			final int xMaxOffset = (containerXposition + 1) * drawableXCells;
			final int yMaxOffset = (containerYposition + 1) * drawableYCells;
			
			//Take the min of MAX (144) and real matrix preview (may be minor, 10x10 have 10x and 10y limit)
			final int minX = Math.min(xMaxOffset, cells[0].length);
			final int minY = Math.min(yMaxOffset, cells.length);
	
			//Draw (x must be from minOffset to maxOffset, same y)
			Platform.runLater(new Runnable() {			
				@Override
				public void run() {
					//Create the graphics
					final GraphicsContext gc = getGraphicsContext2D();
					gc.clearRect(0, 0, getWidth(), getHeight());
					
		
					//Draw (x must be from minOffset to maxOffset, same y)
					Platform.runLater(new Runnable() {			
						@Override
						public void run() {		
							//Create the graphics and clear the previous
							final GraphicsContext gc = getGraphicsContext2D();
							gc.clearRect(0, 0, getWidth(), getHeight());
							
							//Draw inside the limits
							for (int i = xOffset; i < minX; i++) {
								for (int j = yOffset; j < minY; j++) {
									
									//Check if cell is alive (for coloring)
									if (cells[i][j]) {
				        				gc.setFill(ALIVE_CELL_COLOR);
				            		} else {
				            			gc.setFill(BACKGROUND_COLOR);
				            		}       	
									
				        			//Drawing
				        			gc.fillRect((i - xOffset) * CELL_OFFSET, (j - yOffset) * CELL_OFFSET, CELL_SIZE, CELL_SIZE);
								}
							}	
						}
						
					});	
				}
			});
		}
	}
	
	
    /**
     * @return the number of drawable cells in width.
     */
    public int getDrawableXCellsNumber() {
    	return ((int)(this.getWidth() / CELL_OFFSET)) - 1;
    }
    
    /**
     * @return the number of drawable cells in height.
     */
    public int getDrawableYCellsNumber() {
    	return ((int)(this.getHeight() / CELL_OFFSET)) - 1;
    }
    
    /**
     * Reset the control
     */
    public void reset() {
    	this.cells = null;
    	this.clear();
    }
    
}
