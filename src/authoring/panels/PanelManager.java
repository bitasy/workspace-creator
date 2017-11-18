package authoring.panels;

import authoring.Panel;
import authoring.PanelController;
import javafx.scene.layout.Region;
import util.ErrorDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PanelManager {
    private Map<String, Panel> panels;

    private ResourceBundle panelStrings = ResourceBundle.getBundle("screenerrors");
    private ErrorDisplay errorMessage;
    private PanelController controller;

    public PanelManager(PanelController controller, ErrorDisplay errorMessage) throws FileNotFoundException{
        this.errorMessage = errorMessage;
        panels = new HashMap<>();
        this.controller = controller;
        loadPanels();
    }

    public String[] getPanels(){
        return panels.keySet().toArray(new String[panels.size()]);
    }

    public Region getPanelDisplay(String panel){
        return panels.get(panel).getRegion();
    }

    public String getPanelTitle(String panel) {
        return panels.get(panel).title();
    }

    private void loadPanels() throws FileNotFoundException{
        File panelsFolder = new File(panelStrings.getString("tabpath"));
        if(!panelsFolder.exists()) throw new FileNotFoundException();
        File[] panelFiles = panelsFolder.listFiles();
        String[] names = new String[panelFiles.length];
        for(int i = 0; i < panelFiles.length; i++){
            names[i] = panelFiles[i].getName();
        }

        String qualifier = panelStrings.getString("classqualifier");

        for(String panelFile : names){
            try {
                String panelName = panelFile.replace(".java", "");
                Class<Panel> panelClass = (Class<Panel>) Class.forName(qualifier + panelName);
                Constructor<Panel> panelConstructor = panelClass.getConstructor();
                Panel panel = panelConstructor.newInstance();
                panel.setController(controller);
                panels.put(panelName, panel);
            } catch (ClassNotFoundException e) {
                errorMessage.addMessage(String.format(panelStrings.getString("nopanel"), e.getMessage()));
            } catch (NoSuchMethodException e) {
                errorMessage.addMessage(String.format(panelStrings.getString("noconstructor"), e.getMessage()));
            } catch (InstantiationException e) {
                errorMessage.addMessage(String.format(panelStrings.getString("noinstance"), e.getMessage()));
            } catch (IllegalAccessException e) {
                errorMessage.addMessage(String.format(panelStrings.getString("hiddenconstructor"), e.getMessage()));
            } catch (InvocationTargetException e) {
                errorMessage.addMessage(String.format(panelStrings.getString("noinvocation"), e.getMessage()));
            }
        }
    }
}
