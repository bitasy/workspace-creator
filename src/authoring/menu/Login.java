package authoring.menu;

import authoring.User;
import com.google.gson.Gson;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.VoogaPeaches;
import util.PropertiesReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;


/**
 * Login splash screen. Should give information about the user if there is a valid user object corresponding
 * to the username.
 *
 * @author Simran Singh
 * @author Kelly Zhang
 */
public class Login {


    private static final int INSET = 5;
    private static final String TITLE = "VoogaPeaches: Login to Your Account";
    private static final int SPACING = 10;
    private static final String USER_NAME = "User Name";
    private static final String LOGIN = "Login";
    private static final String CREATE_PROFILE = "Create Profile";
    private static final String LIGHT_CSS = "light.css";
    private static final String PANEL = "panel";
    private static final String ERROR = "Unrecognized username.";

    public static final int WIDTH = 350;
    public static final int HEIGHT = 125;
    private Stage myStage;
    private Scene myScene;
    private VBox myArea;
    private TextField userTextField;
    private Label error;

    public Login(Stage stage) {
        myStage = stage;
        myArea = createVBoxLayout();
        myScene = new Scene(myArea, WIDTH, HEIGHT);
        myScene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) loginPressed();
        });
        myStage.setScene(myScene);
        myStage.setResizable(false);
        myStage.setTitle(TITLE);
        updateTheme();
    }

    /**
     * Creates the layout of the login screen, connecting the login button to log in existing users and the create profile to making a new user
     * @return VBox with the labels and textfields for the login
     */
    private VBox createVBoxLayout() {
        VBox vbox = new VBox();
        vbox.setSpacing(SPACING);
        vbox.setPadding(new Insets(INSET));
        vbox.setAlignment(Pos.CENTER_LEFT);
        Text userLabel = new Text(USER_NAME);
        userTextField = new TextField();
        GridPane grid = new GridPane();
        grid.setHgap(SPACING);
        Button loginButton = new Button(LOGIN);
        loginButton.setOnAction(e -> loginPressed());
        grid.add(loginButton, 0,0);
        Button signupButton = new Button(CREATE_PROFILE);
        signupButton.setOnAction(e -> createAccount() );
        grid.add(signupButton, 1, 0);
        error = new Label();
        error.setVisible(false);
        vbox.getChildren().addAll(userLabel, userTextField, grid, error);
        return vbox;
    }

    /**
     * When a new username is entered, the database will eb checked for the username (to see if it already exists) and then if not it will create the new account and then launch the menu with the default display theme for the new account
     * Note: a new account is not associated with any game list
     */
    private void createAccount(){
        if(!userTextField.getText().trim().isEmpty()){
            User newUser = new User(userTextField.getText().trim());
            newUser.save();
            VoogaPeaches.changeUser(newUser);
            Stage menuStage = new Stage();
            new Menu(menuStage, newUser);
            myStage.close();
        }
    }


    /**
     * On the login, it reads the text that the user input. No password check currently. It tries to find a
     * JSON with the username, if it isn't there, it currently doesn't do anything, but if a JSON file exists,
     * it'll publish the current theme and workspace.
     */
    private void loginPressed() {
        try (Reader reader = new InputStreamReader(new FileInputStream(
                PropertiesReader.value("filepaths", "users") + userTextField.getText() + ".json"), StandardCharsets.UTF_8)){

            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            VoogaPeaches.changeUser(user);
            Stage menuStage = new Stage();
            new Menu(menuStage, user);
            myStage.close();
        } catch (IOException e) {
            error.setText(ERROR);
            error.setVisible(true);
        }
    }

    private void updateTheme() {
        myArea.getStylesheets().add(LIGHT_CSS);
        myArea.getStyleClass().add(PANEL);
    }
}
