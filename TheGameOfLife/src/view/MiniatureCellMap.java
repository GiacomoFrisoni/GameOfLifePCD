package view;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MiniatureCellMap extends Canvas {
	
	private static final Color BACKGROUND_COLOR = new Color(0.23, 0.23, 0.23, 1);
	private int cellSizeX, cellSizeY;
	
	
	public MiniatureCellMap() { }
	
	
	/**
	 * Set the limits of the small map preview
	 * @param x
	 * 		max x reachable (in width)
	 * @param y
	 * 		max y reachable (in height)
	 */
	public void setLimits(final int x, final int y) {
		this.cellSizeX = (int) (getWidth() / (x + 1));
		this.cellSizeY = (int) (getHeight() / (y + 1));
	}

	/**
	 * Draw a rectangle corresponding at current position on the map
	 * @param x
	 * 		x coordinate of the rectangle
	 * @param y
	 * 		y coordinate of the rectangle
	 */
	public void drawCurrentPosition(final int x, final int y) {	
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				final GraphicsContext gc = getGraphicsContext2D();
				gc.clearRect(0, 0, getWidth(), getHeight());
				gc.setFill(Color.ORANGE);
				gc.fillRect((x * cellSizeX), (y * cellSizeY), cellSizeX , cellSizeY);
			}
		});	
	}
	
	/**
	 * Clear the small map preview
	 */
	public void reset() {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				final GraphicsContext gc = getGraphicsContext2D();
				gc.setFill(BACKGROUND_COLOR);
				gc.clearRect(0, 0, getWidth(), getHeight());
			}
		});
	}
}
