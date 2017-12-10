package authoring.panels.reserved;

import authoring.Panel;
import authoring.PanelController;
import authoring.menuactions.SaveAction;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;
import util.Loader;
import util.MenuReader;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;
import util.PropertiesReader;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * The Menu Bar is displayed at the top of the authoring environment and contains the menuactions for options related to the environment. This includes saving and loading workspaces, as well as opening panels for viewing within the workspace.
 * @author Brian Nieves
 * @author Simran
 * @author Kelly Zhang
 *
 */
public class MenuBarPanel implements Panel {

    private static final String SCREENLAYOUT = "screenlayout";
    private static final String MENUBARPATH = "menubarpath";
    private static final String MENU_BAR = "menu-bar";
    private static final String WORKSPACEDATA = "workspacedata";
    private static final String CSSPATH = "csspath";
    private static final String CSS = "css";
    public static final String PANELS = "panels";
    public static final String THEMES = "themes";
    public static final String WORKSPACES = "workspaces";
    public static final String MENU_ITEM = "menu-item";
    public static final String MENU = "Menu";
    public static final String SAVE = "Save";
    public static final String THEME_MESSAGE = "THEME_MESSAGE";
    public static final String CSS_EXTENSION = ".css";
    public static final String PANEL_TOGGLE = "PANEL_TOGGLE";
    public static final String WORKSPACE_CHANGE = "WORKSPACE_CHANGE";

    //TODO: Do you care about the lines within the menubar sections?
    private MenuBar bar;
    private PanelController controller;
    private Set<String> workspaces;
    private Set<String> panels;
    private Set<String> themes;

    private String menuPath = PropertiesReader.value(SCREENLAYOUT, MENUBARPATH);
    private MenuReader reader;

    public MenuBarPanel(Set<String> workspaces, Set<String> panels) throws FileNotFoundException {
        bar = new MenuBar();
        this.workspaces = workspaces;
        this.panels = panels;
        this.themes = createThemeList();

        reader = new MenuReader(menuPath, this, getViewList());
        bar.getMenus().addAll(reader.getMenus());

        bar.getStyleClass().add(MENU_BAR);
    }

    /**
     * Programatically searches for the css files within the resources to find those that contain the stylings for the themes
     * @return Set<String> that are the default themes provided
     * @throws FileNotFoundException
     */
    private Set<String> createThemeList() throws FileNotFoundException {
        String themePath = PropertiesReader.value(WORKSPACEDATA, CSSPATH);
        String[] allThemes = Loader.validFiles(themePath, CSS);
        Set<String> myThemes = new HashSet<String>(Arrays.asList(allThemes));
        return myThemes;
    }

    /**
     * This returns the map that is used to create the Views section in the menu. It currently draws information for
     * the panels (from panelManager) and themes (resource file)
     *
     * @return
     */
    private Map<String, MenuItem[]> getViewList() {
        Map<String, MenuItem[]> viewMap = new HashMap<>();
        viewMap.put(PANELS, getPanelList());
        viewMap.put(THEMES, getThemeList());
        viewMap.put(WORKSPACES, getWorkspaceList());
        return viewMap;
    }

    private MenuItem[] getWorkspaceList() {
        List<MenuItem> workspaceTabs = new ArrayList<>();
        for(String space : workspaces){
            MenuItem item = new MenuItem(space);
            item.setOnAction(e -> handleWorkspace(item));
            item.getStyleClass().add(MENU_ITEM);
            workspaceTabs.add(item);
        }
        return workspaceTabs.toArray(new MenuItem[workspaceTabs.size()]);
    }

    private MenuItem[] getPanelList() {
        List<MenuItem> panelTabs = new ArrayList<>();
        for(String space : panels){
            MenuItem item = new MenuItem(space);
            item.getStyleClass().add(MENU_ITEM);
            item.setOnAction(e -> handlePanel(item));
            panelTabs.add(item);
        }
        return panelTabs.toArray(new MenuItem[panelTabs.size()]);
    }

    /**
     * Uses the set of themes to create the submenu of the menubar where the user can pick their theme
     * @return MenuItem[] the list of themes converted into menuitems
     */
    private MenuItem[] getThemeList() {
        List<MenuItem> themeOptions = new ArrayList<>();
        for(String theme : themes){
            MenuItem item = new MenuItem(theme);
            item.getStyleClass().add(MENU_ITEM);
            item.setOnAction(e -> handleTheme(item));
            themeOptions.add(item);
        }
        return themeOptions.toArray(new MenuItem[themeOptions.size()]);
    }

    @Override
    public Region getRegion(){
        return bar;
    }

    @Override
    public void setController(PanelController controller) {
        this.controller = controller;
    }

    @Override
    public String title(){
        return MENU;
    }

    public void setupItem(MenuItem newItem, String strategy) { //TODO: How is styling attached? put string somewhere else
        if(strategy.equals(SAVE)) newItem.setOnAction(e -> new SaveAction(controller).execute());
    }

    /**
     * Uses the menuitem selected to create the string associated with the css file with the stylings and publishes that to pubsub
     * @param item the menuitem that is selected
     */
    public void handleTheme(MenuItem item) {
        PubSub.getInstance().publish(THEME_MESSAGE, new StringMessage(item.getText() + CSS_EXTENSION));
    }

    /**
     * Uses the menuitem selected to communicate with pubsub about the panel activity
     * @param item the menuitem that is selected
     */
    private void handlePanel(MenuItem item) {
        PubSub.getInstance().publish(PANEL_TOGGLE, new StringMessage(item.getText()));
    }

    /**
     * Uses the menuitem selected to communicate with pubsub about the workspace active
     * @param item the menuitem that is selected
     */
    private void handleWorkspace(MenuItem item) {
        PubSub.getInstance().publish(WORKSPACE_CHANGE, new StringMessage(item.getText()));
    }
}