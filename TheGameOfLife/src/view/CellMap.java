package view;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CellMap extends Canvas {
	
	private static final int CELL_SIZE = 9;
	private static final int CELL_OFFSET = CELL_SIZE + 1;
	
	private CellMapViewer container;
	private boolean[][] cells;
	
	private int xPosition, yPosition;
	
	public CellMap() {
		
	}
	
	/**
	 * Set cells to draw on screen
	 * @param cells
	 * 		cells to draw
	 */
	public void setCellsToDraw (boolean[][] cells) {
		this.cells = cells;
		draw();
	}
	
	/**
	 * Set the container of the CellMap
	 * @param container
	 * 		container of the CellMap
	 */
	public void setContainer(CellMapViewer container) {
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
				gc.setFill(new Color(0.23, 0.23, 0.23, 1));
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
	public void updatePosition(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
		clear();
		draw();
	}

	
	/**
	 * Draw the cells considering current position
	 */
	private void draw() {
		//Getting current position of preview (of total map)
		final int containerXposition = this.xPosition;
		final int containerYposition = this.yPosition;
		
		//Getting how many squares I can draw in X and Y
		final int drawableX = getDrawableXCellsNumber();
		final int drawableY = getDrawableYCellsNumber();
		
		//X value of inferior limit of cells I'm able to draw (0 * 144, 1 * 144, 2 * 144)
		final int xOffset = containerXposition * drawableX;
		//X value of superior limit of cells I'm able to draw (1 * 144, 2 * 144, 3 * 144)
		final int xMaxOffset = (containerXposition + 1) * drawableX;
		
		//Y value of inferior limit of cells I'm able to draw (0 * 144, 1 * 144, 2 * 144)
		final int yOffset = containerYposition * drawableY;		
		//Y value of inferior limit of cells I'm able to draw (1 * 144, 2 * 144, 3 * 144)
		final int yMaxOffset = (containerYposition+1) * drawableY;
		
		
		//Draw (x must be from minOffset to maxOffset, same y)
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				//Create the graphics
				final GraphicsContext gc = getGraphicsContext2D();
				
				//For each cell I have
				if (cells != null) {
					
					/*for (int i = xOffset; i < xMaxOffset; i++) {
						for (int j = yOffset; j < yMaxOffset; j++) {
							
							if (cells[i][j]) {
		        				gc.setFill(Color.AQUA);
		            		} else {
		            			gc.setFill(new Color(0.23, 0.23, 0.23, 1));
		            		}       			   				
		        			
		        			gc.fillRect((i - xOffset) * CELL_OFFSET, (j - yOffset) * CELL_OFFSET, CELL_SIZE, CELL_SIZE);
						}
					}*/

				}
			}
		});			
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
