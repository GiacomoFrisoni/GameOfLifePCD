package view;

import controller.GameController;

public interface MainFrame {
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
     * @return the size of a tile.
     */
    int getTileSize();
    
    /**
     * Shows the user interface on the screen.
     */
    void showView();

    /**
     * Updates the view and repaints the game panel.
     */
    void update();
    
    /**
     * Closes the game frame.
     */
    void closeView();
}
