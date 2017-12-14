package authoring.workspaces;

import authoring.AbstractWorkspace;
import authoring.Positions;
import authoring.panels.PanelManager;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;

/**
 * Defines a workspace with the Camera panel of the left side of the screen, and two TabPane areas on the right and bottom.
 * @author Brian Nieves
 */
public class LeftCameraWorkspace extends AbstractWorkspace {

    private static final String RIGHT = "right";
    private static final String BOTTOM = "bottom";
    private static final String MIDDLE_DIVISION_STRING = "middledivision";
    private static final String BODY_DIVISION_STRING = "bodydivision";
    private static final String EMPTY_STRING = "";
    private static final String TITLE = "Left Camera Workspace";

    private Positions positions;
    private SplitPane body;
    private SplitPane middle;
    private TabPane bottom;
    private TabPane right;
    private double middleDivision = 0.7; //Default value
    private double bodyDivision = 0.7; //Default value

    /**
     * Creates a LeftCameraWorkspace and initializes it.
     * @param width the width of the workspace
     * @param height the height of the workspace
     * @param manager the manager for the panels to be added to the workspace
     * @throws IOException if there is a problem loading the panels
     */
    public LeftCameraWorkspace(double width, double height, PanelManager manager) throws IOException{
        super(width, height, manager);

    }

    @Override
    public String title(){
        return TITLE;
    }

    @Override
    protected Positions positionList() {
        positions = new Positions(RIGHT, BOTTOM);
        initialize();
        return positions;
    }

    @Override
    protected String defaultPosition() {
        return RIGHT;
    }

    @Override
    public void addCameraPanel(Region cameraPanel) {
        cameraPanel.setMinWidth(0);
        cameraPanel.setMinHeight(0);
        ((SplitPane)body.getItems().get(0)).getItems().set(0, cameraPanel);
        setDividerFields();
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(middleDivision);
    }

    @Override
    public Region getWorkspace() {
        return body;
    }

    @Override
    public void deactivate() throws IOException{
        setDividerFields();
        super.deactivate();
    }

    @Override
    protected void loadFile() throws IOException {
        super.loadFile();
        middleDivision = getDoubleValue(MIDDLE_DIVISION_STRING);
        bodyDivision = getDoubleValue(BODY_DIVISION_STRING);
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(middleDivision);
    }

    @Override
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
        middle.setMaxWidth(width);
        middle.setMinHeight(0);
        body.setMinHeight(height);
    }

    @Override
    protected void saveState(){
        setDividerProperties();
    }

    private void initialize() {
        body = new SplitPane();
        middle = new SplitPane();
        bottom = positions.getPosition(BOTTOM).getPane();
        right = positions.getPosition(RIGHT).getPane();
    }

    private void setDividerFields() {
        middleDivision = middle.getDividerPositions()[0];
        bodyDivision = body.getDividerPositions()[0];
        setDividerProperties();
    }

    private void setDividerProperties() {
        properties.put(MIDDLE_DIVISION_STRING, middleDivision + "");
        properties.put(BODY_DIVISION_STRING, bodyDivision + "");
    }
}