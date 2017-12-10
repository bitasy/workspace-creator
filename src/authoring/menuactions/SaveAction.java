package authoring.menuactions;

import authoring.MenuAction;
import authoring.menu.GameSaver;
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

    @Override
    public void execute(){
        GameSaver save = new GameSaver();
        panelController.save(save.getName());
    }
}
