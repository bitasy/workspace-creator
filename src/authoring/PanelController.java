package authoring;

import authoring.panels.CameraPanel;
import authoring.panels.LibraryPanel;
import engine.Engine;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;


/**
 * TODO: Create the controller
 *
 * Currently impelementation is just for testing
 *
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController implements IPanelDelegate {
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
<<<<<<< HEAD


=======
	
	
>>>>>>> 98627e938089da10cbd978a16cccf0589ca307c2
	public PanelController() {
		myEngine = new Engine(20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
	}

	public void addCamera(CameraPanel camera){
		myCamera = camera;
		myPlay = camera.getPlay();
		myPause = camera.getPause();
		myWhole = camera.getWhole();
		myLocal = camera.getLocal();
		myLayer = camera.getLayer();
		myGrid = camera.getGridPane();
	}
<<<<<<< HEAD

=======
	
>>>>>>> 98627e938089da10cbd978a16cccf0589ca307c2
	public void addLibrary(LibraryPanel library) {
		myLibrary = library;
		myTile = library.getTile();
		myEntType = library.getEntType();
	}


<<<<<<< HEAD



=======
	
	
	
>>>>>>> 98627e938089da10cbd978a16cccf0589ca307c2
}

