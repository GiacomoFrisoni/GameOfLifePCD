package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ExceptionViewer {
	
	public static void showMessage(AlertType type, String title, String header, String message, String additional) {
		Alert alert = new Alert(AlertType.ERROR);
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

	public static void showException(String title, String message, String exception) {
		showMessage(AlertType.ERROR, title, "ERROR", message, exception);		
	}
	
	public static void showException(String title, String message) {
		showException(title, message, "");
	}
}
