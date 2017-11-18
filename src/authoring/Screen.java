package authoring;

import authoring.panels.PanelManager;
import authoring.panels.reserved.CameraPanel;
import authoring.panels.reserved.MenuBarPanel;
import authoring.workspaces.LeftCameraWorkspace;
import authoring.workspaces.Workspace;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.ErrorDisplay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Screen represents the container of the various areas of the authoring environment user interface. Areas can contain multiple Panels, and each Panel specifies what area it should be viewed in.
 * @author Brian Nieves
 */
public class Screen {

    private VBox root;
    private Workspace workspace;

    private PanelController controller;

    private ResourceBundle properties = ResourceBundle.getBundle("screenlayout"); //If this doesn't work, mark the data folder as a resource folder
    private ResourceBundle panelStrings = ResourceBundle.getBundle("screenerrors");
    private ErrorDisplay errorMessage = new ErrorDisplay(panelStrings.getString("errortitle"));

    public Screen(Stage stage){
        root = new VBox();
        controller = new PanelController();


        //SceenBounds Code courtesy of <a href = "http://www.java2s.com/Code/Java/JavaFX/GetScreensize.htm">java2s</a>
        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        setupStage(stage, primaryScreenBounds);
        int width = (int) primaryScreenBounds.getWidth();
        int height = (int) primaryScreenBounds.getHeight();
        setupScreen(width);

        workspace = null;
        try {
            workspace = new LeftCameraWorkspace(width, height, new PanelManager(controller, errorMessage));
        } catch (FileNotFoundException e) {
            errorMessage.addMessage(panelStrings.getString("nopath"));
            quitOnError();
        } catch (IOException e){
            errorMessage.addMessage(String.format(panelStrings.getString("IOerror"), e.getMessage()));
            quitOnError();
        }

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void quitOnError(){
        errorMessage.displayError();
        Platform.exit();
    }

    private void setupScreen(int width) {
        double cameraWidthRatio = getDoubleValue("camerawidthscale");
        double cameraWidth = width * cameraWidthRatio;
        double cameraHeight = cameraWidth * getDoubleValue("cameraheighttowidthratio");

        CameraPanel camera = new CameraPanel(cameraWidth, cameraHeight);
        camera.setController(controller);
        MenuBarPanel bar = new MenuBarPanel();
        bar.setController(controller);

        Region cameraRegion = camera.getRegion();
        cameraRegion.setMinWidth(0);
        cameraRegion.setMinHeight(0);
        workspace.addCameraPanel(cameraRegion);
        root.getChildren().addAll(bar.getRegion(), workspace.getWorkspace());
    }

    private void setupStage(Stage stage, Rectangle2D primaryScreenBounds) {
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    public void save(){
        //TODO: allow workspace, engine to save to local/database
    }

    private double getDoubleValue(String key) {
        return Double.parseDouble(properties.getString(key));
    }

}