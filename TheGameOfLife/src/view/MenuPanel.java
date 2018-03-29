package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.GameController;

public class MenuPanel extends JPanel {

	/**
	 * Auto-generated UID.
	 */
	private static final long serialVersionUID = -3478859524085262735L;

	private static final int PANEL_WIDTH = 250;
	
	private final GameController controller;
	
	private final JButton start;
	private final JButton stop;
	
	//private final JTextField mapDimension;
	//private final JTextField previewDimension;
	
	private final JLabel currentGeneration;
	private final JLabel timeElapsed;
	private final JLabel liveCells;
	
	private MenuObserver observer;
	
	public MenuPanel(final GameController controller) {
		this.controller = Objects.requireNonNull(controller);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(1, 1));
        
        //Creating buttons for START / STOP
        /*final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2, 5, 5));
        
        this.start = factory.createButton("Start");
        this.stop = factory.createButton("Stop");
        
        buttonPanel.add(this.start);
        buttonPanel.add(this.stop);*/
        
        
        //Creating additional panel for additional info
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        
        this.start = factory.createButton("Start");
        this.stop = factory.createButton("Stop");
        
        this.currentGeneration = factory.createLabel("0");
        this.timeElapsed = factory.createLabel("0");
        this.liveCells = factory.createLabel("0");
        
        panel.add(this.start);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.stop);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(new JLabel("Current generation"));
        panel.add(currentGeneration);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(new JLabel("Time elapsed"));
        panel.add(timeElapsed);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(new JLabel("Live cells"));
        panel.add(liveCells);
        
        setCurrentGenerationInfo("0");
        setTimeElapsedInfo("0");
        
        this.start.addActionListener(e -> {
        	controller.start();
        });
        
        this.stop.addActionListener(e -> {
        	controller.stop();
        });
        
        //this.add(buttonPanel);
        this.add(panel);
        
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
    
    public void setCurrentGenerationInfo(String text) {
    	this.currentGeneration.setText(text);
    }
    
    public void setTimeElapsedInfo(String text) {
    	this.timeElapsed.setText(text);
    }
    
    public void setLiveCellsInfo(String text) {
    	this.liveCells.setText(text);
    }
}
