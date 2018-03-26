package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.GameController;

public class MenuPanel extends JPanel {

	/**
	 * Auto-generated UID.
	 */
	private static final long serialVersionUID = -3478859524085262735L;

	private static final int PANEL_WIDTH = 100;
	
	private final GameController controller;
	
	private final JButton start;
	private final JButton stop;
	
	//private final JTextField mapDimension;
	//private final JTextField previewDimension;
	
	private MenuObserver observer;
	
	public MenuPanel(final GameController controller) {
		this.controller = Objects.requireNonNull(controller);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(2, 0));
        
        //Creating buttons for START / STOP
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));
        
        this.start = factory.createButton("Start");
        this.stop = factory.createButton("Stop");
        
        buttonPanel.add(this.start);
        buttonPanel.add(this.stop);
        
        this.add(buttonPanel);
        
        //Creating more settings
	}
	
	@Override
    public final Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, 0);
    }
	
	/**
     * Set the observer of the MenuScene.
     * 
     * @param observer
     *          the observer to use
     */
    public void setObserver(final MenuObserver observer) {
        this.observer = observer;
    }

    /**
     * This interface indicates the operations that an observer
     * of the MenuScene can do.
     *
     */
    public interface MenuObserver {

        /**
         * Starts the game.
         */
        void start();
        
        /**
         * Show the scores of the player.
         */
        void stop();
    }
}
