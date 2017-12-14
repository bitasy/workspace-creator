package authoring;

import engine.Engine;
import engine.EntityManager;
import engine.entities.Entity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
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

    private static final int GRID_SIZE = 2000;
    private static final int CAMERA_INIT_X = 0;
    private static final int CAMERA_INIT_Y = 0;
    private static final int CAMERA_INIT_X_SIZE = 160;
    private static final int CAMERA_INIT_Y_SIZE = 90;
    private static final int VALUE1 = 100;
    private static final int VALUE2 = 100;
    private Engine myEngine;
    private SoundHandler soundEngine;

	private EntityManager myEntityManager;
    private ObjectProperty<Sound> musicProperty;

    public PanelController() {
		myEngine = new Engine(new Entity(), GRID_SIZE, false);//depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
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
        try {
            soundEngine.loopSound(musicProperty.get());
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException | NullPointerException e) {
            //Do nothing: Don't play invalid sound
        }
        //myEngine.play();
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

    public void load(Entity root) {
        System.out.println(root.getChildren().size());
        myEngine.load(root, GRID_SIZE, false);
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

    public ObjectProperty<Sound> getMusicProperty() {
        return musicProperty;
    }
}