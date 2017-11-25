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

	public void addCamera(CameraPanel camera){
	    camera.setCameraView(myEngine.getCameraView(new Vector(0, 0), new Vector(10, 10)));
		myCamera = camera;
		myPlay = camera.getPlay();
		myPause = camera.getPause();
		myWhole = camera.getWhole();
		myLocal = camera.getLocal();
		myLayer = camera.getLayer();
		myPlay.setOnMouseClicked(e -> myEntityManager.addBG(new Vector(0, 0)));
		myPause.setOnMouseClicked(e -> myEngine.print());
		myCamera.getView(myView);
	}

	public void addLibrary(LibraryPanel library) {
		myLibrary = library;
//		myTile = library.getTile();
//		myEntType = library.getEntType();
	}

    public void addBGTile(){
	    myEntityManager.addBG(new Vector(0, 0));
    }



}

