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
	private final JButton reset;
	
	private final JTextField mapDimension;
	private final JTextField previewDimension;
	
	private final JLabel currentGeneration;
	private final JLabel timeElapsed;
	private final JLabel liveCells;
	private final JLabel currentPosition;
	
	private MenuObserver observer;
	
	public MenuPanel(final GameController controller) {
		this.controller = Objects.requireNonNull(controller);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(1, 1));
        
        
        //Creating additional panel for additional info
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        
        this.start = factory.createButton("Start");
        this.stop = factory.createButton("Stop");
        this.reset = factory.createButton("Reset");
        
        this.currentGeneration = factory.createLabel("0");
        this.timeElapsed = factory.createLabel("0");
        this.liveCells = factory.createLabel("0");
        this.currentPosition = factory.createLabel("1:1");
        
        this.mapDimension = factory.createTextField(true, "100");
        this.previewDimension = factory.createTextField(true, "500");
       
        panel.add(this.start);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.stop);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.reset);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(factory.createLabel("Current map position"));
        panel.add(currentPosition);
        panel.add(factory.createLabel("Current generation"));
        panel.add(currentGeneration);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Time elapsed"));
        panel.add(timeElapsed);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Live cells"));
        panel.add(liveCells);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(factory.createLabel("Map dimension (X & Y)"));
        panel.add(this.mapDimension);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Preview dimension (X & Y)"));
        panel.add(this.previewDimension);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        
        setCurrentGenerationInfo("0");
        setTimeElapsedInfo("0");
        
        this.start.addActionListener(e -> {
        	controller.start();
        });
        
        this.stop.addActionListener(e -> {
        	controller.stop();
        });
        
        this.reset.addActionListener(e -> {
        	controller.stop();
        });
        
        this.add(panel);
        
        Dimension d = new Dimension(300, 25);
        this.start.setMaximumSize(d);
        this.stop.setMaximumSize(d);
        this.reset.setMaximumSize(d);
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
    
	public String getMapDimension() {
		return this.mapDimension.getText();
	}

	public String getPreviewDimension() {
		return this.previewDimension.getText();
	}
	
	public void setStarted() {
		this.start.setEnabled(false);
		this.stop.setEnabled(true);
	}

	public void setStopped() {
		this.start.setEnabled(true);
		this.stop.setEnabled(false);
	}
}
