package authoring.buttons.strategies;

import authoring.menu.Login;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Logout extends MenuItem {

    private static final String TEXT = "Logout";

    public Logout(Node nodeInWindow){
        super(TEXT);
        setOnAction(e -> {
            Stage stage = new Stage();
            Login login = new Login(stage);
            Stage toClose = ((Stage)nodeInWindow.getScene().getWindow());
            //Following close thanks to jewelsea (https://stackoverflow.com/questions/24483686/how-to-force-javafx-application-close-request-programmatically)
            toClose.fireEvent(
                    new WindowEvent(
                            stage,
                            WindowEvent.WINDOW_CLOSE_REQUEST
                    ));
            stage.show();
        });
    }
}
