package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This interface uses and Abstract Factory pattern to define
 * some common aspects of the GUI and to facilitate their change.
 *
 */
public interface GUIFactory {

	/**
	 * @return a {@link JPanel} with a colored background.
	 */
	JPanel createPanel();
	
	/**
	 * Creates a customized {@link JButton}.
	 * 
	 * @param text
	 * 		the content of the button
	 * @return the specified button
	 */
	JButton createButton(String text);

	JButton createPanelButton(String text);

	JLabel createErrorLabel(String text);

	/**
	 * Creates a customized {@link JLabel}.
	 * 
	 * @param text
	 * 		the text to display
	 * @return the specified label
	 */
	JLabel createLabel(String text);
	
	/**
	 * Creates a customized {@link JLabel}.
	 * 
	 * @param text
	 * 		the text to display
	 * @return the specified title label
	 */
	JLabel createTitleLabel(String text);
	
	/**
	 * Creates a customized {@link JTextField}.
	 * 
	 * @param isEditable
	 * 		true if it must be editable, false otherwise
	 * @param text
	 * 		the default text content
	 * @return the specified text field
	 */
	JTextField createTextField(boolean isEditable, String text);
	
	/**
	 * Creates a customized {@link JTextField}.
	 * 
	 * @param isEditable
	 * 		true if it must be editable, false otherwise
	 * @return the specified text field
	 */
	JTextField createTextField(boolean isEditable);
	
	Color getBackgroundColor();
	
	Color getAliveCellColor();
	
	Color getDeadCellColor();
	
	
	/**
	 * A standard implementation of {@link GUIFactory}.
	 * 
	 */
	class Standard implements GUIFactory {
		
		private static final String EMPTY_STRING = "";
		
		private static final Color BACKGROUND_COLOR = new Color(60, 60, 60);
		private static final Color TEXT_COLOR = Color.WHITE;
		private static final Color BUTTON_COLOR = new Color(80, 80, 80);
		private static final Color ALIVE_CELL_COLOR = Color.GREEN;
		private static final Color DEAD_CELL_COLOR = Color.BLACK;

		@Override
		public JPanel createPanel() {
			final JPanel panel = new JPanel();
			panel.setBackground(BACKGROUND_COLOR);
			return panel;
		}
		
		@Override
		public JButton createButton(final String text) {
			final JButton button = new JButton(text);
            button.setForeground(TEXT_COLOR);
            button.setBackground(BUTTON_COLOR);
            button.setPreferredSize(new Dimension(45, 45));
            button.setOpaque(true);
            return button;
		}
		
		@Override
		public JButton createPanelButton(final String text) {
			final JButton button = createButton(text);
			button.setMaximumSize(new Dimension(300, 25));
            return button;
		}

		@Override
		public JLabel createLabel(final String text) {
			final JLabel label = new JLabel();
            label.setForeground(TEXT_COLOR);
            label.setText(text);
            return label;
		}

		@Override
		public JLabel createTitleLabel(final String text) {
			final JLabel label = createLabel(text);
			label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 16));
            return label;
		}
		
		@Override
		public JLabel createErrorLabel(final String text) {
			final JLabel error = createLabel(text);
            error.setForeground(Color.RED);
            error.setFont(new Font(error.getFont().getFontName(), Font.ITALIC, 10));
            return error;
		}
		
		@Override
		public JTextField createTextField(final boolean isEditable, final String text) {
			final JTextField field = new JTextField();           
            field.setHorizontalAlignment(JTextField.LEFT);
            field.setEditable(isEditable);
            field.setMaximumSize(new Dimension(1000, 25));
            field.setText(text);
            return field;
		}
		
		@Override
		public JTextField createTextField(final boolean isEditable) {
			return createTextField(isEditable, EMPTY_STRING);
		}

		@Override
		public Color getBackgroundColor() {
			return BACKGROUND_COLOR;
		}
		
		@Override
		public Color getAliveCellColor() {
			return ALIVE_CELL_COLOR;
		}

		@Override
		public Color getDeadCellColor() {
			return DEAD_CELL_COLOR;
		}
		
	}
}
