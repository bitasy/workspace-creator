package authoring;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
 * @author estellehes
 */
public class Screen {

    /**
     * Constants represent the various areas of the user interface.
     * @see <a href="https://coursework.cs.duke.edu/CompSci308_2017Fall/voogasalad_programmersforpeaches/raw/master/doc/UI.png">UI Image</a>
     */

    public static final int BOTTOM = 0;
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    public static final int BOTTOM_RIGHT = 4;
    public static final int TOP = 5;
    public static final int CAMERA = 6;


    private BorderPane root;
    private PanelController controller;
    private ResourceBundle properties = ResourceBundle.getBundle("screenlayout"); //If this doesn't work, mark the data folder as a resource folder
    private ResourceBundle panelStrings = ResourceBundle.getBundle("screenerrors");
    private StringBuilder errorMessage = new StringBuilder();

    /**
     * Constructs a new Screen, which in turn creates a new environment in the specified Stage. The screen's layout is defined by  a BorderPanel, and each Panel src/authoring/panels is loaded, if possible, and added to the correct area of the Screen. A tailored error message is displayed on any errors that have occured, and if the Screen cannot find the panels folder, the program exits.
     * @param stage the stage that will display the screen
     */
    public Screen(Stage stage){

        /*
         * Code courtesy of <a href = "http://www.java2s.com/Code/Java/JavaFX/GetScreensize.htm">java2s</a>
         */
        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        int width = (int) primaryScreenBounds.getWidth();
        int height = (int) primaryScreenBounds.getHeight();



        int cameraWidth = getIntValue("camerawidth");
        int cameraHeight = getIntValue("cameraheight");

        root = new BorderPane();
        controller = new PanelController();

        List<TabPane> tabAreas = new ArrayList<>();
        for(int i = 0; i <= TOP; i++){
            tabAreas.add(new TabPane());
        }

        setupTabs(root, tabAreas, (width - cameraWidth) / 2, cameraHeight / 2);

        addPanels(tabAreas);

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void addPanels(List<TabPane> tabAreas) {
        for (Panel panel : getPanels()) {
            switch (panel.getArea()){
                case BOTTOM:
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                    makeTab(panel, tabAreas.get(panel.getArea()));
                    break;
                case TOP:
                    root.setTop(panel.getRegion());
                    break;
                case CAMERA:
                    root.setCenter(panel.getRegion());
                    break;
                default:
                    errorMessage.append(panel.title()).append(panelStrings.getString("noarea"));
                    break;
            }
        }
        displayError();
    }

    private int getIntValue(String key){
        return Integer.parseInt(properties.getString(key));
    }

    private void setupTabs(BorderPane root, List<TabPane> tabAreas, int width, int height) {
        root.setBottom(tabAreas.get(BOTTOM));

        for(TabPane tabPane : tabAreas){
            tabPane.setPrefWidth(width);
            tabPane.setPrefHeight(height);
        }

        VBox left = new VBox();
        left.getChildren().addAll(tabAreas.get(TOP_LEFT), tabAreas.get(BOTTOM_LEFT));
        root.setLeft(left);

        VBox right = new VBox();
        right.getChildren().addAll(tabAreas.get(TOP_RIGHT), tabAreas.get(BOTTOM_RIGHT));
        root.setRight(right);
    }

    private void makeTab(Panel panel, TabPane tabPane){
        Tab tab = new DraggableTab(panel.title());
        tab.setContent(panel.getRegion());
        tab.setOnCloseRequest(event -> {
            if(tab.getTabPane().getTabs().size() == 1){
                event.consume();
            }
        });
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
}
