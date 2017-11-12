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
public class PanelController {
	private Engine myEngine;
	private CameraPanel myCamera;
	private Button myPlay;
	private Button myPause;
	
	
	public PanelController(CameraPanel camera) {
		
		myEngine = new Engine(20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
		myCamera = camera;
		myPlay = camera.getPlay();
		myPause = camera.getPause();
	}
	
}
