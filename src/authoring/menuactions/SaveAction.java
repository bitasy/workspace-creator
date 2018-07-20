package authoring.menuactions;

import authoring.MenuAction;
import authoring.PanelController;
import authoring.menu.ProjectSavePrompt;

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
        //ProjectSavePrompt save = new ProjectSavePrompt(panelController);
    }
}