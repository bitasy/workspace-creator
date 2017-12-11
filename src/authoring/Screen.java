package authoring;

import authoring.panels.PanelManager;
import authoring.panels.reserved.CameraPanel;
import authoring.panels.reserved.MenuBarPanel;
import database.User;
import database.firebase.DatabaseConnector;
import database.jsonhelpers.JSONHelper;
import engine.entities.Entity;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.VoogaPeaches;
import org.json.JSONObject;
import util.ErrorDisplay;
import util.PropertiesReader;
import util.exceptions.ObjectIdNotFoundException;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Screen contains the display of the authoring environment. It has a Menu Bar and a Workspace. The workspace is highly customizable, and many different workspaces can be created to suit the user's preference in the display of the various Panels on the screen. The Screen also handles any errors that arise from loading the panels and workspaces. Most errors are non-fatal and result in failure to load a single Panel or Workspace, but if the Screen cannot find the location of any Panels or Workspaces, the program will exit.
 * @author Brian Nieves
 * @author Kelly Zhang
 */
public class Screen {

    private static final String REFLECT = "reflect";
    private static final String ERROR_TITLE = "errortitle";
    private static final String NO_PATH = "nopath";
    private static final String IO_ERROR = "IOerror";
    private static final String THEME_MESSAGE = "THEME_MESSAGE";
    private static final String CAMERA_WIDTH_SCALE = "camerawidthscale";
    private static final String CAMERA_HEIGHT_TO_WIDTH_RATIO = "cameraheighttowidthratio";
    private static final int CAMERA_MIN_WIDTH = 0;
    private static final int CAMERA_MIN_HEIGHT = 0;
    private static final String SCREENLAYOUT = "screenlayout";
    private VBox root;

    private PanelController controller;
    private PanelManager panelManager;
    private WorkspaceManager workspaceManager;
    private ErrorDisplay errorMessage;
    private CameraPanel camera;

    /**
     * Creates a new Screen and adds it to the stage after population. The size of the Screen is determined by the user's computer screen size.
     * @param stage the stage to add the Screen to
     */
    public Screen(Stage stage){
        root = new VBox();
        controller = new PanelController();
        errorMessage = new ErrorDisplay(PropertiesReader.value(REFLECT, ERROR_TITLE));

        //SceenBounds Code courtesy of <a href = "http://www.java2s.com/Code/Java/JavaFX/GetScreensize.htm">java2s</a>
        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        setupStage(stage, primaryScreenBounds);
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();

        try{
            panelManager = new PanelManager(controller, errorMessage);
            setupScreen(width, height);
        } catch (FileNotFoundException e) {
            errorMessage.addMessage(PropertiesReader.value(REFLECT, NO_PATH));
            quitOnError();
        } catch (IOException e){
            errorMessage.addMessage(String.format(PropertiesReader.value(REFLECT, IO_ERROR), e.getMessage()));
            quitOnError();
        }

        Scene scene = new Scene(root, width, height);
        updateTheme();

        stage.setScene(scene);
        stage.show();

        errorMessage.displayError();
    }

    /**
     * sets the initial theme as the user's preference (or the default if a new user), also subscribes to pubsub to allow for updating across all screens for the user's theme
     */
    private void updateTheme() {
        root.getStylesheets().add(VoogaPeaches.getUser().getThemeName()); //update from database
        PubSub.getInstance().subscribe(
                THEME_MESSAGE,
                (message) -> {
                    if (root.getStylesheets().size() >= 1) {
                        root.getStylesheets().remove(0);
                    }
                    String newTheme = ((StringMessage) message).readMessage();
                    root.getStylesheets().add(newTheme);
                    VoogaPeaches.getUser().setTheme(newTheme);
                }
        );
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
        double cameraWidthRatio = getDoubleValue(CAMERA_WIDTH_SCALE);
        double cameraWidth = width * cameraWidthRatio;
        double cameraHeight = cameraWidth * getDoubleValue(CAMERA_HEIGHT_TO_WIDTH_RATIO);

        camera = new CameraPanel(cameraWidth, cameraHeight);
        camera.setController(controller);

        Pane workspaceArea = new Pane();
        workspaceArea.setMinWidth(width);
        workspaceArea.setMaxWidth(width);
        workspaceArea.setMinHeight(height);
        workspaceArea.setMaxHeight(height);


        workspaceManager = new WorkspaceManager(workspaceArea, panelManager, camera);
        MenuBarPanel bar = new MenuBarPanel(workspaceManager.getWorkspaces(), panelManager.getPanels());
        bar.setController(controller);

        Region cameraRegion = camera.getRegion();
        cameraRegion.setMinWidth(CAMERA_MIN_WIDTH);
        cameraRegion.setMinHeight(CAMERA_MIN_HEIGHT);
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

    /**
     * saves the workspace information to their files
     */
    public void save(){
        try {
            workspaceManager.saveWorkspaces();
            DatabaseConnector<User> db = new DatabaseConnector<>(User.class);
            db.addToDatabase(VoogaPeaches.getUser());
            // Have to force a sleep to wait for data to finish sending, but
            // with actual project this shouldn't be a problem
            Thread.sleep(1000);//TODO replace with PauseTransition if possible
        } catch (IOException e){
            errorMessage.addMessage(String.format(PropertiesReader.value(REFLECT,IO_ERROR), e.getMessage()));
            errorMessage.displayError();
        } catch (ObjectIdNotFoundException e) {
            System.out.println("problem with saving!");
        } catch (InterruptedException e) {
            System.out.println("problem with saving!");
        }
    }

    public void load(Entity root) {
        System.out.println("In screen: " + root.UIDforObject());
        controller.load(root);
    }

    /**
     * Returns a double from the screenlayout properties file.
     * @param key the property
     * @return the property's double value
     */
    private double getDoubleValue(String key) {
        return Double.parseDouble(PropertiesReader.value(SCREENLAYOUT, key));
    }
}