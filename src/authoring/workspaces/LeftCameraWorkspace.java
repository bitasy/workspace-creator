package authoring.workspaces;

import authoring.AbstractWorkspace;
import authoring.Positions;
import authoring.Positions.Position;
import authoring.TabManager;
import authoring.Workspace;
import authoring.panels.PanelManager;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class LeftCameraWorkspace extends AbstractWorkspace {

    private Positions positions;

    private SplitPane body;
    private SplitPane middle;
    private TabPane bottom;
    private TabPane right;
    private double middleDivision = 0.7; //Default value
    private double bodyDivision = 0.7; //Default value

    public LeftCameraWorkspace(double width, double height, PanelManager manager) throws IOException{
        super(width, height, manager);

    }

    @Override
    protected Positions positionList() {
        positions = new Positions("right", "bottom");
        return positions;
    }

    @Override
    protected String defaultPosition() {
        return "right";
    }

    @Override
    public void addCameraPanel(Region cameraPanel) {
        cameraPanel.setMinWidth(0);
        cameraPanel.setMinHeight(0);
        ((SplitPane)body.getItems().get(0)).getItems().set(0, cameraPanel);
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(middleDivision);
    }

    @Override
    public Region getWorkspace() {
        return body;
    }

    protected void loadFile() throws IOException {
        super.loadFile();
        middleDivision = getDoubleValue("middledivision");
        bodyDivision = getDoubleValue("bodydivision");
    }

    protected void populateScreen(){
        initialize();
        super.populateScreen();
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(middleDivision);
    }

    protected void saveToFile(File file, Properties properties) throws IOException{
        properties.setProperty("middledivision", middleDivision + "");
        properties.setProperty("bodydivision", bodyDivision + "");

        super.saveToFile(file, properties);
    }

    private void initialize() {
        body = new SplitPane();
        middle = new SplitPane();
        bottom = positions.getPosition("bottom").getPane();
        right = positions.getPosition("right").getPane();
    }

    protected void setupWorkspace(double width, double height) {
        body.setOrientation(Orientation.VERTICAL);
        middle.setOrientation(Orientation.HORIZONTAL);

        Pane cameraDummy = new Pane();

        middle.getItems().addAll(cameraDummy, right);
        middle.setDividerPosition(0, middleDivision);

        body.getItems().addAll(middle, bottom);
        body.setDividerPosition(0, bodyDivision);
        right.setMinWidth(0);
        middle.setMinWidth(width);
        middle.setMinHeight(0);
        body.setMinHeight(height);
    }
}