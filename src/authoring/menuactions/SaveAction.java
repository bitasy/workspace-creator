package authoring.menuactions;

import authoring.IPanelController;
import authoring.MenuAction;

/**
 * Defines the MenuAction that saves the game currently being worked on in the authoring environment.
 */
public class SaveAction implements MenuAction{
    private final IPanelController panelController;

    /**
     * Creates a new SaveAction executor.
     * @param panelController the controller to communicate with
     */
    public SaveAction(IPanelController panelController){
        this.panelController = panelController;
    }

    @Override
    public void execute(){
        panelController.save("needs ot be changed");
    }
}
