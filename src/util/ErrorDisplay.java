package util;

import javafx.scene.control.Alert;
import main.VoogaPeaches;

/**
 * Class for managing the collection and display of error messages to the user. Messages are accumulated and displayed on command in a separate stage.
 * @author Brian Nieves
 * @author Kelly Zhang
 */
public class ErrorDisplay {
    private StringBuilder errorMessage = new StringBuilder();
    private String title;

    /**
     * Creates a new ErrorDisplay and sets its title.
     * @param title the title of the ErrorDisplay
     */
    public ErrorDisplay(String title){
        this.title = title;
    }

    /**
     * Creates a new ErrorDisplay and sets its title.
     * @param title the title of the ErrorDisplay
     */
    public ErrorDisplay(String title, String message){
        this.title = title;
        addMessage(message);
    }

    /**
     * Adds a message to be displayed.
     * @param message the error message
     */
    public void addMessage(String message){
        errorMessage.append(message).append("\n\n");
    }

    /**
     * Adds multiple messages to be displayed. This is the same as calling addMessage() multiple times.
     * @param messages the error messages
     */
    public void addMessages(String... messages){
        for(String message : messages) addMessage(message);
    }

    /**
     * Clears this ErrorDisplay of all messages.
     */
    public void clear(){
        errorMessage.delete(0, errorMessage.length());
    }

    /**
     * Displays the current error message only if it is not empty. Clears this ErrorDisplay's messages after running.
     */
    public void displayError() {
        if(errorMessage.length() > 0) {
            Alert errors = new Alert(Alert.AlertType.ERROR);
            errors.getDialogPane().getStylesheets().add(VoogaPeaches.getUser().getThemeName());
            errors.getDialogPane().getStyleClass().add("panel");
            errors.setTitle(title);
            errors.setContentText(errorMessage.toString());
            
            errors.showAndWait();
        }
        clear();
    }
}
