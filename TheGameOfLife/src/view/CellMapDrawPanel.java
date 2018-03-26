package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameController;

/**
 * A {@link JPanel} for the game of life matrix rendering.
 * It draws the live cells.
 *
 */
public class CellMapDrawPanel extends JPanel {
	
	/**
     * Auto-generated UID.
     */
    private static final long serialVersionUID = -6689261673710076779L;

    private static final double SCALE = 0.6;
    
    private final GameController controller;
    
    private int cellSize;
    private int[][] matrix;
    
    /**
     * Creates a new SimulatorPanel.
     * 
     */
    public CellMapDrawPanel(final GameController controller) {
        this.controller = controller;
        //initialize();
    }
    
    /**
     * Initializes the game panel.
     * Loads the resources according to the level's size and clears the collections
     * for the view-animations.
     *
    public final void initialize() {
        /*
         * Calculates the tile size according to the screen resolution
         * and the map's side (number of tiles in height/width).
         *
        this.cellSize = calculateCellSize(SCALE, this.controller.getCellMapDimension());
        
        /*
         * Sets the preferred size of the panel. 
         *
        this.setPreferredSize(new Dimension(this.controller.getMatrixSize() * this.cellSize, this.controller.getMatrixSize() * this.cellSize));
        final Container c = this.getTopLevelAncestor();
        if (c instanceof JFrame) {
            final JFrame f = (JFrame) c;
            f.pack();
        }
    }*/

    /**
     * Draws all graphical components.
     */
    @Override
    public void paintComponent(final Graphics g) {
        // Draws
    }

    /**
     * @return the size of a tile.
     */
    public int getCellSize() {
        return this.cellSize;
    }

    /**
     * Calculates the perfect size of a tile by desktop resolution.
     * 
     * @param scale
     *          the scale to apply to screen's dimension
     * @param nTiles
     *          the number of tiles in height/width to render in the frame
     * @return the size of a single tile
     */
    private static int calculateCellSize(final double scale, final int nCells) {
        if (scale < 0 || nCells < 0) {
            throw new IllegalArgumentException();
        }
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int height = (int) screen.getHeight();
        return Math.toIntExact(Math.round((height * scale) / nCells));
    }
}
