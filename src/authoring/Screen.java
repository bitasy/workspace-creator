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


    private final int WIDTH;
    private final int HEIGHT;

    private BorderPane root;

    /**
     * Constructs a new Screen, which in turn creates a new environment in the specified Stage.
     * @param stage the stage that will display the screen
     */
    public Screen(Stage stage){
        ResourceBundle properties = ResourceBundle.getBundle("screenlayout"); //If this doesn't work, mark the data folder as a resource folder
        WIDTH = Integer.parseInt(properties.getString("width"));
        HEIGHT = Integer.parseInt(properties.getString("height"));

        root = new BorderPane();

        root.setTop(new MenuBarPanel().getRegion());
        root.setCenter(new CameraPanel().getRegion()); //size still need to be adjusted, just for testing

        Scene scene = new Scene(root,WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
