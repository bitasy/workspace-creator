package authoring;

import authoring.panels.PanelManager;
import authoring.panels.reserved.CameraPanel;
import javafx.scene.layout.Pane;
import util.Loader;
import util.PropertiesReader;
import util.pubsub.PubSub;
import util.pubsub.messages.WorkspaceChange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WorkspaceManager {

    private Pane currentWorkspaceArea;
    private Map<String, Workspace> workspaces = new HashMap<>();
    private PanelManager panelManager;
    private CameraPanel cameraPanel;


    public WorkspaceManager(Pane workspaceArea, PanelManager panelManager, CameraPanel cameraPanel) throws IOException {
        currentWorkspaceArea = workspaceArea;
        this.panelManager = panelManager;
        this.cameraPanel = cameraPanel;

        createWorkspaces(workspaceArea.minWidthProperty().doubleValue(), workspaceArea.minHeightProperty().doubleValue());

        PubSub.getInstance().subscribe(
                PubSub.Channel.WORKSPACE_CHANGE,
                message -> switchWorkspace(((WorkspaceChange)message).readMessage()
        ));
    }

    /**
     * Returns the names of all of the workspaces that have been loaded.
     * @return a set of workspace names
     */
    public Set<String> getWorkspaces(){
        return workspaces.keySet();
    }

    /**
     * Loads all of the workspaces in the workspaces directory.
     * @param width the width of the workspace
     * @param height the height of the workspaces
     * @throws IOException if the workspaces could not be loaded or the directory does not exist
     */
    private void createWorkspaces(double width, double height) throws IOException{
        Map<String, Object> workspaces = Loader.loadObjects(
                PropertiesReader.value("reflect", "workspacepath"),
                width, height, panelManager);
        for(String space : workspaces.keySet()){
            Workspace workspace = (Workspace) workspaces.get(space);
            workspace.addCameraPanel(cameraPanel.getRegion());
            this.workspaces.put(space, workspace);
        }
        switchWorkspace(PropertiesReader.value("screenlayout", "currentworkspace"));
    }

    private void switchWorkspace(String newWorkspace){
        currentWorkspaceArea.getChildren().clear();
        currentWorkspaceArea.getChildren().add(workspaces.get(newWorkspace).getWorkspace());
    }
}
