package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.GameController;

public class CellMapViewer extends JPanel {
	
	private static final long serialVersionUID = 4900611774904891447L;
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

	public CellMapViewer(final GameController controller, final GameOfLifeFrameImpl container) {
		//Take reference
		this.controller = controller;
		this.container = container;	
		
		//Prepare buttons
        final GUIFactory factory = new GUIFactory.Standard();
		this.left = factory.createButton("<");
		this.up = factory.createButton("^");
		this.right = factory.createButton(">");
		this.down = factory.createButton("v");
		
		//Prepare the map
		this.map = new CellMap(this);
			
		// Sets the layout
		this.setLayout(new BorderLayout());
		this.add(this.map, BorderLayout.CENTER);
		this.add(this.left, BorderLayout.WEST);
		this.add(this.up, BorderLayout.NORTH);
		this.add(this.right, BorderLayout.EAST);
		this.add(this.down, BorderLayout.SOUTH);
		
		//Calculate offsets and limits
		//calculateMapLimits();
		
		//Action listeners
		this.left.addActionListener(e -> {
			mapXcurrentPosition = mapXcurrentPosition <= 0 ? 0 : mapXcurrentPosition - 1;
			updateMenuState();
		});
		
		this.up.addActionListener(e -> {
			mapYcurrentPosition = mapYcurrentPosition <= 0 ? 0 : mapYcurrentPosition - 1;
			updateMenuState();
		});
		
		this.right.addActionListener(e -> {
			mapXcurrentPosition = mapXcurrentPosition >= mapXLimit ? mapXLimit : mapXcurrentPosition + 1;
			updateMenuState();
		});
		
		this.down.addActionListener(e -> {
			mapYcurrentPosition = mapYcurrentPosition >= mapYLimit ? mapYLimit : mapYcurrentPosition + 1;
			updateMenuState();
		});
		
	}
	
	
	private void updateMenuState() {
		container.getMenuPanel().setCurrentPosition("" + mapXcurrentPosition, "" + mapYcurrentPosition);
		map.draw(true);
	}
	
	public void reset() {
		mapXcurrentPosition = 0;
		mapYcurrentPosition = 0;
		calculateMapLimits();
	}
	
	public void calculateMapLimits() {
		mapXLimit = (controller.getCellMapDimension().width / map.getDrawableXCellsNumber());
		mapYLimit = (controller.getCellMapDimension().height / map.getDrawableYCellsNumber());
	}
	
	public int getXcurrentPosition() {
		return this.mapXcurrentPosition;
	}
	
	public int getYcurrentPosition() {
		return this.mapYcurrentPosition;
	}
	
	public CellMap getCellMap() {
		return this.map;
	}
	
}
