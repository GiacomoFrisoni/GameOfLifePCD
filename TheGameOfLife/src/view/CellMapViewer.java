package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.GameController;

public class CellMapViewer extends JPanel {
	
	private final CellMapDrawPanel map;
	private final GameController controller;
	
	private final JButton left;
	private final JButton up;
	private final JButton right;
	private final JButton down;
	
	private int xPosition = 1;
	private int yPosition = 1;
	
	private final static int MAX = 5;

	public CellMapViewer(CellMapDrawPanel map, GameController controller) {
		this.map = map;
		this.controller = controller;
		
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
		
		
		this.left.addActionListener(e -> {
			xPosition = xPosition < 1 ? 1 : xPosition - 1;
		});
		
		this.up.addActionListener(e -> {
			yPosition = yPosition < 1 ? 1 : yPosition - 1;
		});
		
		this.right.addActionListener(e -> {
			xPosition = xPosition > MAX ? MAX : xPosition + 1;
		});
		
		this.down.addActionListener(e -> {
			yPosition = xPosition < MAX ? MAX : xPosition + 1;
		});
		
	}
	
}
