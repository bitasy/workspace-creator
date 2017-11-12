package authoring;

import authoring.panels.CameraPanel;
import authoring.panels.MenuBarPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Screen represents the container of the various areas of the authoring environment user interface. Areas can contain multiple Panels, and each Panel specifies what area it should be viewed in.
 * @author Brian Nieves
 * @author estellehes
 */
public class Screen {

    /**
     * Constants represent the various areas of the user interface.
     * @see <a href="https://coursework.cs.duke.edu/CompSci308_2017Fall/voogasalad_programmersforpeaches/raw/master/doc/UI.png">UI Image</a>
     */
    public static final int TOP = 0;
    public static final int BOTTOM_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;
    public static final int TOP_RIGHT = 3;
    public static final int CAMERA = 4;


    private BorderPane root;
    private PanelController myController;
    private MenuBarPanel myMenuBar;
    private CameraPanel myCameraPanel;

    /**
     * Constructs a new Screen, which in turn creates a new environment in the specified Stage.
     * @param stage the stage that will display the screen
     */
    public Screen(Stage stage){
        int width = getIntValue("width");
        int height = getIntValue("height");
        int cameraWidth = getIntValue("camerawidth");
        int cameraHeight = getIntValue("cameraheight");
        int gridNum = getIntValue("camerarownum");

        root = new BorderPane();
        myMenuBar = new MenuBarPanel();
        myCameraPanel = new CameraPanel();
        myController = new PanelController(myCameraPanel); //just for testing, should not pass camera panel into controller
        
        myMenuBar.setController();
        myCameraPanel.setController();

        root.setTop(myMenuBar.getRegion());
        root.setCenter(myCameraPanel.getRegion()); //size still need to be adjusted, just for testing

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private int getIntValue(String key){
        return Integer.parseInt(properties.getString(key));
    }
}
