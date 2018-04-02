package view;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MiniatureCellMap extends Canvas {
	
	private static final int CELL_SIZE = 9;
	private static final int CELL_OFFSET = CELL_SIZE + 1;
	
	
	public MiniatureCellMap() {
		
	}
	

	public void drawCurrentPosition(int x, int y) {
		
		
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				GraphicsContext gc = getGraphicsContext2D();
				gc.clearRect(0, 0, getWidth(), getHeight());
				gc.setFill(Color.ORANGE);
				gc.fillRect(x * CELL_OFFSET , y * CELL_OFFSET, CELL_SIZE, CELL_SIZE);
			}
		});	
		

	}
	
	public void reset() {
			
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				GraphicsContext gc = getGraphicsContext2D();
				gc.clearRect(0, 0, getWidth(), getHeight());
			}
		});	
		
	}
}
