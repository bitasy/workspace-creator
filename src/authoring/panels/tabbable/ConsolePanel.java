package authoring.panels.tabbable;

import authoring.IPanelController;
import authoring.Panel;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 *
 */
public class ConsolePanel implements Panel{

    private Pane myPane;

    public ConsolePanel() {
        myPane = new Pane();
        myPane.getStyleClass().add("panel");
    }

    private TextArea consoleField = new TextArea();
    {
        consoleField.setEditable(false);
    }

    @Override
    public Region getRegion() {
        return myPane;
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
