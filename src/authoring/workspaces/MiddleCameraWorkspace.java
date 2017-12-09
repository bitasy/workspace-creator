package authoring.workspaces;

import authoring.AbstractWorkspace;
import authoring.Positions;
import authoring.panels.PanelManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Defines a workspace with the Camera panel in the middle of the screen, and two TabPane areas on either side of the Camera with one more below it.
 * @author Brian Nieves
 */
public class MiddleCameraWorkspace extends AbstractWorkspace {

    private Positions positions;

    private SplitPane body;
    private SplitPane middle;
    private TabPane bottom;
    private TabPane left;
    private TabPane right;
    private double leftDivision = 0.2; //Default value
    private double rightDivision = 0.7; //Default value
    private double bodyDivision = 0.7; //Default value

    /**
     * Creates a MiddleCameraWorkspace and initializes it.
     * @param width the width of the workspace
     * @param height the height of the workspace
     * @param manager the manager for the panels to be added to the workspace
     * @throws IOException if there is a problem loading the panels
     */
    public MiddleCameraWorkspace(double width, double height, PanelManager manager) throws IOException{
        super(width, height, manager);
    }

    @Override
    protected Positions positionList() {
        positions = new Positions("bottom", "left", "right");
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
        ((SplitPane)body.getItems().get(0)).getItems().set(1, cameraPanel);
        setDividerFields();
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(leftDivision, rightDivision);
    }

    @Override
    public Region getWorkspace() {
        return body;
    }

    @Override
    public void save() throws IOException{
        setDividerFields();
        super.save();
    }

    @Override
    protected void setupWorkspace(double width, double height) {
        body.setOrientation(Orientation.VERTICAL);
        middle.setOrientation(Orientation.HORIZONTAL);

        Pane cameraDummy = new Pane();

        middle.getItems().addAll(left, cameraDummy, right);
        middle.setDividerPositions(leftDivision, rightDivision);

        body.getItems().addAll(middle, bottom);
        body.setDividerPosition(0, bodyDivision);
        right.setMinWidth(0);
        middle.setMinWidth(width);
        middle.setMaxWidth(width);
        middle.setMinHeight(0);
        body.setMinHeight(height);
    }

    @Override
    protected void loadFile() throws IOException {
        super.loadFile();
        leftDivision = getDoubleValue("leftdivision");
        rightDivision = getDoubleValue("rightdivision");
        bodyDivision = getDoubleValue("bodydivision");
        initialize();
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(leftDivision, rightDivision);
    }

    private void initialize() {
        body = new SplitPane();
        middle = new SplitPane();
        left = positions.getPosition("left").getPane();
        bottom = positions.getPosition("bottom").getPane();
        right = positions.getPosition("right").getPane();
    }

    private void setDividerFields() {
        bodyDivision = body.getDividerPositions()[0];
        leftDivision = middle.getDividerPositions()[0];
        rightDivision = middle.getDividerPositions()[1];
        properties.setProperty("leftdivision", leftDivision + "");
        properties.setProperty("rightdivision", leftDivision + "");
        properties.setProperty("bodydivision", bodyDivision + "");
    }
}