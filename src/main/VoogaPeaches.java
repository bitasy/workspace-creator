package main;

import authoring.User;
import authoring.menu.Login;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches the program.
 * @author Brian Nieves
 * @author Kelly Zhang
 */
public class VoogaPeaches extends Application {

    static private User currentUser;
    static private boolean isGaming;

    @Override
	public void start(Stage stage) {
        Login myLogin = new Login(stage);
        stage.show();
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        currentUser.save();
    }

    public static void changeUser(User newUser) { currentUser = newUser; }

    public static User getUser() { return currentUser; }

    public static boolean getIsGaming() { return isGaming; }

    public static void setIsGaming( boolean gaming ) { isGaming = gaming; }

    public static void main(String[] args){
        launch();
    }
}
