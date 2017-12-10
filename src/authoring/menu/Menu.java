package authoring.menu;

import authoring.Screen;
import authoring.buttons.strategies.MenuButton;
import database.GameLoader;
import database.User;
import database.firebase.DatabaseConnector;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.VoogaPeaches;
import util.PropertiesReader;
import util.exceptions.ObjectIdNotFoundException;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.io.File;
import java.util.*;

/**
 *
 * This is the specific window for the Menu with the different buttons for each simulation, subclass of Window
 * All user interactions are determined and executed in here
 *
 * @author Kelly Zhang
 * @author Simran
 *
 */
public class Menu {

    private static final String AUTHORING_IMAGE = "resources/menuImages/authoring.png";
    private static final String PLAYER_IMAGE = "resources/menuImages/player.png";
    private static final String NEW_GAME_IMAGE = "resources/menuImages/new_game_button.png";
    private static final double WIDTH = Double.parseDouble(PropertiesReader.value("menulayout", "width"));
    private static final double HEIGHT = Double.parseDouble(PropertiesReader.value("menulayout", "height"));

    private Pane myRoot;
    private Screen authoring;
    private Stage authoringStage = new Stage();
    private GameSelectionList list;

    public Menu(Stage stage) {
        setupStage(stage);
        addTitle();
        setupGames();
        addButtons();
        myRoot.getStylesheets().add(VoogaPeaches.getUser().getThemeName());
        updateTheme();
    }

    private void setupStage(Stage stage){
        myRoot = new Pane();
        Scene s = new Scene(myRoot, WIDTH, HEIGHT);
        stage.setScene(s);
        stage.setResizable(false);
        stage.setTitle("main.VoogaPeaches: Menu");
        stage.show();
    }

    /**
     * Adds the game selector in the middle of the screen.
     */
    private void setupGames() {
        list = new GameSelectionList(200, 150);
        list.setLayoutX(WIDTH/2-100);
        list.setLayoutY(HEIGHT * 0.28);
        myRoot.getChildren().add(list);
    }

    /**
     * Used to set the initial theme, subscribe to PubSub and get new themes as they are published
     */
    private void updateTheme() {
        String initialTheme = VoogaPeaches.getUser().getThemeName();
        myRoot.getStylesheets().add(initialTheme);
        PubSub.getInstance().publish("THEME_MESSGE",new StringMessage(initialTheme));
        PubSub.getInstance().subscribe(
                "THEME_MESSAGE",
                (message) -> {
                    if (myRoot.getStylesheets().size() >= 1) {
                        myRoot.getStylesheets().remove(0);
                    }
                    myRoot.getStylesheets().add(((StringMessage) message).readMessage());
                }
        );
        myRoot.getStyleClass().add("panel");
    }

    /**
     * Handles switching to the Authoring screen with the pencil image is clicked
     */
    private void authoringPressed() {
        if (!authoringStage.isShowing() && list.getSelectionModel().getSelectedItem() != null) {
            String UID = list.getSelectedUID();
            GameLoader loader = new GameLoader(UID, e -> { authoring.load(e); });
            authoringStage.setTitle("main.VoogaPeaches: A Programmers for Peaches Production");
            authoringStage.setMaximized(true);
            authoringStage.setResizable(false);
            authoring = new Screen(authoringStage);
            authoringStage.setOnCloseRequest(event -> {
                authoring.save();
                DatabaseConnector<User> connector = new DatabaseConnector<>(User.class);
                try { connector.addToDatabase(VoogaPeaches.getUser()); } catch (ObjectIdNotFoundException e) {}
            });
        }
    }

    private void playPressed(){ }

    private void newGamePressed(){
        authoringStage.setTitle("main.VoogaPeaches: A Programmers for Peaches Production");
        authoringStage.setMaximized(true);
        authoringStage.setResizable(false);
        authoring = new Screen(authoringStage);
        authoringStage.setOnCloseRequest(event -> {
            authoring.save();
            DatabaseConnector<User> connector = new DatabaseConnector<>(User.class);
            try { connector.addToDatabase(VoogaPeaches.getUser()); } catch (ObjectIdNotFoundException e) {}
        });
    }

    /**
     * Creates the two buttons and connects them to opening the Authoring and Game Playing Environments
     */
    private void addButtons() {
        Button authoringButton = new MenuButton( ( ) -> authoringPressed(), AUTHORING_IMAGE).getButton();
        Button playButton = new MenuButton(() -> playPressed(), PLAYER_IMAGE).getButton();
        Button newGame = new MenuButton(() -> newGamePressed(), NEW_GAME_IMAGE ).getButton();
        GridPane grid = new GridPane();
        grid.add(newGame, 0,0);
        grid.add(authoringButton,1,0);
        grid.add(playButton,2,0);
        grid.setHgap(50);
        grid.setLayoutX(WIDTH * 0.28);
        grid.setLayoutY(HEIGHT * 0.7);
        myRoot.getChildren().addAll(grid);
    }

    /**
     * Adds the Vooga Peaches text to the menu
     */
    private void addTitle() {
        ImageView title = new ImageView(new File("resources/menuImages/VoogaTransparent.png").toURI().toString());
        title.setScaleX(0.75);
        title.setScaleY(0.75);
        title.setLayoutX(WIDTH / 2 - title.getBoundsInLocal().getWidth() / 2);
        title.setLayoutY(HEIGHT * 0.2 - title.getBoundsInLocal().getHeight() / 2);
        myRoot.getChildren().add(title);
    }
}