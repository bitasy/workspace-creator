package authoring;

import authoring.panels.reserved.CameraPanel;
import authoring.panels.tabbable.LibraryPanel;
import engine.Engine;
import engine.managers.EntityManager;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import util.math.num.Vector;


/**
 * TODO: Create the controller
 *
 * Currently impelementation is just for testing
 *
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController implements IPanelController {
	private Engine myEngine;
	private CameraPanel myCamera;
	private Button myPlay;
	private Button myPause;
	private GridPane myGrid;
	private RadioButton myWhole;
	private RadioButton myLocal;
	private ChoiceBox<String> myLayer;
	private LibraryPanel myLibrary;
	private TilePane myTile;
	private ChoiceBox<String> myEntType;
	private ScrollPane myView;

	private EntityManager myEntityManager;

	public PanelController() {
		myEngine = new Engine(20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
	    myEntityManager = myEngine.getEntityManager();
	}

	public ScrollPane getCamera(){
	    return myEngine.getCameraView(new Vector(1600, 1750), new Vector(800, 500));
	}

	public void addLibrary(LibraryPanel library) {
		myLibrary = library;
	}


    public void addBGTile(Vector pos){

		myEntityManager.addBG(pos);
    }

    /**
     * select layer for display mode
     *
     * 0 is bg layer, -1 is all layer
     *
     * @param layer
     */
    public void selectLayer(int layer) {
        if (layer == 0) {
            myEntityManager.selectBGLayer();
        }
        else if (layer == -1) {
            myEntityManager.allLayer();
        }
        else {
            myEntityManager.selectLayer(layer);
        }
    }


}

