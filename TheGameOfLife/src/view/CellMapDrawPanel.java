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

import controller.GameController;
import model.ConwayCell;

/**
 * A {@link JPanel} for the game of life matrix rendering.
 * It draws the live cells.
 *
 */
public class CellMapDrawPanel extends JComponent {
	
	/**
     * Auto-generated UID.
     */
    private static final long serialVersionUID = -6689261673710076779L;
    private BufferedImage bufferedImage  = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bufferedImage != null) {
        	g.drawImage(bufferedImage, 0, 0, null);
        }      
    }
    
    
    public void setCellsToPaint(Set<ConwayCell> cells) {
    	
        Graphics2D g2d = bufferedImage.createGraphics();
    	
    	g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
             
        
    	for (ConwayCell c : cells) {
    		//System.out.println("" + c.getPosition().x * 4 + "|" + c.getPosition().y * 4);
        	g2d.fillRect(c.getPosition().x * 4, c.getPosition().y * 4, 3, 3);
    	}
    	
    	g2d.dispose();
        this.repaint();
    	
    }
}