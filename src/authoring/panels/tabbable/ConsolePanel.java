package authoring.panels.tabbable;

import authoring.Panel;
import authoring.PanelController;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import util.Console;

public class ConsolePanel implements Panel{

    private static final String PANEL = "panel";
    private static final String CONSOLE = "Console";

    private Pane myPane;
    private TextArea consoleField = new TextArea();
    private Console myConsole;

    public ConsolePanel() {
        myPane = new Pane();
        myPane.getStyleClass().add(PANEL);

        consoleField.setEditable(false);
    }

    @Override
    public Region getRegion() {
        return myPane;
    }

    @Override
    public void setController(PanelController controller) {
        //TODO: Create controller
    }

    @Override
    public String title(){
        return CONSOLE;
    }
}