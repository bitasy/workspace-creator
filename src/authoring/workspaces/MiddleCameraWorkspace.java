package authoring.workspaces;

import authoring.Positions;
import authoring.TabManager;
import authoring.Workspace;
import authoring.panels.PanelManager;
import authoring.Positions.Position;
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

public class MiddleCameraWorkspace implements Workspace {

    private final Positions positions = new Positions("right", "left", "bottom");

    private final Properties properties = new Properties();
    private final ResourceBundle data = ResourceBundle.getBundle("workspacedata");

    private final Position DEFAULT_POSITION = positions.getPosition("right");
    private final String DEFAULT_VISIBILITY = data.getString("defaultvisibility");

    private PanelManager manager;
    private TabManager tabManager;
    private Map<String, Position> panelPositions;

    private SplitPane body;
    private SplitPane middle;
    private TabPane bottom;
    private TabPane left;
    private TabPane right;
    private double leftDivision = 0.2; //Default value
    private double rightDivision = 0.8; //Default value
    private double bodyDivision = 0.7; //Default value

    public MiddleCameraWorkspace(double width, double height, PanelManager manager) throws IOException{
        this.manager = manager;
        initialize();
        loadFile();
        setupWorkspace(width, height);
        populateScreen();
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

    private void initialize() {
        body = new SplitPane();
        middle = new SplitPane();
        left = positions.getPosition("left").getPane();
        bottom = positions.getPosition("bottom").getPane();
        right = positions.getPosition("right").getPane();
        tabManager = new TabManager(positions);
        panelPositions = new HashMap<>();
    }

    private void setupWorkspace(double width, double height) {
        body.setOrientation(Orientation.VERTICAL);
        middle.setOrientation(Orientation.HORIZONTAL);

        Pane cameraDummy = new Pane();

        middle.getItems().addAll(left, cameraDummy, right);
        middle.setDividerPositions(leftDivision, rightDivision);

        body.getItems().addAll(middle, bottom);
        body.setDividerPosition(0, bodyDivision);
        right.setMinWidth(0);
        middle.setMinWidth(width);
        middle.setMinHeight(0);
        body.setMinHeight(height);
    }

    private void loadFile() throws IOException {
        File file = new File(String.format(data.getString("filepath"), this.getClass().getSimpleName()));
        if(file.exists()){
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
            for(String panel : manager.getPanels()){
                Position position = positions.getPosition(properties.getProperty(panel));
                if(position != null){
                    panelPositions.put(panel, position);
                } else {
                    properties.setProperty(panel, DEFAULT_POSITION.toString());
                    panelPositions.put(panel, DEFAULT_POSITION);
                }
            }
            leftDivision = getDoubleValue("leftdivision");
            rightDivision = getDoubleValue("rightdivision");
            bodyDivision = getDoubleValue("bodydivision");
        } else {
            createFile(file);
            loadFile();
        }
    }

    private void populateScreen(){
        for(String panel : panelPositions.keySet()){
            panelPositions.get(panel).addTab(newTab(panel));
        }
        body.setDividerPositions(bodyDivision);
        middle.setDividerPositions(leftDivision, rightDivision);
    }

    private Tab newTab(String panel) {
        Tab tab = tabManager.newTab(panel);
        tab.setOnCloseRequest(event -> {
            if(tab.getTabPane().getTabs().size() == 1){
                event.consume();
            }
        });
        return tab;
    }

    private void createFile(File location) throws IOException{
        for(String panel : manager.getPanels()){
            properties.setProperty(panel, DEFAULT_POSITION.toString());
            properties.setProperty(String.format(data.getString("visibilitytag"), panel), DEFAULT_VISIBILITY);
        }
        saveToFile(location, properties);
    }

    private void saveToFile(File file, Properties properties) throws IOException{
        properties.setProperty("leftdivision", leftDivision + "");
        properties.setProperty("rightdivision", rightDivision + "");
        properties.setProperty("bodydivision", bodyDivision + "");

        OutputStream output = new FileOutputStream(file);
        properties.store(output, String.format(data.getString("dataheader"), getClass().getSimpleName()));
        output.close();
    }

    private double getDoubleValue(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }
}