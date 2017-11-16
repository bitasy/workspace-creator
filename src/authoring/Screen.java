package authoring;

<<<<<<< HEAD
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
=======
import authoring.panels.CameraPanel;
import authoring.panels.MenuBarPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

>>>>>>> a99dff4e041643300ea9ab418d2bfbd70f12f6a9
import java.util.ResourceBundle;

/**
 * Screen represents the container of the various areas of the authoring environment user interface. Areas can contain multiple Panels, and each Panel specifies what area it should be viewed in.
 * @author Brian Nieves
 * @author estellehes
 */
public class Screen {

    /**
     * Constants represent the various areas of the user interface.
     * @see <a href="https://coursework.cs.duke.edu/CompSci308_2017Fall/voogasalad_programmersforpeaches/raw/master/doc/UI.png">UI Image</a>
     */
    public static final int TOP = 0;
    public static final int BOTTOM_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;
    public static final int TOP_RIGHT = 3;
    public static final int CAMERA = 4;


    private BorderPane root;
<<<<<<< HEAD
    private PanelController controller;
    private ResourceBundle properties = ResourceBundle.getBundle("screenlayout"); //If this doesn't work, mark the data folder as a resource folder
    private ResourceBundle panelStrings = ResourceBundle.getBundle("screenerrors");
    private StringBuilder errorMessage = new StringBuilder();

    /**
     * Constructs a new Screen, which in turn creates a new environment in the specified Stage. The screen's layout is defined by  a BorderPanel, and each Panel src/authoring/panels is loaded, if possible, and added to the correct area of the Screen. A tailored error message is displayed on any errors that have occured, and if the Screen cannot find the panels folder, the program exits.
=======
    private PanelController myController;
    private MenuBarPanel myMenuBar;
    private CameraPanel myCameraPanel;

    /**
     * Constructs a new Screen, which in turn creates a new environment in the specified Stage.
>>>>>>> a99dff4e041643300ea9ab418d2bfbd70f12f6a9
     * @param stage the stage that will display the screen
     */
    public Screen(Stage stage){
        int width = getIntValue("width");
        int height = getIntValue("height");
<<<<<<< HEAD

        root = new BorderPane();
        controller = new PanelController();

        TabPane bottom_left = new TabPane();
        TabPane bottom_right = new TabPane();
        TabPane top_right = new TabPane();

        setupTabs(root, bottom_left, bottom_right, top_right);

        addPanels(bottom_left, bottom_right, top_right);
=======
        int cameraWidth = getIntValue("camerawidth");
        int cameraHeight = getIntValue("cameraheight");
        int gridNum = getIntValue("camerarownum");

        root = new BorderPane();
        myMenuBar = new MenuBarPanel();
        myCameraPanel = new CameraPanel();
        myController = new PanelController(myCameraPanel); //just for testing, should not pass camera panel into controller
        
        myMenuBar.setController();
        myCameraPanel.setController();

        root.setTop(myMenuBar.getRegion());
        root.setCenter(myCameraPanel.getRegion()); //size still need to be adjusted, just for testing
>>>>>>> a99dff4e041643300ea9ab418d2bfbd70f12f6a9

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

<<<<<<< HEAD
    private void addPanels(TabPane bottom_left, TabPane bottom_right, TabPane top_right) {
        for (Panel panel : getPanels()) {
            switch (panel.getArea()){
                case TOP:
                    root.setTop(panel.getRegion());
                    break;
                case BOTTOM_LEFT:
                    makeTab(panel, bottom_left);
                    break;
                case BOTTOM_RIGHT:
                    makeTab(panel, bottom_right);
                    break;
                case TOP_RIGHT:
                    makeTab(panel, top_right);
                    break;
                case CAMERA:
                    root.setCenter(panel.getRegion());
                    break;
                default:
                    errorMessage.append(panel.title() + panelStrings.getString("noarea"));
                    break;
            }
        }
        displayError();
    }

    private int getIntValue(String key){
        return Integer.parseInt(properties.getString(key));
    }

    private void setupTabs(BorderPane root, TabPane bottom_left, TabPane bottom_right, TabPane top_right) {
        root.setBottom(bottom_left);

        VBox right = new VBox();
        right.getChildren().addAll(top_right, bottom_right);
        root.setRight(right);
    }

    private void makeTab(Panel panel, TabPane tabPane){
        Tab tab = new Tab();
        tab.setText(panel.title());
        tab.setContent(panel.getRegion());
        tabPane.getTabs().add(tab);
    }

    private List<Panel> getPanels(){
        List<Panel> panels = new ArrayList<>();
        try {
            File panelsFolder = new File(panelStrings.getString("path"));
            File[] panelFiles = panelsFolder.listFiles();
            String[] names = new String[panelFiles.length];
            for(int i = 0; i < panelFiles.length; i++){
                names[i] = panelFiles[i].getName();
            }

            String qualifier = panelStrings.getString("classqualifier");

            for(String panelName : names){
                try {
                    Class<Panel> panelClass = (Class<Panel>) Class.forName(qualifier + panelName.replace(".java", ""));
                    Constructor<Panel> panelConstructor = panelClass.getConstructor();
                    Panel panel = panelConstructor.newInstance();
                    panel.setController(controller);
                    panels.add(panel);
                } catch (ClassNotFoundException e) {
                    errorMessage.append(String.format(panelStrings.getString("nopanel"), e.getMessage()));
                } catch (NoSuchMethodException e) {
                    errorMessage.append(String.format(panelStrings.getString("noconstructor"), e.getMessage()));
                } catch (InstantiationException e) {
                    errorMessage.append(String.format(panelStrings.getString("noinstance"), e.getMessage()));
                } catch (IllegalAccessException e) {
                    errorMessage.append(String.format(panelStrings.getString("hiddenconstructor"), e.getMessage()));
                } catch (InvocationTargetException e) {
                    errorMessage.append(String.format(panelStrings.getString("noinvocation"), e.getMessage()));
                }
            }

            return panels;

        } catch (NullPointerException e){
            errorMessage.append(panelStrings.getString("nopath"));
            displayError();
            Platform.exit();
        }
        return panels;
    }

    private void displayError() {
        if(errorMessage.length() > 0) {
            Alert errors = new Alert(Alert.AlertType.ERROR);
            errors.setTitle(panelStrings.getString("errortitle"));
            errors.setContentText(errorMessage.toString());

            errors.showAndWait();
        }
    }
=======
    private int getIntValue(String key){
        return Integer.parseInt(properties.getString(key));
    }
>>>>>>> a99dff4e041643300ea9ab418d2bfbd70f12f6a9
}
