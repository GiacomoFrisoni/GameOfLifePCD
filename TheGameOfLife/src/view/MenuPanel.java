package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Objects;
import java.util.Optional;

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
	private static final int PANEL_INSETS = 10;
	
	private static final String START = "Start";
	private static final String STOP = "Stop";
	private static final String RESET = "Reset";
	
	private static final String MAP_WIDTH_INFO = "Map width (X)";
	private static final String MAP_HEIGHT_INFO = "Map height (Y)";
	private static final String MAP_POSITION_INFO = "Current map position";
	private static final String GENERATION_INFO = "Current generation";
	private static final String TIME_ELAPSED_INFO = "Time elapsed";
	private static final String ALIVE_CELLS_INFO = "Alive cells";
	private static final String PREVIEW_SQUARES_INFO = "Preview squares";
	private static final String DEFAULT_POSITION = "W:0|H:0";
	private static final String DEFAULT_PREVIEW = "-";
	private static final String DEFAULT_NUMBER = "0";
	private static final int DEFAULT_VALUE = 0;
	private static final String INPUT_ERROR = "Inserire valori numerici!";
	
	
	private final GameController controller;
	
	private final JButton start;
	private final JButton stop;
	private final JButton reset;
	
	private final JTextField mapDimensionX;
	private final JTextField mapDimensionY;
	private final JLabel inputError;
	
	private final JLabel currentGeneration;
	private final JLabel timeElapsed;
	private final JLabel aliveCells;
	private final JLabel currentPosition;
	private final MiniatureCellMap miniatureMap;
	
	private final JLabel previewDimension;
	
	
	/**
	 * Creates a new menu panel.
	 * 
	 * @param controller
	 * 		the game of life controller
	 */
	public MenuPanel(final GameController controller) {
		this.controller = Objects.requireNonNull(controller);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(1, 1));
        
        //Creating additional panel for additional info
        JPanel panel = factory.createPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(PANEL_INSETS, PANEL_INSETS, PANEL_INSETS, PANEL_INSETS)));
        
        //TextField for specifying map dimensions
        this.mapDimensionX = factory.createTextField(true, "" + DEFAULT_MAP_WIDTH);
        this.mapDimensionY = factory.createTextField(true, "" + DEFAULT_MAP_HEIGHT);
        this.inputError = factory.createErrorLabel(INPUT_ERROR);
        this.inputError.setVisible(false);
        
        //Buttons to interact with game
        this.start = factory.createPanelButton(START);
        this.stop = factory.createPanelButton(STOP);
        this.reset = factory.createPanelButton(RESET);
        
        //Some info about what's happening
        this.currentGeneration = factory.createTitleLabel(DEFAULT_NUMBER);
        this.timeElapsed = factory.createTitleLabel(DEFAULT_NUMBER);
        this.aliveCells = factory.createTitleLabel(DEFAULT_NUMBER);
        this.currentPosition = factory.createTitleLabel(DEFAULT_POSITION);
        this.previewDimension = factory.createTitleLabel(DEFAULT_PREVIEW);
        
        this.miniatureMap = new MiniatureCellMap(PANEL_WIDTH);
        
        //Putting the area of input
        panel.add(factory.createLabel(MAP_WIDTH_INFO));
        panel.add(this.mapDimensionX); 
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel(MAP_HEIGHT_INFO));
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
        panel.add(this.miniatureMap);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel(MAP_POSITION_INFO));
        panel.add(currentPosition);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(factory.createLabel(GENERATION_INFO));
        panel.add(currentGeneration);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel(TIME_ELAPSED_INFO));
        panel.add(timeElapsed);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel(ALIVE_CELLS_INFO));
        panel.add(aliveCells);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));           
        panel.add(factory.createLabel(PREVIEW_SQUARES_INFO));
        panel.add(this.previewDimension);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
              
        
        //Action listeners
        this.start.addActionListener(e -> {
        	this.controller.start();
        });
        
        this.stop.addActionListener(e -> {
        	this.controller.stop();
        });
        
        this.reset.addActionListener(e -> {
        	this.controller.reset();
        });
       
        
        //Add all things to the panel
        this.add(panel);
	}
	
	@Override
    public final Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, 0);
    }
	
	public void setMiniatureMapSize(Dimension d) {
		this.miniatureMap.setSquareNumber(d.width, d.height);
	}
	
    
	/**
	 * Sets text to inform about current generation.
	 * @param text
	 * 		the number of generation
	 */
    public void setCurrentGenerationInfo(final String text) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	currentGeneration.setText(text);
            }
        }); 	
    }
    
    /**
     * Sets text to inform about time elapsed during last computation.
     * @param text
     * 		the time elapsed during last computation
     */
    public void setTimeElapsedInfo(final String text) { 	
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	timeElapsed.setText(text);
            }
        }); 
    }
    
    /**
     * Sets text to inform about how many cells are still alive.
     * @param text
     * 		how many cells are still alive
     */
    public void setLiveCellsInfo(final String text) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	aliveCells.setText(text);
            }
        });
    }
    
    /**
     * Sets text to inform about what's the current position on the general map.
     * @param x
     * 		width (x) position of the map
     * @param y
     * 		height (y) position of the map
     */
    public void setCurrentPosition(int x, int y) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentPosition.setText("W: " + x + " | H: " + y);
                miniatureMap.setCurrentSquare(x, y);
            }
        });
    }
    
    /**
     * Sets text to inform how big is preview area.
     * @param x
     * 		squares in width
     * @param y
     * 		squares in height
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
     * Takes the user input of how big should be the map.
     * @return {@link Dimension} if input was correct, null otherwise
     */
	public Optional<Dimension> getMapDimension() {
		this.inputError.setVisible(false);
		
		if (this.mapDimensionX.getText().chars().allMatch(Character::isDigit) &&
				this.mapDimensionY.getText().chars().allMatch(Character::isDigit)) {
			return Optional.of(new Dimension(Integer.parseInt(this.mapDimensionX.getText()), Integer.parseInt(this.mapDimensionY.getText())));
		} else {
			this.inputError.setVisible(true);
			return Optional.empty();
		}

	}
	
	/**
	 * Sets the current state of the view as started.
	 */
	public void setStarted() {
		this.start.setEnabled(false);
		this.stop.setEnabled(true);
		this.mapDimensionX.setEnabled(false);
		this.mapDimensionY.setEnabled(false);
	}

	/**
	 * Sets the current state of the view as stopped.
	 */
	public void setStopped() {
		this.start.setEnabled(true);
		this.stop.setEnabled(false);
	}
	
	/**
	 * Resets all the view.
	 */
	public void reset() {
		this.start.setEnabled(true);
		this.stop.setEnabled(false);
		this.mapDimensionX.setEnabled(true);
		this.mapDimensionY.setEnabled(true);
		
		setCurrentGenerationInfo(DEFAULT_PREVIEW);
		setTimeElapsedInfo(DEFAULT_PREVIEW);
		setLiveCellsInfo(DEFAULT_PREVIEW);
		setCurrentPosition(DEFAULT_VALUE, DEFAULT_VALUE);
	}
}
