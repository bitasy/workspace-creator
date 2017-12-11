package authoring;

import database.jsonhelpers.JSONHelper;
import engine.Engine;
import engine.EntityManager;
import engine.entities.Entity;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import util.math.num.Vector;

/**
 * PanelController delegates access to the engine to each panel that needs it.
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController {

    private static final int GRID_SIZE = 50;
    private static final int CAMERA_INIT_X = 400;
    private static final int CAMERA_INIT_Y = 250;
    private static final int CAMERA_INIT_X_SIZE = 800;
    private static final int CAMERA_INIT_Y_SIZE = 500;
    private static final int VALUE1 = 75;
    private static final int VALUE2 = 75;
    private Engine myEngine;

	private EntityManager myEntityManager;

	public PanelController() {
		myEngine = new Engine(new Entity(), GRID_SIZE, false);//depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
	    myEntityManager = myEngine.getEntityManager();
	}

    /**
     * get camera view
     * @return camera view
     */
	public Node getCamera(){
	    return myEngine.getCameraView(new Vector(CAMERA_INIT_X, CAMERA_INIT_Y), new Vector(CAMERA_INIT_X_SIZE, CAMERA_INIT_Y_SIZE));
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

    public void load(Entity root) {
        myEngine.load(root, GRID_SIZE, false);
    }

    /**
     * get minimap
     * @return
     */
    public Node getMiniMap() {
        return myEngine.getMiniMap(new Vector(VALUE1, VALUE2));
    }
}