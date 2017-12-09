package main;

import database.User;
import authoring.menu.Login;
import database.firebase.FirebaseConnector;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches the program.
 * @author Brian Nieves
 */
public class VoogaPeaches extends Application {

    static public User currentUser;

    @Override
	public void start(Stage stage) {
//        Login myLogin = new Login(stage);
//        myLogin.getStage().show();
        Screen kitty = new Screen(stage);

    }

    @Override
    public void stop() throws Exception{
        //TODO: save the authoring environment when that stage is closed
        super.stop();
        FirebaseConnector.closeFirebaseApp();
    }

    public static void changeUser(User newUser) {currentUser = newUser;}

    public static User getUser() {return currentUser; }

    public static void main(String[] args){
        launch();
    }
}
