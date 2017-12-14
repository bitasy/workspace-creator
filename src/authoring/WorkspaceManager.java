package authoring;

import authoring.panels.PanelManager;
import authoring.panels.reserved.CameraPanel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import main.VoogaPeaches;
import util.ErrorDisplay;
import util.Loader;
import util.PropertiesReader;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * WorkspaceManager loads and handles the workspaces available for use.
 * @author Brian Nieves
 */
public class WorkspaceManager {

    private static final String REFLECT = "reflect";
    private static final String WORKSPACEPATH = "workspacepath";
    private static final String WORKSPACE_CHANGE = "WORKSPACE_CHANGE";
    private static final String IO_ERROR = "IO Error";
    private static final String COULD_NOT_SWITCH_WORKSPACE_ERROR = "Couldn't switch workspace";
    private Pane currentWorkspaceArea;
    private Map<String, Workspace> workspaces = new HashMap<>();
    private PanelManager panelManager;
    private CameraPanel cameraPanel;
    private Workspace currentWorkspace;

    /**
     * Creates a new workspace manager and initializes the workspaces.
     * @author Brian Nieves
     * @param workspaceArea the area of the screen to display the current workspace in
     * @param panelManager the manager of the panels to display in the workspace
     * @param cameraPanel the camera panel to add to the workspaces manually
     * @throws IOException if the workspace directory cannot be found
     */
    public WorkspaceManager(Pane workspaceArea, PanelManager panelManager, CameraPanel cameraPanel) throws IOException {
        currentWorkspaceArea = workspaceArea;
        this.panelManager = panelManager;
        this.cameraPanel = cameraPanel;

        createWorkspaces(workspaceArea.minWidthProperty().doubleValue(), workspaceArea.minHeightProperty().doubleValue());

        PubSub.getInstance().subscribe(
                WORKSPACE_CHANGE,
                message -> {
                    try {
                        switchWorkspace(((StringMessage)message).readMessage()
                );
                    } catch (IOException e) {
                        new ErrorDisplay(IO_ERROR, COULD_NOT_SWITCH_WORKSPACE_ERROR);//TODO: put this in a properties file
                    }
                });
    }

    /**
     * Returns the names of all of the workspaces that have been loaded.
     * @return a set of workspace names
     */
    public Set<String> getWorkspaces(){
        return workspaces.keySet();
    }

    /**
     * Saves the currently active workspace.
     */
    public void saveWorkspace() throws IOException {
        currentWorkspace.deactivate();
    }

    /**
     * Loads all of the workspaces in the workspaces directory.
     * @param width the width of the workspace
     * @param height the height of the workspaces
     * @throws IOException if the workspaces could not be loaded or the directory does not exist
     */
    private void createWorkspaces(double width, double height) throws IOException{
        Map<String, Object> workspaces = Loader.loadObjects(
                PropertiesReader.value(REFLECT, WORKSPACEPATH),
                width, height, panelManager);
        for(String space : workspaces.keySet()){
            Workspace workspace = (Workspace) workspaces.get(space);
            this.workspaces.put(space, workspace);
        }
        switchWorkspace(VoogaPeaches.getUser().getWorkspaceName());
    }

    /**
     * Changes the workspace currently being viewed on the screen.
     * @param newWorkspace the name of the new workspace
     */
    private void switchWorkspace(String newWorkspace) throws IOException {
        Workspace workspace = workspaces.get(newWorkspace);
        if(currentWorkspace != null){
            if(workspace == currentWorkspace) return;
            currentWorkspace.deactivate();
        }
        currentWorkspace = workspace;
        workspace.activate();
        workspace.addCameraPanel(cameraPanel.getRegion());
        currentWorkspaceArea.getChildren().clear();
        Region workRegion = workspace.getWorkspace();
        workRegion.setMaxHeight(currentWorkspaceArea.getMaxHeight());
        currentWorkspaceArea.getChildren().add(workRegion);
    }
}