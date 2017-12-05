package authoring;

import engine.Engine;
import engine.EntityManager;
import javafx.scene.control.ScrollPane;
import util.math.num.Vector;


/**
 *
 * Currently impelementation is just for testing
 *
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController implements IPanelController {
	private Engine myEngine;

	private EntityManager myEntityManager;

	public PanelController() {
		myEngine = new Engine(20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
	    myEntityManager = myEngine.getEntityManager();
	}

    /**
     * get camera view
     * @return camera view
     */
	public ScrollPane getCamera(){
	    return myEngine.getCameraView(new Vector(1600, 1750), new Vector(800, 500));
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
 }

