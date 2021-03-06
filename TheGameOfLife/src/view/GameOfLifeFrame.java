package view;

import java.awt.Dimension;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import controller.GameController;

public interface GameOfLifeFrame {
	
	/**
     * Sets an observer of the game frame.
     * 
     * @param observer
     * 		the controller to attach
     */
    void setObserver(GameController observer);
    
    /**
     * Shows the user interface on the screen.
     */
    void showView();
    
    /**
     * Closes the game frame.
     */
    void closeView();

    /**
     * Updates the view and repaints the cell map panel.
     */
    void drawCells(boolean[][] cells, CountDownLatch latch);
    
    /**
     * Set info about current generation
     * @param generation
     * 		current generation number
     * @param elapsedTime
     * 		time elapsed to calculate current generation
     * @param cellsAlive
     * 		cells that are alive in current generation
     */
    void setGenerationInfo(long generation, long elapsedTime, long cellsAlive);
    
    /**
     * Set the status as "started"
     */
    void setStarted();
    
    /**
     * Set the status as "stopped"
     */
    void setStopped();
    
    /**
     * Reset the view to initial state
     */
    void reset();
    
    /**
     * Set the progress type and title
     * @param progressType
     * 		progress type (indeterminate or determinate)
     * @param title
     * 		title of the progress (starting..., stopping..., started, stopped etc...)
     */
    void setProgress(ProgressType progressType, String title);
    
    /**
     * Update the progress if setted as determinate
     * @param value
     * 		% of progress between 0 and 1 (example, 50% is 0.5)
     */
    void updateProgress(double value);
    
    /**
     * Get user input: the dimension of the map 
     * @return
     * 	 	the dimension of the map
     */
    Optional<Dimension> getMapDimension();
    
    /**
     * Get user input: the minimum refresh time
     * @return
     * 		the minimum refresh time
     */
    int getMinRefreshTime();
    
    /**
     * Show an error message
     * @param header
     * 		header of the message (main cause of the error, short!)
     * @param message
     * 		user-friendly message to show 
     * @param exception
     * 		string generated by the error itself
     */
    void showErrorAlert(String header, String message, String exception);
    
    /**
     * Show a message
     * @param header
     * 		header of the message (short!)
     * @param message
     * 		user-friendly message to show 
     */
    void showAlert(String header, String message);
     
}
