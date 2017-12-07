package authoring.panels;

import authoring.IPanelController;
import authoring.Panel;
import javafx.scene.layout.Region;
import util.ErrorDisplay;
import util.Loader;
import util.PropertiesReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * PanelManager creates and maintains all tabbable panels throughout the lifecycle of the program. Tabbable panels are those written correctly that are located in the package specified in the reflect properties file under the key tabpath.
 * @author Brian Nieves
 */
public class PanelManager {
    private Map<String, Panel> panels;
    //private ResourceBundle reflect = ResourceBundle.getBundle("reflect");
    private ErrorDisplay errorMessage;
    private IPanelController controller;

    /**
     * Creates a new PanelManager and loads all existing panels. Any errors in the process of loading panels are recorded in an ErrorDisplay and handled by the Screen.
     * @param controller the controller given to every panel for communication
     * @param errorMessage the screen's current ErrorDisplay object
     * @throws FileNotFoundException if the package specified by tabpath in reflect does not exist
     */
    public PanelManager(IPanelController controller, ErrorDisplay errorMessage) throws FileNotFoundException{
        this.errorMessage = errorMessage;
        panels = new HashMap<>();
        this.controller = controller;
        loadPanels();
    }

    /**
     * Returns the names of all of the panels this PanelManager is managing.
     * @return a set of panel names
     */
    public Set<String> getPanels(){
        return panels.keySet();
    }

    /**
     * Returns the javafx region that displays a panel.
     * @param panel the name of the panel, as given in getPanels()
     * @return the Region of the panel
     */
    public Region getPanelDisplay(String panel){
        return panels.get(panel).getRegion();
    }

    /**
     * Returns the title of the panel to be displayed
     * @param panel the name of the panel, as given in getPanels()
     * @return the title of the panel
     */
    public String getPanelTitle(String panel) {
        return panels.get(panel).title();
    }

    /**
     * Loads all of the tabbable panels found in the tabbable directory.
     * @throws FileNotFoundException if the directory for tabbable panels does not exist.
     */
    private void loadPanels() throws FileNotFoundException{
        Map<String, Object> panels = Loader.loadObjects(
                PropertiesReader.value("reflect", "tabpath"));
        for(String name : panels.keySet()){
            Panel panel = (Panel) panels.get(name);
            panel.setController(controller);
            this.panels.put(name, panel);
        }
    }
}
