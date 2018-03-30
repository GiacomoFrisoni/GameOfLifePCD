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
	
	private final GameController controller;
	
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
	
	private MenuObserver observer;
	
	public MenuPanel(final GameController controller) {
		this.controller = Objects.requireNonNull(controller);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(1, 1));
        
        
        //Creating additional panel for additional info
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        
        this.mapDimensionX = factory.createTextField(true, "1000");
        this.mapDimensionY = factory.createTextField(true, "1000");
        this.inputError = factory.createErrorLabel("Inserire valori numerici!");
        
        this.start = factory.createButton("Start");
        this.stop = factory.createButton("Stop");
        this.reset = factory.createButton("Reset");
        
        this.currentGeneration = factory.createTitleLabel("0");
        this.timeElapsed = factory.createTitleLabel("0");
        this.liveCells = factory.createTitleLabel("0");
        this.currentPosition = factory.createTitleLabel("0:0");
        this.previewDimension = factory.createLabel("-");
        

        panel.add(factory.createLabel("Map width (X)"));
        panel.add(this.mapDimensionX); 
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Map width (X)"));
        panel.add(this.mapDimensionY);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.inputError);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(this.start);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.stop);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.reset);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
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
        

        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(factory.createLabel("Preview squares"));
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
        	controller.reset();
        });
        
        this.add(panel);
        
        Dimension d = new Dimension(300, 25);
        this.start.setMaximumSize(d);
        this.stop.setMaximumSize(d);
        this.reset.setMaximumSize(d);
        this.inputError.setVisible(false);
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
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	currentGeneration.setText(text);
            }
        }); 	
    }
    
    public void setTimeElapsedInfo(String text) { 	
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	timeElapsed.setText(text);
            }
        }); 
    }
    
    public void setLiveCellsInfo(String text) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	liveCells.setText(text);
            }
        });
    }
    
    public void setCurrentPosition(String x, String y) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	currentPosition.setText(x + ";" + y);
            }
        });
    }
    
    
    public void setPreviewDimensionInfo(int x, int y) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	previewDimension.setText("W: " + x + " H: " + y);
            }
        });
    }
    
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
	
	public void setStarted() {
		this.start.setEnabled(false);
		this.stop.setEnabled(true);
		this.mapDimensionX.setEnabled(false);
		this.mapDimensionY.setEnabled(false);
	}

	public void setStopped() {
		this.start.setEnabled(true);
		this.stop.setEnabled(false);
	}
	
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
