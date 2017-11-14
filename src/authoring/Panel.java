package authoring;

import javafx.scene.layout.Region;

/**
 * A Panel is the functional unit of the VoogaSalad authoring environment. Every panel has a single purpose and a single place to be shown on the screen. Panels can be opened and closed at will by the user, and are held in a TabView for each area with the exception of the Menu Bar.
 * @author Brian Nieves
 */
public interface Panel {

    /**
     * Returns the region to be displayed inside the authoring environment. The Panel must ensure that it views properly within the environment, regardless of the environment's current width and height, which may affect the size of the display space allocated to the Panel.
     * @return the Region to be displayed
     */
    Region getRegion();

    /**
     * Returns which area of the Screen the Panel will be displayed on. The return is expected to have the value of one of the area constants specified in the Screen class.
     * @return the area to be displayed in
     */
    int getArea();

    /**
     * Sets the controller with which this Panel will communicate with both other panels and the engine. This method will be called directly after the creation of any Panel object by the Screen. Its implementation is not required for Panels that require no communication with other parts of the environment.
     */
    default void setController(PanelController controller){}

    /**
     * Returns the title of the panel to be displayed in the authoring environment.
     * @return the title
     */
    String title();
}
