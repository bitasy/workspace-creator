package authoring;

import authoring.panels.tabbable.PropertiesPanel;
import engine.Engine;
import engine.entities.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import engine.EntityManager;
import javafx.scene.control.ScrollPane;
import util.PropertiesReader;
import util.exceptions.GroovyInstantiationException;
import util.math.num.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * PanelController delegates access to the engine to each panel that needs it.
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController {
    private Engine myEngine;

	private EntityManager myEntityManager;

	public PanelController() {
		myEngine = new Engine(new Entity(), 70); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
	    myEntityManager = myEngine.getEntityManager();
	}

    /**
     * get camera view
     * @return camera view
     */
	public ScrollPane getCamera(){
	    return myEngine.getCameraView(new Vector(400, 250), new Vector(800, 500));
	}

    /**
     * get entitymanager
     * @return entitymanager
     */
    public EntityManager getManager() {
	    return myEntityManager;
    }

    /**
     * engine start to run script
     */
    public void play() {
        myEngine.play();
    }

    /**
     * engine stop to run script
     */
    public void pause() {
        myEngine.pause();
    }

    public void save(String name) {
        myEngine.save(name);
    }

    /**
     * get minimap
     * @return
     */
    public Pane getMiniMap() {
        return myEngine.getMiniMap(new Vector(75, 75));
    }

    public static class ScriptButton {
        private final String ADD = "Add";
        private Map<String, List<String>> myMap;
        private PropertiesPanel myPanel;
        private ComboBox comboBox;
        private HBox hbox;

        public ScriptButton(Map<String, List<String>> map, PropertiesPanel panel){
            myMap = map;
            myPanel = panel;
            hbox = new HBox();
            makeVisual();
        }

        private void makeVisual() {
            Set<String> events= PropertiesReader.map("events").keySet();
            ObservableList<String> options = FXCollections.observableArrayList(events);
            comboBox = new ComboBox(options);

            Button button = makeButton();
            hbox.getChildren().add(comboBox);
            hbox.getChildren().add(button);
        }

        private Button makeButton() {
            Button button = new Button(ADD);
            button.setOnAction(e-> add());
            return button;
            }

        private void add() {
            String type = comboBox.getSelectionModel().getSelectedItem().toString();
            myMap.put(type, new ArrayList<String>());
            try {
                myPanel.updateView();
            } catch (GroovyInstantiationException e) {}
        }

        public Node getNode(){
            return hbox;
        }
    }
}

