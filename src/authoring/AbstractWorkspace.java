package authoring;

import authoring.Positions.Position;
import authoring.panels.PanelManager;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import util.PropertiesReader;
import util.pubsub.PubSub;
import util.pubsub.messages.Message;
import util.pubsub.messages.MoveTabMessage;
import util.pubsub.messages.StringMessage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A partial implementation of a Workspace that handles all of the position, tab, data, and panel management. Concrete classes must implement the visual implementation, and can add to the data saved to the file by overriding the protected methods.
 * @see authoring.Workspace
 * @author Brian Nieves
 */
public abstract class AbstractWorkspace implements Workspace{

    private static final String DATA = "workspacedata";
    private static final String VISIBILITY_TAG = "visibilitytag";
    private static final String FILEPATH = "filepath";
    private static final String DATAHEADER = "dataheader";
    private static final String PANEL_TOGGLE = "PANEL_TOGGLE";
    private static final String LOAD_TAB = "LOAD_TAB";
    private static final String PUT_TAB = "PUT_TAB";
    private boolean defaultVisibility = Boolean.parseBoolean(PropertiesReader.value(DATA, "defaultvisibility"));
    protected final Properties properties = new Properties();
    
    private Position defaultPosition;
    private PanelManager manager;
    private TabManager tabManager;
    private Positions positions;
    private Map<String, Position> panelPositions;
    private Map<String, Boolean> visibilities;

    private Consumer<Message> panelToggle = message -> toggle(((StringMessage)message).readMessage());
    private Function<Message, Object> loadTab = message -> loadPanel(((StringMessage)message).readMessage());
    private Consumer<Message> putTab = message -> movePanel(((MoveTabMessage)message).tab(), ((MoveTabMessage)message).pane());

    /**
     * Constructs an initializes a Workspace object.
     * @param width the width of the workspace
     * @param height the height of the workspace
     * @param manager the manager for the panels to be added to the workspace
     * @throws IOException if there is a problem loading the panels
     */
    public AbstractWorkspace(double width, double height, PanelManager manager) throws IOException {
        this.manager = manager;
        positions = positionList();
        panelPositions = new HashMap<>();
        visibilities = new HashMap<>();
        defaultPosition = positions.getPosition(defaultPosition());
        tabManager = new TabManager(positions);
        tabManager.setOnTabClose(e -> visibilities.put(((VoogaTab)e.getTarget()).getPanelName(), false));
        setupWorkspace(width, height);
    }

    @Override
    public void activate() throws IOException{
        loadFile();
        subscribeToChannels();
    }

    @Override
    public void deactivate() throws IOException {
        disconnect();

        panelPositions.forEach((name, pos) -> properties.setProperty(name, pos.toString()));
        visibilities.forEach((name, bool) -> properties.setProperty(
                String.format(PropertiesReader.value(DATA, VISIBILITY_TAG), name),
                Boolean.toString(bool))
        );

        positions.clear();

        saveToFile(new File(String.format(PropertiesReader.value(DATA, FILEPATH), this.getClass().getSimpleName())), properties);
    }

    /**
     * Loads the workspace settings from a file, creating it if it does not exist.
     * @throws IOException if the file cannot be read
     */
    protected void loadFile() throws IOException {
        File file = new File(String.format(PropertiesReader.value(DATA, FILEPATH), this.getClass().getSimpleName()));
        if(file.exists()){
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
            for(String panel : manager.getPanels()){
                Position position = positions.getPosition(properties.getProperty(panel));
                if(position != null){
                    panelPositions.put(panel, position);
                    visibilities.put(panel,
                            Boolean.parseBoolean(
                                    properties.getProperty(
                                            String.format(PropertiesReader.value(DATA, VISIBILITY_TAG), panel))));
                } else {
                    properties.setProperty(panel, defaultPosition.toString());
                    panelPositions.put(panel, defaultPosition);
                    visibilities.put(panel, defaultVisibility);
                }
                loadPanel(panel);
            }
        } else {
            createFile(file);
            loadFile();
        }
    }

    /**
     * Provides the list of positions to the AbstractWorkspace for management.
     * @return the workspace's positions
     */
    protected abstract Positions positionList();

    /**
     * Returns the name of the default position for panels on the screen, if not specified
     * @return the default position
     */
    protected abstract String defaultPosition();

    /**
     * Prepares the workspace for display, given the width and height.
     * @param width width of the workspace
     * @param height height of the workspace
     */
    protected abstract void setupWorkspace(double width, double height);

    /**
     * Finds a property's value and converts it to a double.
     * @param key the property
     * @return the double value
     */
    protected double getDoubleValue(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }

    /**
     * Saves the given workspace state to the settings file for the workspace. Used to save the current workspace data.
     * @param file the workspace file
     * @param properties the state of the workspace to save
     * @throws IOException if the file cannot be written to
     */
    protected void saveToFile(File file, Properties properties) throws IOException{
        saveState();
        OutputStream output = new FileOutputStream(file);
        properties.store(output, String.format(PropertiesReader.value(DATA, DATAHEADER), getClass().getSimpleName()));
        output.close();
    }

    /**
     * Saves the state of the current workspace. Also used to save the defaults of a workspace for a new user.
     */
    protected abstract void saveState();

    private Tab newTab(String panel) {
        Tab tab = tabManager.newTab(panel);
        tab.setContent(manager.getPanelDisplay(panel));
        return tab;
    }

    private void createFile(File location) throws IOException{
        for(String panel : manager.getPanels()){
            properties.setProperty(panel, defaultPosition.toString());
            properties.setProperty(String.format(PropertiesReader.value(DATA, VISIBILITY_TAG), panel), Boolean.toString(defaultVisibility));
        }
        saveToFile(location, properties);
    }

    private void toggle(String panel){
        visibilities.put(panel, !visibilities.get(panel));
        if(!visibilities.get(panel)) tabManager.remove(panel);
        else {
            loadPanel(panel);
        }
    }

    private boolean loadPanel(String panel) {
        Position position = panelPositions.get(panel);
        if(position != null){
            if(visibilities.get(panel)) putPanel(panel);
        } else return false;
        return true;
    }

    private void putPanel(String panel){
        panelPositions.get(panel).addTab(newTab(panel));
    }

    private void movePanel(String panel, TabPane newPane){
        Position to = positions.getPosition(newPane);
        panelPositions.put(panel, to);
    }

    private void subscribeToChannels() {
        PubSub.getInstance().subscribe(PANEL_TOGGLE, panelToggle);
        PubSub.getInstance().subscribeSync(LOAD_TAB, loadTab);
        PubSub.getInstance().subscribe(PUT_TAB, putTab);
    }

    private void disconnect() {
        PubSub.getInstance().unsubscribe(PANEL_TOGGLE, panelToggle);
        PubSub.getInstance().unsubscribeSync(LOAD_TAB);
        PubSub.getInstance().unsubscribe(PUT_TAB, putTab);
    }
}