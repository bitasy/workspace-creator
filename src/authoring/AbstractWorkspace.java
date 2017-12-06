package authoring;

import authoring.panels.PanelManager;
import authoring.Positions.Position;
import javafx.scene.control.Tab;
import util.PropertiesReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractWorkspace implements Workspace{
    
    private static final String DATA = "workspacedata";
    private String defaultVisibility = PropertiesReader.value(DATA, "defaultvisibility");
    protected final Properties properties = new Properties();
    
    private Position defaultPosition;
    private PanelManager manager;
    private TabManager tabManager;
    private Positions positions;
    private Map<String, Position> panelPositions;
    
    public AbstractWorkspace(double width, double height, PanelManager manager) throws IOException {
        this.manager = manager;
        positions = positionList();
        panelPositions = new HashMap<>();
        defaultPosition = positions.getPosition(defaultPosition());
        tabManager = new TabManager(positions);
        loadFile();
        populateScreen();
        setupWorkspace(width, height);
    }

    protected abstract Positions positionList();

    protected abstract String defaultPosition();
    
    protected void loadFile() throws IOException {
        File file = new File(String.format(PropertiesReader.value(DATA, "filepath"), this.getClass().getSimpleName()));
        if(file.exists()){
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
            for(String panel : manager.getPanels()){
                Position position = positions.getPosition(properties.getProperty(panel));
                if(position != null){
                    panelPositions.put(panel, position);
                } else {
                    properties.setProperty(panel, defaultPosition.toString());
                    panelPositions.put(panel, defaultPosition);
                }
            }
        } else {
            createFile(file);
            loadFile();
        }
    }

    protected void populateScreen(){
        for(String panel : panelPositions.keySet()){
            panelPositions.get(panel).addTab(newTab(panel));
        }
    }

    protected abstract void setupWorkspace(double width, double height);

    private Tab newTab(String panel) {
        Tab tab = tabManager.newTab(panel);
        tab.setContent(manager.getPanelDisplay(panel));
        tab.setOnCloseRequest(event -> {
            if(tab.getTabPane().getTabs().size() == 1){
                event.consume();
            }
        });
        return tab;
    }

    private void createFile(File location) throws IOException{
        for(String panel : manager.getPanels()){
            properties.setProperty(panel, defaultPosition.toString());
            properties.setProperty(String.format(PropertiesReader.value(DATA, "visibilitytag"), panel), defaultVisibility);
        }
        saveToFile(location, properties);
    }

    protected void saveToFile(File file, Properties properties) throws IOException{
        OutputStream output = new FileOutputStream(file);
        properties.store(output, String.format(PropertiesReader.value(DATA, "dataheader"), getClass().getSimpleName()));
        output.close();
    }

    protected double getDoubleValue(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }
}
