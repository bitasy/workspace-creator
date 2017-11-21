package authoring.panels;

import authoring.IPanelDelegate;
import authoring.Panel;
import javafx.scene.layout.Region;
import util.ErrorDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * PanelManager creates and maintains all tabbable panels throughout the lifecycle of the program. Tabbable panels are those written correctly that are located in the package specified in the paneldata properties file under the key tabpath.
 * @author Brian Nieves
 */
public class PanelManager {
    private Map<String, Panel> panels;
    private ResourceBundle paneldata = ResourceBundle.getBundle("paneldata");
    private ErrorDisplay errorMessage;
    private IPanelDelegate controller;

    /**
     * Creates a new PanelManager and loads all existing panels. Any errors in the process of loading panels are recorded in an ErrorDisplay and handled by the Screen.
     * @param controller the controller given to every panel for communication
     * @param errorMessage the screen's current ErrorDisplay object
     * @throws FileNotFoundException if the package specified by tabpath in paneldata does not exist
     */
    public PanelManager(IPanelDelegate controller, ErrorDisplay errorMessage) throws FileNotFoundException{
        this.errorMessage = errorMessage;
        panels = new HashMap<>();
        this.controller = controller;
        loadPanels();
    }

    /**
     * Returns the names of all of the panels this PanelManager is managing.
     * @return a string array of panel names
     */
    public String[] getPanels(){
        return panels.keySet().toArray(new String[panels.size()]);
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
     * Looks at every file in the specified directory and adds them to this PanelManager's collection. If a file cannot be loaded as a panel, the error is recorded for the Screen object to later display.
     * @throws FileNotFoundException if the directory for tabbable panels does not exist.
     */
    private void loadPanels() throws FileNotFoundException{
        File panelsFolder = new File(paneldata.getString("tabpath"));
        if(!panelsFolder.exists()) throw new FileNotFoundException();
        File[] panelFiles = panelsFolder.listFiles();
        String[] names = new String[panelFiles.length];
        for(int i = 0; i < panelFiles.length; i++){
            names[i] = panelFiles[i].getName();
        }

        String qualifier = paneldata.getString("classqualifier");

        for(String panelFile : names){
            try {
                String panelName = panelFile.replace(".java", "");
                Class<Panel> panelClass = (Class<Panel>) Class.forName(qualifier + panelName);
                Constructor<Panel> panelConstructor = panelClass.getConstructor();
                Panel panel = panelConstructor.newInstance();
                panel.setController(controller);
                panels.put(panelName, panel);
            } catch (ClassNotFoundException e) {
                errorMessage.addMessage(String.format(paneldata.getString("nopanel"), e.getMessage()));
            } catch (NoSuchMethodException e) {
                errorMessage.addMessage(String.format(paneldata.getString("noconstructor"), e.getMessage()));
            } catch (InstantiationException e) {
                errorMessage.addMessage(String.format(paneldata.getString("noinstance"), e.getMessage()));
            } catch (IllegalAccessException e) {
                errorMessage.addMessage(String.format(paneldata.getString("hiddenconstructor"), e.getMessage()));
            } catch (InvocationTargetException e) {
                errorMessage.addMessage(String.format(paneldata.getString("noinvocation"), e.getMessage()));
            }
        }
    }
}
