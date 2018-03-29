package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controller.GameController;

public class CellMapViewer extends JPanel {
	
	private final CellMapDrawPanel map;
	private final GameController controller;

	public CellMapViewer(CellMapDrawPanel map, GameController controller) {
		this.map = map;
		this.controller = controller;
		
		// Sets the layout
        final GUIFactory factory = new GUIFactory.Standard();
		this.setLayout(new BorderLayout());
		this.add(this.map, BorderLayout.CENTER);
		this.add(factory.createButton("<"), BorderLayout.WEST);
		this.add(factory.createButton("^"), BorderLayout.NORTH);
		this.add(factory.createButton(">"), BorderLayout.EAST);
		this.add(factory.createButton("v"), BorderLayout.SOUTH);
		
	}
	
}
