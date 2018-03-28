package view;

import java.util.Set;

import controller.GameController;
import model.ConwayCell;

public interface GameOfLifeFrame {
	
	/**
     * Sets an observer of the game frame.
     * 
     * @param observer
     *          the controller to attach
     */
    void setObserver(GameController observer);
    
    /**
     * This method is called before the UI is used.
     * It creates and initializes the view.
     */
    void initView();
    
    /**
     * Shows the user interface on the screen.
     */
    void showView();

    /**
     * Updates the view and repaints the cell map panel.
     */
    void drawCells(Set<ConwayCell> cells);
    
    /**
     * Closes the game frame.
     */
    void closeView();
    
}
