package authoring.workspaces;

import authoring.DraggableTab;
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

public class LeftCameraWorkspace implements Workspace {

    private enum Position {
        BOTTOM("bottom"),
        RIGHT("right");

        private String name;

        Position (String name){
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        private static Position getEnum(String name){
            switch (name) {
                case "right":
                    return RIGHT;
                case "bottom":
                    return BOTTOM;
                default:
                    return null;
            }
        }
    }

    private Properties properties = new Properties();
    private ResourceBundle data = ResourceBundle.getBundle("workspacedata");

    private final Position DEFAULT_POSITION = Position.RIGHT;
    private final String DEFAULT_VISIBILITY = data.getString("defaultvisibility");

    private PanelManager manager;
    private Map<String, Position> panelPositions;

    private SplitPane body;
    private SplitPane middle;
    private TabPane bottom;
    private TabPane right;
    private double middleDivision = 0.7; //Default value
    private double bodyDivision = 0.7; //Default value

    public LeftCameraWorkspace(double width, double height, PanelManager manager) throws IOException{
        this.manager = manager;
        panelPositions = new HashMap<>();
        initialize();
        loadFile();
        setupWorkspace(width, height);
        populateScreen();
    }

    @Override
    public void addCameraPanel(Region cameraPanel) {
        cameraPanel.setMinWidth(0);
        cameraPanel.setMinHeight(0);
        ((SplitPane)body.getItems().get(0)).getItems().set(0, cameraPanel);
    }


    @Override
    public Region getWorkspace() {
        return body;
    }

    private void initialize() {
        body = new SplitPane();
        middle = new SplitPane();
        bottom = new TabPane();
        right = new TabPane();
    }

    private void setupWorkspace(double width, double height) {
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

    private void loadFile() throws IOException {
        File file = new File(String.format(data.getString("filepath"), this.getClass().getSimpleName()));
        if(file.exists()){
            FileInputStream input = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(input);
            for(String panel : manager.getPanels()){
                Position position = Position.getEnum(properties.getProperty(panel));
                if(position != null){
                    panelPositions.put(panel, position);
                } else {
                    properties.setProperty(panel, DEFAULT_POSITION.toString());
                    panelPositions.put(panel, DEFAULT_POSITION);
                }
            }
            middleDivision = getDoubleValue("middledivision");
            bodyDivision = getDoubleValue("bodydivision");
        } else {
            createFile(file);
            loadFile();
        }
    }

    private void populateScreen(){
        for(String panel : panelPositions.keySet()){
            switch (panelPositions.get(panel)){
                case RIGHT:
                    makeTab(panel, right);
                    break;
                case BOTTOM:
                    makeTab(panel, bottom);
                    break;
            }
        }
    }

    private void createFile(File location) throws IOException{
        for(String panel : manager.getPanels()){
            properties.setProperty(panel, DEFAULT_POSITION.toString());
            properties.setProperty(String.format(data.getString("visibilitytag"), panel), DEFAULT_VISIBILITY);
        }
        saveToFile(location, properties);
    }

    private void saveToFile(File file, Properties properties) throws IOException{
        properties.setProperty("middledivision", middleDivision + "");
        properties.setProperty("bodydivision", bodyDivision + "");

        OutputStream output = new FileOutputStream(file);
        properties.store(output, String.format(data.getString("dataheader"), getClass().getSimpleName()));
        output.close();
    }

    private void makeTab(String panel, TabPane tabPane){
        Tab tab = new DraggableTab(manager.getPanelTitle(panel));
        Region region = manager.getPanelDisplay(panel);
        tab.setContent(region);
        tab.setOnCloseRequest(event -> {
            if(tab.getTabPane().getTabs().size() == 1){
                event.consume();
            }
        });
        tabPane.getTabs().add(tab);
    }

    private double getDoubleValue(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }
}