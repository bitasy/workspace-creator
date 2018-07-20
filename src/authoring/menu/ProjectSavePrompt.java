package authoring.menu;

import authoring.PanelController;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import main.Peaches;

import java.util.Optional;

/**
 * Popup used to save the name of the new game that is created (also can be used to rename an existing game)
 * @author Walker Willetts
 * @author Kelly Zhang
 */
public class ProjectSavePrompt {

    private static final String SAVE_GAME = "Save Game";
    private static final String SAVE_PROMPT = "Please enter your game's name:";
    private static final String PANEL = "panel";
    private PanelController panelController;

    public ProjectSavePrompt(PanelController panelController) {
        this.panelController = panelController;
        Optional<String> result = createChoiceDialog().showAndWait();
        result.ifPresent(name -> { saveGame(name); });
    }

    private TextInputDialog createChoiceDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().getStylesheets().add(Peaches.getUser().getThemeName());
        dialog.getDialogPane().getStyleClass().add(PANEL);
        dialog.getDialogPane().getButtonTypes().remove(0);
        dialog.getDialogPane().getButtonTypes().add(0, new ButtonType(SAVE_GAME, ButtonBar.ButtonData.OK_DONE));
        dialog.setTitle(SAVE_GAME);
        dialog.setContentText(SAVE_PROMPT);
        return dialog;
    }

    private void saveGame(String name) {
        /*if(name.trim().length() != 0)
            panelController.save(name.trim());*/
    }
}