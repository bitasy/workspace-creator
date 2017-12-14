package authoring.menu;

import database.User;
import database.firebase.DatabaseConnector;
import database.jsonhelpers.JSONDataFolders;
import database.jsonhelpers.JSONDataManager;
import database.jsonhelpers.JSONHelper;
import database.jsonhelpers.JSONToObjectConverter;
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
import org.json.JSONObject;
import util.exceptions.ObjectIdNotFoundException;


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
        error = new Label(ERROR);
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
            JSONDataManager manager = new JSONDataManager(JSONDataFolders.USER_SETTINGS);
            manager.writeJSONFile(userTextField.getText().trim(), JSONHelper.JSONForObject(newUser));
            VoogaPeaches.changeUser(newUser);
            DatabaseConnector<User> db = new DatabaseConnector<>(User.class);
            try {
                db.addToDatabase(newUser);
            } catch (ObjectIdNotFoundException e) {
                System.out.println(e.getMessage());
            }
            Stage menuStage = new Stage();
            Menu myMenu = new Menu(menuStage);
            myStage.close();
        }
    }


    /**
     * On the login, it reads the text that the user input. No password check currently. It tries to find a
     * JSON with the username, if it isn't there, it currently doesn't do anything, but if a JSON file exists,
     * it'll publish the current theme and workspace.
     */
    private void loginPressed() {
        DatabaseConnector<User> connector = new DatabaseConnector<>(User.class);
        JSONDataManager manager = new JSONDataManager(JSONDataFolders.USER_SETTINGS);
        JSONObject blueprint = manager.readJSONFile(userTextField.getText());
        if (blueprint != null) {
            JSONToObjectConverter<User> converter = new JSONToObjectConverter<>(User.class);
            User user = converter.createObjectFromJSON(User.class, blueprint);
            VoogaPeaches.changeUser(user);
            Stage menuStage = new Stage();
            Menu myMenu = new Menu(menuStage);
            menuStage.setOnCloseRequest(event -> {
                //TODO: SIMRAN HALP
                try {
                    connector.addToDatabase(VoogaPeaches.getUser());
                } catch (ObjectIdNotFoundException e) {
                    // do nothing
                }
            });
            myStage.close();
        }
        error.setVisible(true);
    }

    private void updateTheme() {
        myArea.getStylesheets().add(LIGHT_CSS); //update from database
        myArea.getStyleClass().add(PANEL);
    }
}
