package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.GameController;

public class CellMapViewer extends JPanel {
	
	private static final long serialVersionUID = 4900611774904891447L;
	private final CellMapDrawPanel map;
	private final MenuPanel menuPanel;
	
	private final JButton left;
	private final JButton up;
	private final JButton right;
	private final JButton down;
	
	private int xPosition = 0;
	private int yPosition = 0;
	private int mapXLimit = 1;
	private int mapYLimit = 1;

	public CellMapViewer(CellMapDrawPanel map, MenuPanel menuPanel) {
		this.map = map;
		this.menuPanel = menuPanel;
		
        final GUIFactory factory = new GUIFactory.Standard();
		this.left = factory.createButton("<");
		this.up = factory.createButton("^");
		this.right = factory.createButton(">");
		this.down = factory.createButton("v");
			
		// Sets the layout
		this.setLayout(new BorderLayout());
		this.add(this.map, BorderLayout.CENTER);
		this.add(this.left, BorderLayout.WEST);
		this.add(this.up, BorderLayout.NORTH);
		this.add(this.right, BorderLayout.EAST);
		this.add(this.down, BorderLayout.SOUTH);
		
		calculateMapLimits();
		
		this.left.addActionListener(e -> {
			xPosition = xPosition <= 0 ? 0 : xPosition - 1;
			updateMenuState();
		});
		
		this.up.addActionListener(e -> {
			yPosition = yPosition <= 0 ? 0 : yPosition - 1;
			updateMenuState();
		});
		
		this.right.addActionListener(e -> {
			xPosition = xPosition >= mapXLimit ? mapXLimit : xPosition + 1;
			updateMenuState();
		});
		
		this.down.addActionListener(e -> {
			yPosition = yPosition >= mapYLimit ? mapYLimit : yPosition + 1;
			updateMenuState();
		});
		
	}
	
	
	private void updateMenuState() {
		menuPanel.setCurrentPosition("" + xPosition, "" + yPosition);
	}
	
	public void reset() {
		xPosition = 0;
		yPosition = 0;
		calculateMapLimits();
	}
	
	public void calculateMapLimits() {
		mapXLimit = 1;
		mapYLimit = 1;
	}
	
}
