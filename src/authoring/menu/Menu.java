package authoring.menu;

import authoring.PanelController;
import authoring.Screen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.tasks.Tasks;
import database.GameLoader;
import database.User;
import database.firebase.DatabaseConnector;
import database.firebase.FileStorageConnector;
import engine.entities.Entity;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
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

    private static final String AUTHORING_ENVIRONMENT = "AUTHORING";
    private static final String PLAYER = "PLAY";
    private static final String SETTINGS = "SETTINGS";
    private static final String AUTHORINGPIC = "resources/menuImages/authoring.png";
    private static final String PLAYERPIC = "resources/menuImages/player.png";
    private static final String SETTINGSPIC = "resources/menuImages/settings.png";

    private static final double WIDTH = Double.parseDouble(PropertiesReader.value("menulayout", "width"));
    private static final double HEIGHT = Double.parseDouble(PropertiesReader.value("menulayout", "height"));

    private List<Button> buttons;

    private Stage myStage;
    private Scene myScene;
    private Pane myRoot;
    private Screen authoring;
    private Stage authoringStage = new Stage();
    private ListView<String> list;
    private Map<String, String> gameUIDS;

    public Menu(Stage stage) {
        gameUIDS = new HashMap<>();
        myStage = stage;
        myRoot = new Pane();
        myScene = new Scene(myRoot, WIDTH, HEIGHT);
        addButtons();
        addTitle();
        myStage.setScene(myScene);
        myStage.setResizable(false);
        myStage.setTitle("main.VoogaPeaches: Menu");
        myStage.show();
        myRoot.getStylesheets().add(VoogaPeaches.getUser().getThemeName());
        formatButtons();
        setupGames();
        updateTheme();
    }


    /**
     * Adds the game selector in the middle of the screen.
     */
    private void setupGames() {
        double width = 200;
        double height = 150;
        double botMargin = 50;
        list = new ListView<>();
        FirebaseDatabase.getInstance().getReference("gameNames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren().forEach(key -> {
                    list.getItems().add(key.getKey());
                    gameUIDS.put(key.getKey(), (String) key.getValue());
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
        list.setLayoutX(WIDTH/2-width/2);
        list.setLayoutY(HEIGHT-height-botMargin);
        list.setPrefSize(width, height);
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
    private void onAuthoringPressed() {
        if (!authoringStage.isShowing() && list.getSelectionModel().getSelectedItem() != null) {
            String UID = gameUIDS.get(list.getSelectionModel().getSelectedItem());
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

    /**
     * Creates the two buttons and connects them to opening the Authoring and Game Playing Environments
     */
    private void addButtons() { //https://stackoverflow.com/questions/40883858/how-to-evenly-distribute-elements-of-a-javafx-vbox
        //http://docs.oracle.com/javafx/2/ui_controls/button.htm
        Button authoringButton = createMenuButton(AUTHORINGPIC, AUTHORING_ENVIRONMENT);
        Button playerButton = createMenuButton(PLAYERPIC, PLAYER);
        Button newGame = new Button("New Game");
        buttons = new ArrayList<>(Arrays.asList(authoringButton, playerButton, newGame));
        myRoot.getChildren().addAll(buttons);
        buttons.get(0).setOnAction((e) -> onAuthoringPressed());
        buttons.get(1).setOnAction((e) -> onPlayingPressed());
        buttons.get(2).setOnAction((e) -> onCreateNewGame());
    }

    private void onCreateNewGame(){
        String UID = gameUIDS.get(list.getSelectionModel().getSelectedItem());
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
     * Will switch to the playing environment
     */
    private void onPlayingPressed() {
        System.out.println("Implement Playing lol");
    }

    /**
     * Used to set the positioning of the buttons
     */
    private void formatButtons() {
        int numButtons = buttons.size();

        double buttonXOffset = WIDTH/(numButtons+2);
        double buttonYOffset = HEIGHT*2/3;

        for (int i = 0; i < numButtons; i++) {
            setMenuButtonLayout(buttons.get(i), buttonXOffset*(i*2+1) - buttons.get(i).getBoundsInLocal().getWidth()/2, buttonYOffset);
        }
    }

    /**
     * Helper method to set the position of a button to the given x and y
     *
     * @param button
     * @param x
     * @param y
     */
    private void setMenuButtonLayout(Button button, double x, double y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    /**
     * Creates a new button specific to the menu
     *
     * @param imageName
     * @param buttonText
     * @return
     */
    private Button createMenuButton(String imageName, String buttonText) {
        Button myButton = new Button();
        myButton.setGraphic(createImageView(imageName));

        myButton.setTooltip(new Tooltip(buttonText));

        return myButton;
    }

    /**
     * Adds the Vooga Peaches text to the menu
     */
    private void addTitle() {
        ImageView title = createImageView("resources/menuImages/VoogaTransparent.png");
        title.setScaleX(0.75);
        title.setScaleY(0.75);
        title.setLayoutX(WIDTH / 2 - title.getBoundsInLocal().getWidth() / 2);
        title.setLayoutY(HEIGHT * 1 / 3 - title.getBoundsInLocal().getHeight() / 2);
        myRoot.getChildren().add(title);
    }

    /**
     * Helper method to create the imageview for the buttons
     *
     * @param picLocation
     * @return
     */
    private ImageView createImageView(String picLocation) {
        File myFile = new File(picLocation);
        ImageView myImageView = new ImageView(myFile.toURI().toString());
        return myImageView;
    }
}
