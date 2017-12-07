package authoring;

import engine.Engine;
import engine.entities.Entity;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import engine.EntityManager;
import javafx.scene.control.ScrollPane;
import util.math.num.Vector;


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

 }

