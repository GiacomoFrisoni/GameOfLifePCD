package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MiniatureCellMap extends JComponent {

	private static final long serialVersionUID = -8086980349925960260L;
	
	private static final int CELL_SIZE = 13;
	private static final int CELL_OFFSET = CELL_SIZE + 1;
	
	private BufferedImage bufferedImage;  
	
	private int xSquaresNumber = 1;
	private int ySquaresNumber = 1;
	
	private int xCurrentSquare = 0;
	private int yCurrentSquare = 0;
	
	private int panelSize;
	
	public MiniatureCellMap(int panelSize) {
		this.panelSize = panelSize;
	}
	
	public void setSquareNumber(int x, int y) {
		this.xSquaresNumber = x;
		this.ySquaresNumber = y;
		bufferedImage = new BufferedImage(xSquaresNumber * CELL_OFFSET, ySquaresNumber * CELL_OFFSET, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setCurrentSquare(int x, int y) {
		this.xCurrentSquare = x;
		this.yCurrentSquare = y;
		draw();
	}
	
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (bufferedImage != null) {
        	g.drawImage(bufferedImage, 0, 0, null);
        }      
    }
    
    
    private void draw() {
    	//Just draw it!
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	Graphics2D g2d = bufferedImage.createGraphics();   	
            	g2d.setStroke(new BasicStroke(1));
            	
            	//Clear
		    	g2d.setColor(UIManager.getColor("Panel.background"));
		    	g2d.fillRect(0, 0, xSquaresNumber * CELL_OFFSET, ySquaresNumber * CELL_OFFSET);         	
            	
            	//Draw current position square
            	g2d.setColor(Color.ORANGE);
    			g2d.fillRect(xCurrentSquare * CELL_OFFSET, yCurrentSquare * CELL_OFFSET, CELL_SIZE, CELL_SIZE);
                    
            	g2d.dispose();
                repaint();
            }
        });
    }
    
    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(this.panelSize, this.panelSize);
    }
    
    @Override
    public Dimension getMaximumSize() {
    	return new Dimension(this.panelSize, this.panelSize);
    }
    
    @Override
    public Dimension getMinimumSize() {
    	return new Dimension(this.panelSize, this.panelSize);
    }
   

}
