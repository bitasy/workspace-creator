package authoring.panels.reserved;

import authoring.Panel;
import authoring.IPanelController;
import authoring.Workspace;
import authoring.WorkspaceManager;
import authoring.menuactions.SaveAction;
import authoring.panels.PanelManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import util.Loader;
import util.MenuReader;
import util.pubsub.PubSub;
import util.pubsub.messages.ThemeMessage;
import util.PropertiesReader;
import util.pubsub.messages.WorkspaceChange;

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

    private HBox hbar;
    private MenuBar bar;
    private IPanelController controller;
    private Set<String> workspaces;
    private Set<String> panels;
    private Set<String> themes;

    private String menuPath = PropertiesReader.value("screenlayout","menubarpath");
    private MenuReader reader;
    private double height = Double.parseDouble(PropertiesReader.value("screenlayout","menubarheight"));
    private String style = PropertiesReader.value("screenlayout","menubarstyle");
    private Color textColor = Color.web(PropertiesReader.value("screenlayout","menubartextcolor"));
    private double spacing = Double.parseDouble(PropertiesReader.value("screenlayout","menubarspacing"));
    private Color color = Color.web(PropertiesReader.value("screenlayout","menubarcolor"));
    private Color onHoverColor = Color.web(PropertiesReader.value("screenlayout","menubaronhovercolor"));

    public MenuBarPanel(Set<String> workspaces, Set<String> panels) throws FileNotFoundException {
        bar = new MenuBar();
        bar.getStyleClass().add("menuBar");
        this.workspaces = workspaces;
        this.panels = panels;
        this.themes = createThemeList();

        reader = new MenuReader(menuPath, this, getViewList());
        bar.getMenus().addAll(reader.getMenus());
        Pane file = getOption("File"); //TODO: Style the menu bar
        Pane view = getOption("View");

        hbar.getChildren().addAll(file, view);
    }

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
    private Set<String> createThemeList() throws FileNotFoundException {
        String themePath = PropertiesReader.value("workspacedata", "csspath");
        String[] allThemes = Loader.validFiles(themePath,"css");
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
        viewMap.put("panels", getPanelList());
        viewMap.put("themes", getThemeList());
        viewMap.put("workspaces", getWorkspaceList());
        return viewMap;
    }

    private MenuItem[] getWorkspaceList() {
        List<MenuItem> workspaceTabs = new ArrayList<>();
        for(String space : workspaces){
            MenuItem item = new MenuItem(space);
            item.setOnAction(e -> handleWorkspace(item));
            workspaceTabs.add(item);
        }
        return workspaceTabs.toArray(new MenuItem[workspaceTabs.size()]);
    }

    private MenuItem[] getPanelList() {
        List<MenuItem> panelTabs = new ArrayList<>();
        for(String space : panels){
            MenuItem item = new MenuItem(space);
            item.setOnAction(e -> handlePanel(item));
            panelTabs.add(item);
        }
        return panelTabs.toArray(new MenuItem[panelTabs.size()]);
    }

    private MenuItem[] getThemeList() {
        List<MenuItem> themeOptions = new ArrayList<>();
        for(String theme : themes){
            MenuItem item = new MenuItem(theme);
            item.setOnAction(e -> handleTheme(item));
            themeOptions.add(item);
        }
        return themeOptions.toArray(new MenuItem[themeOptions.size()]);
    }

    @Override
    public Region getRegion(){
        return bar;
    }

    /**
     * Creates an option to be put in the Menu Bar.
     * @param text the text for the option
     * @return a Pane that represents the field for the option
     */
    private Pane getOption(String text) {
        StackPane option = new StackPane();

        Text textbutton = new Text(text);
        textbutton.setFill(textColor);

        Rectangle box = new Rectangle();
        box.setWidth(textbutton.minWidth(height) + spacing);
        box.setHeight(height);
        box.setFill(color);

        option.setOnMouseEntered(event -> box.setFill(onHoverColor));
        option.setOnMouseExited(event -> box.setFill(color));

        option.getChildren().addAll(box, textbutton);
        return option;
    }

    @Override
    public void setController(IPanelController controller) {
        this.controller = controller;
    }

    @Override
    public String title(){
        return "Menu";
    }

    public void setupItem(MenuItem newItem, String strategy) { //TODO: How is styling attached? put string somewhere else
        if(strategy.equals("Save")) newItem.setOnAction(e -> new SaveAction(controller).execute());
    }

    public void handleTheme(MenuItem item) {
        PubSub.getInstance().publish("THEME_MESSAGE", new ThemeMessage(item.getText()+".css"));
    }


    private void handlePanel(MenuItem item) {
        PubSub.getInstance().publish("PANEL_TOGGLE", new WorkspaceChange(item.getText()));
    }

    private void handleWorkspace(MenuItem item) {
        PubSub.getInstance().publish("WORKSPACE_CHANGE", new WorkspaceChange(item.getText()));
    }
}