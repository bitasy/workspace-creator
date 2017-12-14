package authoring;

import engine.Engine;
import engine.EntityManager;
import engine.entities.Entity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import util.math.num.Vector;
import voogasalad.util.sound.Sound;
import voogasalad.util.sound.SoundHandler;
import voogasalad.util.sound.SoundManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * PanelController delegates access to the engine to each panel that needs it.
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController {

    public static final int GRID_SIZE = 50;
    public static final int CAMERA_INIT_X = 400;
    public static final int CAMERA_INIT_Y = 250;
    public static final int CAMERA_INIT_X_SIZE = 800;
    public static final int CAMERA_INIT_Y_SIZE = 500;
    private static final int VALUE1 = 150;
    private static final int VALUE2 = 150;
    private Engine myEngine;
    private SoundHandler soundEngine;

	private EntityManager myEntityManager;
    private ObjectProperty<Sound> musicProperty;

	public PanelController(Entity root) {
		myEngine = new Engine(root, GRID_SIZE, false);
	    myEntityManager = myEngine.getEntityManager();
	    soundEngine = new SoundManager(false);
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
        /*try { TODO put sound in engine if possible
            soundEngine.loopSound(musicProperty.get());
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException | NullPointerException e) {
            //Do nothing: Don't play invalid sound
        }*/
        myEngine.play();
    }

    /**
     * engine stop to run script
     */
    public void pause() {
        soundEngine.removeAllSounds();
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
        return myEngine.getMiniMap(new Vector(VALUE1, VALUE2));
    }

    public void setMusicProperty(ObjectProperty<Sound> musicProperty) {
        this.musicProperty = new SimpleObjectProperty<>();
        this.musicProperty.bind(musicProperty);

    }

    public Engine getEngine() {
        return myEngine;
    }
}