package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.GameController;
import model.ConwayCell;

/**
 * A {@link JPanel} for the game of life matrix rendering.
 * It draws the live cells.
 *
 */
public class CellMapDrawPanel extends JComponent {
	
	private static final int CELL_SIZE = 9;
	private static final int CELL_OFFSET = CELL_SIZE + 1;
	
    private static final long serialVersionUID = -6689261673710076779L;
    private BufferedImage bufferedImage;
    
    private int actualWidth, actualHeight;
    
    private int mapXOffset = 0;
    private int mapYOffset = 0;
    

    public void initialize() {
    	actualWidth = CELL_OFFSET * ((int)(this.getWidth()/CELL_OFFSET));
    	actualHeight = CELL_OFFSET * ((int)(this.getHeight()/CELL_OFFSET));
    	bufferedImage = new BufferedImage(actualWidth, actualHeight, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void updateMapOffset(int x, int y) {
    	this.mapXOffset = x;
    	this.mapYOffset = y;
    }
    
    public int getDrawableXCellsNumber() {
    	return (int)(this.getWidth()/CELL_OFFSET);
    }
    
    public int getDrawableYCellsNumber() {
    	return (int)(this.getHeight()/CELL_OFFSET);
    }
     
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bufferedImage != null) {
        	g.drawImage(bufferedImage, 0, 0, null);
        }      
    }
    
    public void clear() {
    	Graphics2D g2d = bufferedImage.createGraphics();   	
    	g2d.setStroke(new BasicStroke(1));
    	g2d.fillRect(0, 0, actualWidth, actualHeight);
    	g2d.dispose();
        this.repaint();
    }
    
    public void setCellsToPaint(Set<ConwayCell> cells) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	Graphics2D g2d = bufferedImage.createGraphics();   	
            	g2d.setStroke(new BasicStroke(1));
            	
            	//Foreach cell I have
            	cells.forEach(c -> {
            		//If the cell is inside my current limit
            		if (checkXlimit(c) && checkYlimit(c)) {
            			
            			//BLACK = alive
            			if (c.isAlive()) {
                	        g2d.setColor(Color.BLACK);
                		}
            			
            			//WHITE = dead
                		else {
                	        g2d.setColor(Color.WHITE);
                		}
            			
            			//Draw
            			g2d.fillRect(c.getPosition().x * CELL_OFFSET, c.getPosition().y * CELL_OFFSET, CELL_SIZE, CELL_SIZE);
            		}
            	});
                    
            	g2d.dispose();
                repaint();
            }
        });
    	
        
    	
    }
    
    
    private boolean checkXlimit(ConwayCell c) {
    	int min = mapXOffset * getDrawableXCellsNumber();
    	int max = (mapXOffset + 1) * getDrawableXCellsNumber();
    			
    	if (c.getPosition().x >= min && c.getPosition().x <= max)
    		return true;
    	
    	return false;
    }
    
    private boolean checkYlimit(ConwayCell c) {
    	int min = mapYOffset * getDrawableYCellsNumber();
    	int max = (mapYOffset + 1) * getDrawableYCellsNumber();
    			
    	if (c.getPosition().y >= min && c.getPosition().y <= max)
    		return true;
    	
    	return false;
    }
    
    
}