package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MessageViewer {
	
	public static void showMessage(final AlertType type, final String title, final String header,
			final String message, final String additional) {
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(title);
		
		String msg = message;
		
		if (!additional.isEmpty()) {
			msg += "\n\n";
			msg += additional;
		}
		
		alert.setContentText(msg);
		alert.showAndWait().ifPresent(response -> {
		     if (response == ButtonType.OK) {
		    	 
		     }
		 });
	}

	public static void showException(final String title, final String message, final String exception) {
		showMessage(AlertType.ERROR, title, "ERROR", message, exception);		
	}
	
	public static void showException(final String title, final String message) {
		showException(title, message, "");
	}
}
