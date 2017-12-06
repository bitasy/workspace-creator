package authoring;

import engine.Engine;
import engine.EntityManager;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
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
        myEngine = new Engine(null, 20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file//TODO: put something other than null as the root. I guess it's supposed to come from database"
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

    @Override
    public void save() {
        myEngine.save("Wtf is name? *documentation plez*");//TODO figure out what to put here
    }

    /**
     * get minimap
     * @return
     */
    public Pane getMiniMap() {
        return myEngine.getMiniMap(new Vector(75, 75));
    }

}
