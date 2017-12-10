package authoring;

import engine.Engine;
import engine.EntityManager;
import engine.entities.Entity;
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
	public ScrollPane getCamera(){
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

    public ScrollPane load(Entity root) {
        System.out.println(root.getChildren().size());
        myEngine.pause();
        myEngine = new Engine(root, GRID_SIZE, false);
        return myEngine.getCameraView(new Vector(CAMERA_INIT_X, CAMERA_INIT_Y), new Vector(CAMERA_INIT_X_SIZE, CAMERA_INIT_Y_SIZE));
    }

    /**
     * get minimap
     * @return
     */
    public Pane getMiniMap() {
        return myEngine.getMiniMap(new Vector(75, 75));
    }
}

