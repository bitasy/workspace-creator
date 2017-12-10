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
    private static final String TITLE = "main.VoogaPeaches: Menu";
    private static final double SELECTION_HEIGHT_RATIO = 0.28;
    private static final int SELECTION_LIST_WIDTH = 200;
    private static final int SELECTION_LIST_HEIGHT = 150;
    private static final String THEME_MESSAGE = "THEME_MESSAGE";
    private static final String PANEL = "panel";
    private static final String AUTHORING_TITLE = "main.VoogaPeaches: A Programmers for Peaches Production";
    private static final int HGAP = 50;
    private static final double GRID_WIDTH_RATIO = 0.28;
    private static final double GRID_HEIGHT_RATIO = 0.7;
    private static final int SELECTION_LIST_XOFFSET = 100;
    private static final double SELECTION_WIDTH_RATIO = 0.5;
    private static final String TITLE_IMAGE_PATH = "resources/menuImages/VoogaTransparent.png";
    private static final double TITLE_SCALEX = 0.75;
    private static final double TITLE_SCALEY = 0.75;
    private static final double TITLE_WIDTH_RATIO = 0.5;
    private static final double TITLE_HEIGHT_RATIO = 0.2;
    private static final int TITLE_WIDTH_CENTER = 2;
    private static final int TITLE_HEIGHT_CENTER = 2;

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
        stage.setTitle(TITLE);
        stage.show();
    }

    /**
     * Adds the game selector in the middle of the screen.
     */
    private void setupGames() {
        list = new GameSelectionList(SELECTION_LIST_WIDTH, SELECTION_LIST_HEIGHT);
        list.setLayoutX(WIDTH * SELECTION_WIDTH_RATIO - SELECTION_LIST_XOFFSET);
        list.setLayoutY(HEIGHT * SELECTION_HEIGHT_RATIO);
        myRoot.getChildren().add(list);
    }

    /**
     * Used to set the initial theme, subscribe to PubSub and get new themes as they are published
     */
    private void updateTheme() {
        String initialTheme = VoogaPeaches.getUser().getThemeName();
        myRoot.getStylesheets().add(initialTheme);
        PubSub.getInstance().publish(THEME_MESSAGE,new StringMessage(initialTheme));
        PubSub.getInstance().subscribe(
                THEME_MESSAGE,
                (message) -> {
                    if (myRoot.getStylesheets().size() >= 1) {
                        myRoot.getStylesheets().remove(0);
                    }
                    myRoot.getStylesheets().add(((StringMessage) message).readMessage());
                }
        );
        myRoot.getStyleClass().add(PANEL);
    }

    /**
     * Handles switching to the Authoring screen with the pencil image is clicked
     */
    private void authoringPressed() {
        if (!authoringStage.isShowing() && list.getSelectionModel().getSelectedItem() != null) {
            String UID = list.getSelectedUID();
            GameLoader loader = new GameLoader(UID, e -> { authoring.load(e); });
            authoringStage.setTitle(AUTHORING_TITLE);
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
        authoringStage.setTitle(AUTHORING_TITLE);
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
        grid.setHgap(HGAP);
        grid.setLayoutX(WIDTH * GRID_WIDTH_RATIO);
        grid.setLayoutY(HEIGHT * GRID_HEIGHT_RATIO);
        myRoot.getChildren().addAll(grid);
    }

    /**
     * Adds the Vooga Peaches text to the menu
     */
    private void addTitle() {
        ImageView title = new ImageView(new File(TITLE_IMAGE_PATH).toURI().toString());
        title.setScaleX(TITLE_SCALEX);
        title.setScaleY(TITLE_SCALEY);
        title.setLayoutX(WIDTH * TITLE_WIDTH_RATIO - title.getBoundsInLocal().getWidth() / TITLE_WIDTH_CENTER);
        title.setLayoutY(HEIGHT * TITLE_HEIGHT_RATIO - title.getBoundsInLocal().getHeight() / TITLE_HEIGHT_CENTER);
        myRoot.getChildren().add(title);
    }
}