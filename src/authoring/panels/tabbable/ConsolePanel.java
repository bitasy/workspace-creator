package authoring.panels.tabbable;

import authoring.IPanelController;
import authoring.Panel;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 *
 */
public class ConsolePanel implements Panel{

    private TextArea consoleField = new TextArea();
    {
        consoleField.setEditable(false);
    }

    @Override
    public Region getRegion() {
        return consoleField;
    }

    @Override
    public void setController(IPanelController controller) {
        //TODO: Create controller
    }

    @Override
    public String title(){
        return "Console";
    }

}
