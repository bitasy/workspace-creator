package authoring.panels.reserved;

import authoring.Panel;
import authoring.PanelController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Main panel inside authoring environment that displays the main content. This panel cannot be removed from view.
 * @author Brian Nieves
 */
public class MainPanel implements Panel {
    private static final String TITLE = "Main Area";

    private TextArea myView;
    private VBox myArea;
    private PanelController myController;

    public MainPanel(double width, double height) {

        myView = new TextArea();
        myView.setPrefWidth(width);
        myView.setPrefHeight(height);

        myArea = new VBox(buttonRow(), myView);

        myView.getStyleClass().add("main");
        myArea.getStyleClass().add("panel");
    }

    /**
     * Set up option buttons.
     * @return HBox with all of the buttons.
     */
    private HBox buttonRow() {
        Button example1 = new Button("Options");
        Button example2 = new Button("More");

        HBox buttonRow = new HBox(example1, example2);
        buttonRow.setAlignment(Pos.CENTER);

        return buttonRow;
    }

    @Override
    public Region getRegion() {
        return myArea;
    }

    @Override
    public void setController(PanelController controller) {
        this.myController = controller;
    }

    @Override
    public String title(){
        return TITLE;
    }
}
