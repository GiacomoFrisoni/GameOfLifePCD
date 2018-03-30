package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.ConwayCell;

/**
 * A {@link JPanel} for the game of life matrix rendering.
 * It draws the live cells.
 *
 */
public class CellMap extends JComponent {
	
    private static final long serialVersionUID = -6689261673710076779L;
    
	private static final int CELL_SIZE = 9;
	private static final int CELL_OFFSET = CELL_SIZE + 1;
	
	private CellMapViewer container;
	
    private BufferedImage bufferedImage;  
    private int actualWidth, actualHeight;   
    private Set<ConwayCell> cells;
    
    
    /**
     * Constructs a new cell map component.
     * 
     * @param container
     * 		the cell map viewer container
     */
    public CellMap(final CellMapViewer container) {
    	this.container = container;
    }  

    /**
     * Initializes the component.
     */
    public void initialize() {
    	actualWidth = CELL_OFFSET * ((int)(this.getWidth()/CELL_OFFSET));
    	actualHeight = CELL_OFFSET * ((int)(this.getHeight()/CELL_OFFSET));
    	bufferedImage = new BufferedImage(actualWidth, actualHeight, BufferedImage.TYPE_INT_ARGB);
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bufferedImage != null) {
        	g.drawImage(bufferedImage, 0, 0, null);
        }      
    }
    
    /**
     * Clears the cell map rendering.
     */
    public void clear() {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
		    	Graphics2D g2d = bufferedImage.createGraphics();   	
		    	g2d.setStroke(new BasicStroke(1));
		    	g2d.setColor(UIManager.getColor("Panel.background"));
		    	g2d.fillRect(0, 0, actualWidth, actualHeight);
		    	g2d.dispose();
		        repaint();
            }
    	});
    }
    
    /**
     * Paints a cell.
     * 
     * @param cellsToDraw
     * 		the cells to draw
     */
    public void setCellsToPaint(final Set<ConwayCell> cellsToDraw) {
    	this.cells = cellsToDraw;  
    	draw(false);
    }
    
    /**
     * Draw the matrix.
     * 
     * @param clear
     * 		true if the matrix need to be cleared (preview shifts), false otherwise.
     */
    public void draw(final boolean clear) {
    	if (clear) {
    		clear();
    	}
    	
    	// If i have something to draw
    	if (cells != null) {
	    	if (!cells.isEmpty()) {
	    		// Just draw it!
		    	SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	final Graphics2D g2d = bufferedImage.createGraphics();   	
		            	g2d.setStroke(new BasicStroke(1));
		            	
		            	// For each cell I have
		            	cells.forEach(c -> {
		            		//If the cell is inside my current limit
		            		if (checkXlimit(c) && checkYlimit(c)) {
		            			if (c.isAlive()) {
		                	        g2d.setColor(Color.BLACK);
		                		} else {
		                	        g2d.setColor(Color.WHITE);
		                		}
		            			
		            			// Draw
		            			g2d.fillRect(
		            					(c.getPosition().x - (container.getXcurrentPosition() * getDrawableXCellsNumber())) * CELL_OFFSET, 
		            					(c.getPosition().y - (container.getYcurrentPosition() * getDrawableYCellsNumber())) * CELL_OFFSET, 
		            					CELL_SIZE, 
		            					CELL_SIZE);
		            		}
		            	});
		                    
		            	g2d.dispose();
		                repaint();
		            }
		        });
	    	}
    	}
    }
    
    /**
     * @return the number of drawable cells in width.
     */
    public int getDrawableXCellsNumber() {
    	return (int)(this.getWidth() / CELL_OFFSET);
    }
    
    /**
     * @return the number of drawable cells in height.
     */
    public int getDrawableYCellsNumber() {
    	return (int)(this.getHeight() / CELL_OFFSET);
    }
    
    /*
     * Checks if the point stay in current limit (X)
     */
    private boolean checkXlimit(final ConwayCell c) {
    	final int min = this.container.getXcurrentPosition() * getDrawableXCellsNumber();
    	final int max = (this.container.getXcurrentPosition() + 1) * getDrawableXCellsNumber();
    		
    	if (c.getPosition().x >= min && c.getPosition().x <= max) {
    		return true;
    	}
    	
    	return false;
    }
    
    /*
     * Checks if the point stay in current limit (Y)
     */
    private boolean checkYlimit(final ConwayCell c) {
    	final int min = this.container.getYcurrentPosition() * getDrawableYCellsNumber();
    	final int max = (this.container.getYcurrentPosition() + 1) * getDrawableYCellsNumber();
    		
    	if (c.getPosition().y >= min && c.getPosition().y <= max)
    		return true;
    	
    	return false;
    }
    
    
}