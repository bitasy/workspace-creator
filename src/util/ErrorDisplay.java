package util;

import javafx.scene.control.Alert;

public class ErrorDisplay {
    private StringBuilder errorMessage = new StringBuilder();
    private String title;

    public ErrorDisplay(String title){
        this.title = title;
    }

    public void addMessage(String message){
        errorMessage.append(message).append("\n");
    }

    public void addMessages(String... messages){
        for(String message : messages) errorMessage.append(message);
    }

    public void clear(){
        errorMessage.delete(0, errorMessage.length());
    }

    /**
     * Displays the current error message only if it is not empty.
     */
    public void displayError() {
        if(errorMessage.length() > 0) {
            Alert errors = new Alert(Alert.AlertType.ERROR);
            errors.setTitle(title);
            errors.setContentText(errorMessage.toString());
            errors.showAndWait();
        }
    }
}
