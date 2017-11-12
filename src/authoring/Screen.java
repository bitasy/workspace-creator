package authoring;

import authoring.panels.MenuBarPanel;
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
import java.util.ResourceBundle;

/**
 * Screen represents the container of the various areas of the authoring environment user interface. Areas can contain multiple Panels, and each Panel specifies what area it should be viewed in.
 * @author Brian Nieves
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
    private ResourceBundle properties = ResourceBundle.getBundle("screenlayout"); //If this doesn't work, mark the data folder as a resource folder
    private StringBuilder errorMessage = new StringBuilder();

    /**
     * Constructs a new Screen, which in turn creates a new environment in the specified Stage. The screen's layout is defined by  a BorderPanel, and each Panel src/authoring/panels is loaded, if possible, and added to the correct area of the Screen. A tailored error message is displayed on any errors that have occured, and if the Screen cannot find the panels folder, the program exits.
     * @param stage the stage that will display the screen
     */
    public Screen(Stage stage){
        int width = getIntValue("width");
        int height = getIntValue("height");
        int cameraWidth = getIntValue("camerawidth");

        root = new BorderPane();

        TabPane bottom_left = new TabPane();
        TabPane bottom_right = new TabPane();
        TabPane top_right = new TabPane();

        setupTabs(root, bottom_left, bottom_right, top_right);

        addPanels(bottom_left, bottom_right, top_right);

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

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
                default:
                    errorMessage.append(panel.title() + " does not have a designated area and could not be loaded.\n");
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
            File panelsFolder = new File("src/authoring/panels");
            File[] panelFiles = panelsFolder.listFiles();
            String[] names = new String[panelFiles.length];
            for(int i = 0; i < panelFiles.length; i++){
                names[i] = panelFiles[i].getName();
            }

            String qualifier = "authoring.panels.";

            for(String panelName : names){
                try {
                    Class<Panel> panelClass = (Class<Panel>) Class.forName(qualifier + panelName.replace(".java", ""));
                    Constructor<Panel> panelConstructor = panelClass.getConstructor();
                    Panel panel = panelConstructor.newInstance();
                    panels.add(panel);
                } catch (ClassNotFoundException e) {
                    errorMessage.append("Could not find panel: " + e.getMessage() + "\n");
                } catch (NoSuchMethodException e) {
                    errorMessage.append("Could not find default constructor: " + e.getMessage() + "\n");
                } catch (InstantiationException e) {
                    errorMessage.append("Could not create instance: " + e.getMessage() + "\n");
                } catch (IllegalAccessException e) {
                    errorMessage.append("Could not access constructor: " + e.getMessage() + "\n");
                } catch (InvocationTargetException e) {
                    errorMessage.append("Could not invocate panel: " + e.getMessage());
                }
            }

            return panels;

        } catch (NullPointerException e){
            errorMessage.append("Path " + "src/authoring/panels" + " could not be located. \n");
            displayError();
            Platform.exit();
        }
        return panels;
    }

    private void displayError() {
        if(errorMessage.length() > 0) {
            Alert errors = new Alert(Alert.AlertType.ERROR);
            errors.setTitle("Error Loading Panel(s)");
            errors.setContentText(errorMessage.toString());

            errors.showAndWait();
        }
    }
}
