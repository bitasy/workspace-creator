package authoring;

import authoring.panels.PanelManager;
import authoring.panels.reserved.CameraPanel;
import authoring.panels.reserved.MenuBarPanel;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.VoogaPeaches;
import util.ErrorDisplay;
import util.PropertiesReader;
import util.pubsub.PubSub;
import util.pubsub.messages.ThemeMessage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Screen contains the display of the authoring environment. It has a Menu Bar and a Workspace. The workspace is highly customizable, and many different workspaces can be created to suit the user's preference in the display of the various Panels on the screen. The Screen also handles any errors that arise from loading the panels and workspaces. Most errors are non-fatal and result in failure to load a single Panel or Workspace, but if the Screen cannot find the location of any Panels or Workspaces, the program will exit.
 * @author Brian Nieves
 * @author Kelly Zhang
 */
public class Screen {

    private VBox root;

    private PanelController controller;
    private PanelManager panelManager;
    private WorkspaceManager workspaceManager;
    private ErrorDisplay errorMessage;

    /**
     * Creates a new Screen and adds it to the stage after population. The size of the Screen is determined by the user's computer screen size.
     * @param stage the stage to add the Screen to
     */
    public Screen(Stage stage){
        root = new VBox();
        controller = new PanelController();
        errorMessage = new ErrorDisplay(PropertiesReader.value("reflect","errortitle"));


        //SceenBounds Code courtesy of <a href = "http://www.java2s.com/Code/Java/JavaFX/GetScreensize.htm">java2s</a>
        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        setupStage(stage, primaryScreenBounds);
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();

        try{
            panelManager = new PanelManager(controller, errorMessage);
            setupScreen(width, height);
        } catch (FileNotFoundException e) {
            errorMessage.addMessage(PropertiesReader.value("reflect","nopath"));
            quitOnError();
        } catch (IOException e){
            errorMessage.addMessage(String.format(PropertiesReader.value("reflect","IOerror"), e.getMessage()));
            quitOnError();
        }

        Scene scene = new Scene(root, width, height);
        updateTheme();

        stage.setScene(scene);
        stage.show();

        errorMessage.displayError();
    }

    private void updateTheme() {
        root.getStylesheets().add(VoogaPeaches.getUser().getThemeName()); //update from database
        PubSub.getInstance().subscribe(
                "THEME_MESSAGE",
                (message) -> {
                    if (root.getStylesheets().size() >= 1) {
                        root.getStylesheets().remove(0);
                    }
                    root.getStylesheets().add(((ThemeMessage) message).readMessage());
                }
        );
        //myUser.setTheme();
        //TODO: on screen close update the database with the theme file name string
    }

    /**
     * Displays the accumulated errors and terminates the program due to a fatal error.
     */
    private void quitOnError(){
        errorMessage.displayError();
        Platform.exit();
    }

    /**
     * Sets up the Screen by creating the Menu Bar and the Camera. Then adds the camera's display Region to the workspace and adds all the elements to the Screen.
     * @param width the width of the Screen, used to scale the camera appropriately.
     */
    private void setupScreen(double width, double height) throws IOException {
        double cameraWidthRatio = getDoubleValue("camerawidthscale");
        double cameraWidth = width * cameraWidthRatio;
        double cameraHeight = cameraWidth * getDoubleValue("cameraheighttowidthratio");

        CameraPanel camera = new CameraPanel(cameraWidth, cameraHeight);
        camera.setController(controller);

        Pane workspaceArea = new Pane();
        workspaceArea.setMinWidth(width);
        workspaceArea.setMinHeight(height);


        workspaceManager = new WorkspaceManager(workspaceArea, panelManager, camera);
        MenuBarPanel bar = new MenuBarPanel(workspaceManager.getWorkspaces(), panelManager.getPanels());
        bar.setController(controller);

        Region cameraRegion = camera.getRegion();
        cameraRegion.setMinWidth(0);
        cameraRegion.setMinHeight(0);
        root.getChildren().addAll(bar.getRegion(), workspaceArea);
    }

    /**
     * Sets the stage to be maximized.
     * @param stage the stage
     * @param primaryScreenBounds the computer screen's bounds
     */
    private void setupStage(Stage stage, Rectangle2D primaryScreenBounds) {
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    public void save(){
        try {
            workspaceManager.saveWorkspaces();
        } catch (IOException e){
            errorMessage.addMessage(String.format(PropertiesReader.value("reflect","IOerror"), e.getMessage()));
            errorMessage.displayError();
        }
    }

    /**
     * Returns a double from the screenlayout properties file.
     * @param key the property
     * @return the property's double value
     */
    private double getDoubleValue(String key) {
        return Double.parseDouble(PropertiesReader.value("screenlayout", key));
    }

}