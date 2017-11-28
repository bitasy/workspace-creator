package authoring.panels.reserved;

import authoring.Panel;
import authoring.IPanelController;
import authoring.panels.PanelManager;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import util.MenuReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The Menu Bar is displayed at the top of the authoring environment and contains the buttons for options related to the environment. This includes saving and loading workspaces, as well as opening panels for viewing within the workspace.
 * @author Brian Nieves
 */
public class MenuBarPanel implements Panel {

    private HBox hbar;
    private MenuBar bar;
    private IPanelController controller;

    private ResourceBundle properties = ResourceBundle.getBundle("screenlayout");
    private String path = properties.getString("menubarpath");
    private double height = Double.parseDouble(properties.getString("menubarheight"));
    private String style = properties.getString("menubarstyle");
    private Color textColor = Color.web(properties.getString("menubartextcolor"));
    private double spacing = Double.parseDouble(properties.getString("menubarspacing"));
    private Color color = Color.web(properties.getString("menubarcolor"));
    private Color onHoverColor = Color.web(properties.getString("menubaronhovercolor"));

    public MenuBarPanel(PanelManager panelManager){
        hbar = new HBox();
        bar = new MenuBar();

        MenuReader reader = new MenuReader(path, this, getPanelList(panelManager));
        bar.getMenus().addAll(reader.getMenus());

        hbar.setPrefHeight(height);
        hbar.setStyle(style);

        Pane file = getOption("File"); //TODO: Style the menu bar and remove hbar
        Pane view = getOption("View");

        hbar.getChildren().addAll(file, view);
    }

    private Map<String, MenuItem[]> getPanelList(PanelManager panelManager) {
        String[] panels = panelManager.getPanels();
        MenuItem[] panelitems = new MenuItem[panels.length];
        for(int i = 0; i < panels.length; i++){
            panelitems[i] = new MenuItem(panels[i]);
        }
        Map<String, MenuItem[]> panelMap = new HashMap<>();
        panelMap.put("panels", panelitems);


        MenuItem[] themes = new MenuItem[1];
        MenuItem testItem = new MenuItem("test");
        setupItem(testItem, "themes");
        themes[0] = testItem;
        panelMap.put("themes", themes);


        return panelMap;
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
        if (strategy.equals("themes")) {
            System.out.println(newItem.getText());
        }
    }

}
