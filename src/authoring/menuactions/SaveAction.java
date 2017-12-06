package authoring.menuactions;

import authoring.IPanelController;
import authoring.MenuAction;

/**
 * Defines the MenuAction that saves the game currently being worked on in the authoring environment.
 */
public class SaveAction implements MenuAction{
    private final IPanelController panelController;

    public SaveAction(IPanelController panelController){
        this.panelController = panelController;
    }
    public void execute(){
        panelController.save();
    }
}
