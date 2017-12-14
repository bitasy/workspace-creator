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
 * Defines a workspace with the Camera panel in the middle of the screen, and two TabPane areas on either side of the Camera with one more below it.
 * @author Brian Nieves
 */
public class MiddleCameraWorkspace extends AbstractWorkspace {

    private static final String BOTTOM = "bottom";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String LEFT_DIVISION_STRING = "leftdivision";
    private static final String RIGHT_DIVISION_STRING = "rightdivision";
    private static final String BODY_DIVISION_STRING = "bodydivision";
    private static final String EMPTY_STRING = "";
    private static final String TITLE = "Middle Camera Workspace";

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
    public String title(){
        return TITLE;
    }

    @Override
    protected Positions positionList() {
        positions = new Positions(BOTTOM, LEFT, RIGHT);
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
        ((SplitPane)body.getItems().get(0)).getItems().set(1, cameraPanel);
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(leftDivision, rightDivision);
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
        leftDivision = getDoubleValue(LEFT_DIVISION_STRING);
        rightDivision = getDoubleValue(RIGHT_DIVISION_STRING);
        bodyDivision = getDoubleValue(BODY_DIVISION_STRING);
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(leftDivision, rightDivision);
    }

    @Override
    protected void saveState(){
        setDividerProperties();
    }

    private void initialize() {
        body = new SplitPane();
        middle = new SplitPane();
        left = positions.getPosition(LEFT).getPane();
        bottom = positions.getPosition(BOTTOM).getPane();
        right = positions.getPosition(RIGHT).getPane();
    }

    private void setDividerFields() {
        bodyDivision = body.getDividerPositions()[0];
        leftDivision = middle.getDividerPositions()[0];
        rightDivision = middle.getDividerPositions()[1];
        setDividerProperties();
    }

    private void setDividerProperties() {
        properties.put(LEFT_DIVISION_STRING, leftDivision + EMPTY_STRING);
        properties.put(RIGHT_DIVISION_STRING, rightDivision + EMPTY_STRING);
        properties.put(BODY_DIVISION_STRING, bodyDivision + EMPTY_STRING);
    }
}