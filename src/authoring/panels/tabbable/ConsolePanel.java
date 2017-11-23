package authoring.panels.tabbable;

import authoring.IPanelDelegate;
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
    public void setController(IPanelDelegate controller) {
    }

    @Override
    public String title(){
        return "Console";
    }

}
