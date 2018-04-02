package view;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MiniatureCellMap extends Canvas {
	
	private int cellSizeX, cellSizeY;
	
	
	public MiniatureCellMap() { }
	
	
	public void setLimits(final int x, final int y) {
		this.cellSizeX = (int) (getWidth() / (x + 1));
		this.cellSizeY = (int) (getHeight() / (y + 1));
	}

	public void drawCurrentPosition(final int x, final int y) {	
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				final GraphicsContext gc = getGraphicsContext2D();
				gc.clearRect(0, 0, getWidth(), getHeight());
				gc.setFill(Color.ORANGE);
				gc.fillRect((x * cellSizeX) + 1 , (y * cellSizeY) + 1, cellSizeX , cellSizeY);
			}
		});	
	}
	
	public void reset() {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				final GraphicsContext gc = getGraphicsContext2D();
				gc.clearRect(0, 0, getWidth(), getHeight());
			}
		});
	}
}
