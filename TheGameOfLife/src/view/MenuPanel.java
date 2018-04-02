package view;

import java.awt.Dimension;
import java.util.Optional;

import controller.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MenuPanel extends VBox {

	@FXML
	private TextField mapWidth, mapHeight;
	
	@FXML
	private MiniatureCellMap miniatureCellMap;
	
	@FXML
	private Label currentPosition, viewableCells, generation, elapsedTime, cellsAlive, errorLabel, loadingLabel;
	
	@FXML
	private Button start, stop, reset;
	
	@FXML
	private Pane cellMapContainer;
	
	@FXML
	private VBox loadingStatus;
	
	@FXML
	private ProgressBar progress;
	
	private GameController controller;

	/**
	 * Create a new menu panel
	 */
	public MenuPanel() {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuPanel.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		
        try {
            fxmlLoader.load();
    		this.reset();
        } catch (Exception exception) {
        	MessageViewer.showException(
        			"FXML Loading Exception", 
        			"MenuPanel.fxml could not be loaded.", 
        			exception.getMessage());
        	
            System.exit(0);
        }  
	}
	
	/**
	 * Initialize the menu panel
	 * @param controller
	 * 		controller of the main frame
	 */
	public void init(GameController controller) {
		this.controller = controller;
		setProgress(ProgressType.IDLE, "Idle");
		
		start.setOnMouseClicked(e -> {
			setProgress(ProgressType.INDETERMINATE, "Starting...");
			this.controller.start();
		});
		
		stop.setOnMouseClicked(e -> {
			setProgress(ProgressType.INDETERMINATE, "Stopping...");
			this.controller.stop();
		});
		
		reset.setOnMouseClicked(e -> {
			setProgress(ProgressType.INDETERMINATE, "Resetting...");
			this.controller.reset();
		});
	}
	
	/**
	 * Get the map dimension inserted by user
	 * @return
	 * 		dimension of the map inserted by user
	 */
	public Optional<Dimension> getMapDimension() {
		this.errorLabel.setVisible(false);
		final String width = this.mapWidth.getText();
		final String height = this.mapHeight.getText();
		
		if (width != null && height != null) {
			if (!width.isEmpty() && !height.isEmpty()) {
				if (width.chars().allMatch(Character::isDigit) && height.chars().allMatch(Character::isDigit)) {
					if (Integer.parseInt(width) > 0 && Integer.parseInt(height) > 0) {
						return Optional.of(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
					}
				}
			} 
		}
			
		this.errorLabel.setVisible(true);
		return Optional.empty();	
	}
	
	
	/**
	 * Set the info about the current position of the preview
	 * @param x
	 * 		x position of the preview
	 * @param y
	 * 		y position of the preview
	 */
	public void setCurrentPosition(int x, int y) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				currentPosition.setText(x + ", " + y);	
				miniatureCellMap.drawCurrentPosition(x,y);
			}
		});		
	}
	
	public void setLimits(int x, int y) {
		this.miniatureCellMap.setLimits(x, y);
	}
	
	/**
	 * Set the info about how many cells are viewable
	 * @param x
	 * 		cells viewable in width
	 * @param y
	 * 		cells viewable in height
	 */
	public void setViewableCells(int x, int y) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				viewableCells.setText("W: " + x + ", H: " + y);		
			}
		});			
	}
	
	/**
	 * Set info about the current generation
	 * @param gen
	 * 		number of the generation
	 * @param time
	 * 		time elapsed to calculate this generation
	 * @param cells
	 * 		currently alive cells
	 */
	public void setGenerationInfo(long gen, long time, long cells) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				generation.setText("" + gen);
				elapsedTime.setText("" + time + "ms");
				cellsAlive.setText("" + cells);	
			}
		});	
	}
	
	/**
	 * Invoked when computation started
	 */
	public void setStarted() {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				start.setDisable(true);
				reset.setDisable(true);
				stop.setDisable(false);
				
				mapWidth.setDisable(true);
				mapHeight.setDisable(true);
			}
		});	
	}
	
	/**
	 * Invoked when computation stopped
	 */
	public void setStopped() {	
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				start.setDisable(false);
				reset.setDisable(false);
				stop.setDisable(true);
			}
		});		
	}
	
	/**
	 * Reset all the view and sub components
	 */
	public void reset() {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				start.setDisable(false);
				reset.setDisable(true);
				stop.setDisable(true);
				errorLabel.setVisible(false);
				
				mapWidth.setDisable(false);
				mapHeight.setDisable(false);
				miniatureCellMap.reset();		
			}
		});	
		
		setCurrentPosition(0, 0);
		setGenerationInfo(0, 0, 0);
	}

	
	/**
	 * Set the progress
	 * @param type
	 * 		type of the progress (determinate or not)
	 * @param title
	 * 		title of the label of progress
	 */
	public void setProgress(ProgressType type, String title) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				loadingLabel.setText(title);	
				progress.setProgress(type.equals(ProgressType.INDETERMINATE) ? ProgressBar.INDETERMINATE_PROGRESS : 0);
			}
		});	
	}
	
	/**
	 * Update the value of the progress if it's determinated
	 * @param value
	 * 		value of the progress between 0 and 1 (example, 50% is 0.5)
	 */
	public void updateProgress(double value) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				progress.setProgress(value);
			}
		});	
	}
	

}
