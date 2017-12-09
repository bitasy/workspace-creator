package main;

import authoring.Screen;
import authoring.menu.Menu;
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
    static public Menu menuScreen;

    @Override
	public void start(Stage stage) {
        Login myLogin = new Login(stage);
        myLogin.getStage().show();
    }

    @Override
    public void stop() throws Exception{
        FirebaseConnector.closeFirebaseApp();
        super.stop();
    }

    public static void changeUser(User newUser) {currentUser = newUser;}

    public static User getUser() {return currentUser; }

    public static Menu getMenu() { return menuScreen; }

    public static void createMenu(Menu myMenu) {menuScreen = myMenu;}

    public static void main(String[] args){
        launch();
    }
}
