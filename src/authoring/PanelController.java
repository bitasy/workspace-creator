package authoring;

import authoring.panels.CameraPanel;
import engine.Engine;
import javafx.scene.control.Button;


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
	
	
	public PanelController() {
		myEngine = new Engine(20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
	}

	public void addCamera(CameraPanel camera){
		myCamera = camera;
		myPlay = camera.getPlay();
		myPause = camera.getPause();
	}


	
}
