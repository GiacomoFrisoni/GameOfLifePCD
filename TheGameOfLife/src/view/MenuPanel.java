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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import controller.GameController;

public class MenuPanel extends JPanel {

	/**
	 * Auto-generated UID.
	 */
	private static final long serialVersionUID = -3478859524085262735L;

	private static final int PANEL_WIDTH = 250;
	private static final int DEFAULT_MAP_WIDTH = 500;
	private static final int DEFAULT_MAP_HEIGHT = 500;
	
	private final GameController controller;
	private final GameOfLifeFrameImpl container;
	
	private final JButton start;
	private final JButton stop;
	private final JButton reset;
	
	private final JTextField mapDimensionX;
	private final JTextField mapDimensionY;
	private final JLabel inputError;
	
	private final JLabel currentGeneration;
	private final JLabel timeElapsed;
	private final JLabel liveCells;
	private final JLabel currentPosition;
	
	private final JLabel previewDimension;	
	
	public MenuPanel(final GameController controller, final GameOfLifeFrameImpl container) {
		this.controller = Objects.requireNonNull(controller);
		this.container = Objects.requireNonNull(container);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(1, 1));
        
        //Creating additional panel for additional info
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        
        //TextField for specifying map dimensions
        this.mapDimensionX = factory.createTextField(true, "" + DEFAULT_MAP_WIDTH);
        this.mapDimensionY = factory.createTextField(true, "" + DEFAULT_MAP_HEIGHT);
        this.inputError = factory.createErrorLabel("Inserire valori numerici!");
        this.inputError.setVisible(false);
        
        //Buttons to interact with game
        this.start = factory.createPanelButton("Start");
        this.stop = factory.createPanelButton("Stop");
        this.reset = factory.createPanelButton("Reset");
        
        //Some info about what's happening
        this.currentGeneration = factory.createTitleLabel("0");
        this.timeElapsed = factory.createTitleLabel("0");
        this.liveCells = factory.createTitleLabel("0");
        this.currentPosition = factory.createTitleLabel("0:0");
        this.previewDimension = factory.createLabel("-");
        

        //Putting the area of input
        panel.add(factory.createLabel("Map width (X)"));
        panel.add(this.mapDimensionX); 
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Map width (X)"));
        panel.add(this.mapDimensionY);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.inputError);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        //Putting the area of interact
        panel.add(this.start);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.stop);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.reset);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        //Putting the are of info
        panel.add(factory.createLabel("Current map position"));
        panel.add(currentPosition);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(factory.createLabel("Current generation"));
        panel.add(currentGeneration);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Time elapsed"));
        panel.add(timeElapsed);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Live cells"));
        panel.add(liveCells);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));           
        panel.add(factory.createLabel("Preview squares"));
        panel.add(this.previewDimension);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
              
        
        //Action listeners
        this.start.addActionListener(e -> {
        	controller.start();
        });
        
        this.stop.addActionListener(e -> {
        	controller.stop();
        });
        
        this.reset.addActionListener(e -> {
        	controller.reset();
        });
       
        
        //Add all things to the panel
        this.add(panel);
	}
	
	@Override
    public final Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, 0);
    }
	
    
	/**
	 * Set text to inform about current generation
	 * @param text - representing the number of generation
	 */
    public void setCurrentGenerationInfo(String text) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	currentGeneration.setText(text);
            }
        }); 	
    }
    
    /**
     * Set text to inform about time elapsed during last computation
     * @param text - representing the time elapsed during last computation
     */
    public void setTimeElapsedInfo(String text) { 	
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	timeElapsed.setText(text);
            }
        }); 
    }
    
    /**
     * Set text to inform about how many cells are still alive
     * @param text - representing how many cells are still alive
     */
    public void setLiveCellsInfo(String text) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	liveCells.setText(text);
            }
        });
    }
    
    /**
     * Set text to inform about what's the current position on the general map
     * @param x - width (x) position of the map
     * @param y - height (y) position of the map
     */
    public void setCurrentPosition(String x, String y) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	currentPosition.setText(x + ";" + y);
            }
        });
    }
    
    /**
     * Set text to inform how big is preview area
     * @param x - squares in width
     * @param y - squares in height
     */
    public void setPreviewDimensionInfo(int x, int y) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	previewDimension.setText("W: " + x + " H: " + y);
            }
        });
    }
    
    /**
     * Takes the user input of how big should be the map
     * @return DIMENSION if input was correct, NULL otherwise
     */
	public Dimension getMapDimension() {
		try {
			this.inputError.setVisible(false);
			int x = Integer.parseInt(this.mapDimensionX.getText());
			int y = Integer.parseInt(this.mapDimensionY.getText());
			
			return new Dimension(x,y);
		}
		catch (Exception e) {
			this.inputError.setVisible(true);
			return null;
		}
	}
	
	/**
	 * Set the current state of the view as STARTED
	 */
	public void setStarted() {
		this.start.setEnabled(false);
		this.stop.setEnabled(true);
		this.mapDimensionX.setEnabled(false);
		this.mapDimensionY.setEnabled(false);
	}

	/**
	 * Set the current state of the view as STOPPED
	 */
	public void setStopped() {
		this.start.setEnabled(true);
		this.stop.setEnabled(false);
	}
	
	/**
	 * RESET all the view
	 */
	public void reset() {
		this.start.setEnabled(true);
		this.stop.setEnabled(false);
		this.mapDimensionX.setEnabled(true);
		this.mapDimensionY.setEnabled(true);
		
		setCurrentGenerationInfo("-");
		setTimeElapsedInfo("-");
		setLiveCellsInfo("-");
		setCurrentPosition("0", "0");
	}
}
