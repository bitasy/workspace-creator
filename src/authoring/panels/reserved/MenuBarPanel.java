package authoring.panels.reserved;

import authoring.Panel;
import authoring.IPanelController;
import authoring.Workspace;
import authoring.WorkspaceManager;
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
 * The Menu Bar is displayed at the top of the authoring environment and contains the buttons for options related to the environment. This includes saving and loading workspaces, as well as opening panels for viewing within the workspace.
 * @author Brian Nieves
 * @author Simran
 *
 */
public class MenuBarPanel implements Panel {

    private HBox hbar;
    private MenuBar bar;
    private IPanelController controller;
    private Set<String> panels;
    private Set<String> workspaces;

    private ResourceBundle themes = ResourceBundle.getBundle("themes");
    private String path = PropertiesReader.value("screenlayout","menubarpath");
    private double height = Double.parseDouble(PropertiesReader.value("screenlayout","menubarheight"));
    private String style = PropertiesReader.value("screenlayout","menubarstyle");
    private Color textColor = Color.web(PropertiesReader.value("screenlayout","menubartextcolor"));
    private double spacing = Double.parseDouble(PropertiesReader.value("screenlayout","menubarspacing"));
    private Color color = Color.web(PropertiesReader.value("screenlayout","menubarcolor"));
    private Color onHoverColor = Color.web(PropertiesReader.value("screenlayout","menubaronhovercolor"));

    public MenuBarPanel(Set<String> workspaces, Set<String> panels) {
        hbar = new HBox();
        bar = new MenuBar();
        this.workspaces = workspaces;
        this.panels = panels;

        MenuReader reader = new MenuReader(path, this, getViewList());
        bar.getMenus().addAll(reader.getMenus());

        hbar.setPrefHeight(this.height);
        hbar.setStyle(style);

        Pane file = getOption("File"); //TODO: Style the menu bar and remove hbar
        Pane view = getOption("View");

        hbar.getChildren().addAll(file, view);
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

    private MenuItem[] getThemeList() {
        List<String> keys = Collections.list(themes.getKeys());
        MenuItem[] themeItems = new MenuItem[keys.size()];
        for(int i = 0; i < keys.size(); i++){
            MenuItem item = new MenuItem(keys.get(i));
            setupTheme(item);
            themeItems[i] = item;
        }
        return themeItems;
    }

    private MenuItem[] getPanelList() {
        List<MenuItem> panelTabs = new ArrayList<>();
        for(String space : panels){
            MenuItem item = new MenuItem(space);
            item.setOnAction(e -> handlePanel(item.getText()));
            panelTabs.add(item);
        }
        return panelTabs.toArray(new MenuItem[panelTabs.size()]);
    }

    private MenuItem[] getWorkspaceList() {
        List<MenuItem> workspaceTabs = new ArrayList<>();
        for(String space : workspaces){
            MenuItem item = new MenuItem(space);
            item.setOnAction(e -> handleWorkspace(item.getText()));
            workspaceTabs.add(item);
        }
        return workspaceTabs.toArray(new MenuItem[workspaceTabs.size()]);
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

    public void setupItem(MenuItem newItem, String strategy) {
        //TODO: Attach onAction to controller actions, style stuff
    }

    private void setupTheme(MenuItem item) {
        item.setOnAction(event -> {
            PubSub pubsub = PubSub.getInstance();
            pubsub.publish(PubSub.Channel.THEME_MESSAGE, new ThemeMessage(themes.getString(item.getText())));
        });
    }

    private void handlePanel(String text){
        PubSub.getInstance().publish(PubSub.Channel.PANEL_TOGGLE, new WorkspaceChange(text));
    }

    private void handleWorkspace(String text){
        PubSub.getInstance().publish(PubSub.Channel.WORKSPACE_CHANGE, new WorkspaceChange(text));
    }
}
