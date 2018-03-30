package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.GameController;

public class CellMapViewer extends JPanel {
	
	private static final long serialVersionUID = 4900611774904891447L;
	
	private static final String LEFT_SHIFT_INFO = "◀";
	private static final String UP_SHIFT_INFO = "▲";
	private static final String RIGHT_SHIFT_INFO = "▶";
	private static final String DOWN_SHIFT_INFO = "▼";
	
	private final GameController controller;
	private final GameOfLifeFrameImpl container;
	
	private final CellMap map;
	
	private final JButton left;
	private final JButton up;
	private final JButton right;
	private final JButton down;
	
	private int mapXcurrentPosition = 0;
	private int mapYcurrentPosition = 0;
	private int mapXLimit = 1;
	private int mapYLimit = 1;

	/**
	 * Constructs a cell map viewer.
	 * 
	 * @param controller
	 * 		the game of life controller
	 * @param container
	 * 		the frame container
	 */
	public CellMapViewer(final GameController controller, final GameOfLifeFrameImpl container) {
		// Takes references
		this.controller = controller;
		this.container = container;	
		
		// Prepares buttons
        final GUIFactory factory = new GUIFactory.Standard();
		this.left = factory.createButton(LEFT_SHIFT_INFO);
		this.up = factory.createButton(UP_SHIFT_INFO);
		this.right = factory.createButton(RIGHT_SHIFT_INFO);
		this.down = factory.createButton(DOWN_SHIFT_INFO);
		
		// Prepares the map
		this.map = new CellMap(this);
		
		// Sets the layout
		this.setLayout(new BorderLayout());
		this.add(this.map, BorderLayout.CENTER);
		this.add(this.left, BorderLayout.WEST);
		this.add(this.up, BorderLayout.NORTH);
		this.add(this.right, BorderLayout.EAST);
		this.add(this.down, BorderLayout.SOUTH);
		
		//Action listeners
		this.left.addActionListener(e -> {
			this.mapXcurrentPosition = mapXcurrentPosition <= 0 ? 0 : mapXcurrentPosition - 1;
			updateMenuState();
		});
		
		this.up.addActionListener(e -> {
			this.mapYcurrentPosition = mapYcurrentPosition <= 0 ? 0 : mapYcurrentPosition - 1;
			updateMenuState();
		});
		
		this.right.addActionListener(e -> {
			this.mapXcurrentPosition = mapXcurrentPosition >= mapXLimit ? mapXLimit : mapXcurrentPosition + 1;
			updateMenuState();
		});
		
		this.down.addActionListener(e -> {
			this.mapYcurrentPosition = mapYcurrentPosition >= mapYLimit ? mapYLimit : mapYcurrentPosition + 1;
			updateMenuState();
		});
	}
	
	private void updateMenuState() {
		container.getMenuPanel().setCurrentPosition(mapXcurrentPosition, mapYcurrentPosition);
		map.draw(true);
	}
	
	/**
	 * Resets the cell map viewer.
	 * Back to default position.
	 */
	public void reset() {
		this.mapXcurrentPosition = 0;
		this.mapYcurrentPosition = 0;
		calculateMapLimits();
	}
	
	/**
	 * Calculates the x and y limit for the cell map rendering.
	 */
	public void calculateMapLimits() {
		final Dimension mapDimension = controller.getCellMapDimension();
		this.mapXLimit = (mapDimension.width / map.getDrawableXCellsNumber());
		this.mapYLimit = (mapDimension.height / map.getDrawableYCellsNumber());
	}
	
	public Dimension getMapLimits() {
		return new Dimension(mapXLimit, mapYLimit);
	}
	
	/**
	 * @return the x-coordinate of the current region reference position.
	 */
	public int getXcurrentPosition() {
		return this.mapXcurrentPosition;
	}
	
	/**
	 * @return the y-coordinate of the current region reference position.
	 */
	public int getYcurrentPosition() {
		return this.mapYcurrentPosition;
	}
	
	/**
	 * @return the represented map component.
	 */
	public CellMap getCellMap() {
		return this.map;
	}
	
}
