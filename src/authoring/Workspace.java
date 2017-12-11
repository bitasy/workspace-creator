package authoring;

import javafx.scene.layout.Region;

import java.io.IOException;

/**
 * A Workspace defines the layout of all tabbable Panels as well as the CameraPanel on the screen. This includes the location and management of the various areas that contain Panel Regions. Every full implementation workspace has a properties file with keys of the panels and values of where in the workspace they are. This is managed by the Workspace, but can be directly edited if the user desired. If the file does not exist, it should be created with defaults specified by the Workspace. The Workspace uses a PanelManager to get a list of all existing Panels and to display each one correctly.
 * @author Brian Nieves
 */
public interface Workspace {
    /**
     * Returns the workspace to be displayed on the Screen.
     * @return the workspace Region
     */
    Region getWorkspace();

    /**
     * Adds a region that displays the camera to the workspace. This is not a tabbable Panel and thus must be displayed in the workspace at all times.
     * @param cameraPanel the cameraPanel's region
     */
    void addCameraPanel(Region cameraPanel);

    /**
     * Loads the workspace settings from a file and subscribes it to tab changes.
     * @throws IOException if the workspace settings cannot be found or read
     */
    void activate() throws IOException;

    /**
     * Saves the workspace settings to a file and freezes changes it to.
     * @throws IOException if the workspace settings cannot be found or written to
     */
    void deactivate() throws IOException;
}