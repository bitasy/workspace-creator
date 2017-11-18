package authoring.panels.tabbable;

import authoring.IPanelDelegate;
import authoring.Panel;
import authoring.Screen;
import authoring.ScreenPosition;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class ConsolePanel implements Panel{

    private TextArea consoleField = new TextArea();
    {
        consoleField.setEditable(false);
        consoleField.setBackground(new Background(new BackgroundFill(Color.LAVENDER, null, null)));
    }

    @Override
    public Region getRegion() {
        return consoleField;
    }

    @Override
    public ScreenPosition getPosition(){
        return ScreenPosition.BOTTOM;
    }

    @Override
    public void setController(IPanelDelegate controller) {
        //TODO: Create controller
    }

    @Override
    public String title(){
        return "Console";
    }

}
