package authoring.panels.tabbable;

import authoring.Panel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.PropertiesReader;
import util.drawing.ImageCanvasPane;

import java.io.File;
import java.util.*;
import java.util.function.Supplier;

/**
 * drawing panel inside the authoring environment
 * @author Kelly Zhang
 */
public class DrawingPanel implements Panel {

    private Button newDrawing;
    private HBox myInputs;
    private VBox myArea;
    private TextField nameDrawing;
    private ImageCanvasPane myDrawing;



    public DrawingPanel() {
        myArea = new VBox();
        myInputs = new HBox();

        nameDrawing = new TextField();
        nameDrawing.setPromptText("name your drawing");
        nameDrawing.setOnMouseClicked(e -> nameDrawing.clear());
        myInputs.getChildren().add(nameDrawing);

        newDrawing = new Button("new drawing");
        newDrawing.setOnMouseClicked(e -> makeNewCanvas());
        myInputs.getChildren().add(newDrawing);

        myArea.getChildren().add(myInputs);
        myArea.fillWidthProperty().setValue(true);

        getRegion().getStyleClass().add("panel");
    }

    private void makeNewCanvas() {
        Supplier<File> myFile = () -> new File("./resources/drawImages/" + nameDrawing.getText() + ".png");
        myDrawing = new ImageCanvasPane(600,1000,myFile);
        if (myArea.getChildren().size() > 1) {
            myArea.getChildren().remove(1);
        }
        myArea.getChildren().add(myDrawing);
    }

    @Override
    public Region getRegion() {
        return myArea;
    }

    @Override
    public String title() {
        return "Drawing";
    }
}
