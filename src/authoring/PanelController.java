package authoring;

import authoring.panels.CameraPanel;
import engine.Engine;

/**
 * TODO: Create the controller
 * @author Brian Nieves
 * @author Estelle He
 */
public class PanelController {
	private Engine myEngine;
	private CameraPanel myCamera;
	
	public PanelController(CameraPanel camera) {
		
		myEngine = new Engine(20); //depending on the design of panelcontroller, gridszie would either be retrived from camera panel or properties file
		myCamera = camera;
	}
	
}
