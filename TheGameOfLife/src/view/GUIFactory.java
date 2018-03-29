package view;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public interface GUIFactory {

	JButton createButton(String text);

	JLabel createLabel(String text);
	
	JLabel createTitleLabel(String text);
	
	JTextField createTextField(boolean isEditable);
	
	JTextField createTextField(boolean isEditable, String text);
	
	
	class Standard implements GUIFactory {
		
		private static final Color COLOR_FONT = Color.BLACK;
		private static final Color COLOR_BUTTON = new Color(150, 150, 150);

		@Override
		public JButton createButton(String text) {
			final JButton button = new JButton(text);
            button.setForeground(COLOR_FONT);
            button.setBackground(COLOR_BUTTON);
            button.setOpaque(true);
            return button;
		}

		@Override
		public JLabel createLabel(String text) {
			final JLabel label = new JLabel();
            label.setForeground(COLOR_FONT);
            label.setText(text);
            return label;
		}

		@Override
		public JLabel createTitleLabel(String text) {
			final JLabel title = createLabel(text);
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("Serif", Font.PLAIN, 16));
            return title;
		}

		@Override
		public JTextField createTextField(boolean isEditable) {
			return createTextField(isEditable, "");
		}
		
		@Override
		public JTextField createTextField(boolean isEditable, String text) {
			final JTextField field = new JTextField();           
            field.setHorizontalAlignment(JTextField.LEFT);
            field.setEditable(isEditable);
            field.setMaximumSize(new Dimension(1000, 25));
            field.setText(text);
            return field;
		}
		
	}
}
