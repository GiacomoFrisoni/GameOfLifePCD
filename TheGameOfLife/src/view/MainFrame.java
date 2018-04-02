package view;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Optional;

import controller.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainFrame extends BorderPane implements GameOfLifeFrame {
	
	private static final int FRAME_SCALE = 70;
	private GameController controller;
	private Stage stage;
	
	
	@FXML
	private CellMapViewer cellMapViewer;
	
	@FXML
	private MenuPanel menuPanel;
	
	

	/**
	 * Creates a new frame for the game rendering.
	 */
	public MainFrame(Stage stage) {
        this.stage = stage;
        
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
        try {
            fxmlLoader.load();
        } catch (Exception exception) {
            System.out.println("FXML Loading Exception: MainFrame.fxml could not be loaded. Exception: " + exception.getMessage());
            exception.printStackTrace();
            System.exit(0);
        }  
	}
	
	

	@Override
	public void setObserver(GameController observer) {
		controller = observer;
	}


	private void initView() {
		final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	this.setWidth((gd.getDisplayMode().getWidth() * FRAME_SCALE) / 100);
    	this.setHeight((gd.getDisplayMode().getHeight() * FRAME_SCALE) / 100);
    	this.setMinWidth((gd.getDisplayMode().getWidth() * FRAME_SCALE) / 100);
    	this.setMinHeight((gd.getDisplayMode().getHeight() * FRAME_SCALE) / 100);
	}


	@Override
	public void showView() {
		initView();
		
		final Scene scene = new Scene(this);
		
		this.stage.setOnCloseRequest(e -> {
			this.closeView();
	        System.exit(0);
		});

		this.stage.setTitle("\"The Game Of Life - Giacomo Frisoni & Marcin Pabich\"");
		this.stage.setScene(scene);
		this.stage.show();	
		
    	this.cellMapViewer.init(this.controller, this);
    	this.menuPanel.init(controller);
    	this.menuPanel.setViewableCells(this.cellMapViewer.getDrawableXCellsNumber(), this.cellMapViewer.getDrawableYCellsNumber());
	}
	
	@Override
	public void closeView() {
		this.stage.close();
	}


	@Override
	public void drawCells(boolean[][] cells) {
		this.cellMapViewer.drawCells(cells);
	}

	@Override
	public void setGenerationInfo(long generation, long elapsedTime, long cellsAlive) {
		this.menuPanel.setGenerationInfo(generation, elapsedTime, cellsAlive);
	}

	@Override
	public void setStarted() {
		this.cellMapViewer.calculateMapLimits();
		this.menuPanel.setStarted();
		this.menuPanel.setLimits(this.cellMapViewer.getXLimit(), this.cellMapViewer.getYLimit());
	}

	@Override
	public void setStopped() {
		this.menuPanel.setStopped();
	}

	@Override
	public void reset() {
		this.menuPanel.reset();
		this.cellMapViewer.reset();	
	}

	@Override
	public Optional<Dimension> getMapDimension() {
		return this.menuPanel.getMapDimension();
	}
	
	@Override
	public void setProgress(ProgressType progressType, String title) {
		this.menuPanel.setProgress(progressType, title);
	}


	@Override
	public void updateProgress(double value) {
		this.menuPanel.updateProgress(value);
	}
	
	
	/**
	 * Get the menu panel of the main frame
	 * @return
	 * 		the menu panel of the main frame
	 */
	public MenuPanel getMenuPanel() {
		return this.menuPanel;
	}








}
