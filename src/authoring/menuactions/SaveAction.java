package authoring.menuactions;

import authoring.MenuAction;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import authoring.PanelController;

/**
 * Defines the MenuAction that saves the game currently being worked on in the authoring environment.
 *
 * @author Walker Willetts
 */
public class SaveAction implements MenuAction{
    private final PanelController panelController;

    /**
     * Creates a new SaveAction executor.
     * @param panelController the controller to communicate with
     */
    public SaveAction(PanelController panelController){
        this.panelController = panelController;
    }

    /**
     * Creates the Dialog<String> used to save the game under a cetain name
     * @return A {@code Dialog<String>} that allows users to save the game
     */
    private Dialog<String> createDialog() {
        Dialog<String> saveDialog = new Dialog<>();
        // Set the button types for dialog
        ButtonType saveButtonType = new ButtonType("Save Game", ButtonBar.ButtonData.OK_DONE);
        saveDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        // Create Grid for Dialog
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 15, 10, 15));
        TextField gameName = new TextField();
        grid.add(new Label("Game Name:   ") ,0,0);
        grid.add(gameName,1,0);
        saveDialog.getDialogPane().setContent(grid);
        // Handle Results of Button Clicks on Window
        saveDialog.setResultConverter(saveButton -> {
            if(saveButton == saveButtonType) return gameName.getText();
            return "";
        });
        return saveDialog;
    }

    @Override
    public void execute(){
        Dialog<String> saveDialog = createDialog();
        Optional<String> name = saveDialog.showAndWait();
        if(name.get().length() > 0) panelController.save(name.get());
    }
}
