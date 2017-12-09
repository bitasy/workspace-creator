package main;//import authoring.Screen;
import authoring.Login;
import authoring.Screen;
import database.User;
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
        Login myLogin = new Login(stage);
        myLogin.getStage().show();
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
