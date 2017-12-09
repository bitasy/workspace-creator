package authoring.menu;

import authoring.Screen;
import database.User;
import database.firebase.DatabaseConnector;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.VoogaPeaches;
import util.PropertiesReader;
import util.exceptions.ObjectIdNotFoundException;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * This is the specific window for the Menu with the different buttons for each simulation, subclass of Window
 * All user interactions are determined and executed in here
 *
 * @author Kelly Zhang
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

    public Menu(Stage stage) {
        myStage = stage;
        myRoot = new Pane();

        myScene = new Scene(myRoot, WIDTH, HEIGHT);
        setupScene();

        myStage.setScene(myScene);
        myStage.setResizable(false);
        myStage.setTitle("main.VoogaPeaches: Menu");
        myStage.show();

        formatButtons();
        updateTheme();
    }

    private void updateTheme() {
        myRoot.getStylesheets().add(VoogaPeaches.getUser().getThemeName());
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


    public void setupScene() {
        addButtons();
        addTitle();
    }

    private void ifPressed() { //http://www.java2s.com/Code/Java/JavaFX/AddClickactionlistenertoButton.htm
        for (int i = 0; i < buttons.size(); i ++) {
            Button button = buttons.get(i);
            button.setOnAction(e -> onPressed(button));

        }
    }

    private Stage onPressed(Button buttonPressed) {
        String button = buttonPressed.getAccessibleText();
        if (button.equals("AUTHORING")) {
            Stage authoringStage = new Stage();
            authoringStage.setTitle("main.VoogaPeaches: A Programmers for Peaches Production");
            authoringStage.setMaximized(true);
            authoringStage.setResizable(false);
            authoring = new Screen(authoringStage);
            authoringStage.setOnCloseRequest(event -> {
                authoring.save();
                DatabaseConnector<User> connector = new DatabaseConnector<>(User.class);
                try {
                    connector.addToDatabase(VoogaPeaches.getUser());
                } catch (ObjectIdNotFoundException e) {
                    //TODO: is this possible? If so what do?
                }
            });
            myStage.close();
        }
        return null;
    }

    private void addButtons() { //https://stackoverflow.com/questions/40883858/how-to-evenly-distribute-elements-of-a-javafx-vbox
        //http://docs.oracle.com/javafx/2/ui_controls/button.htm
        Button authoringButton = createMenuButton(AUTHORINGPIC, AUTHORING_ENVIRONMENT);
        Button playerButton = createMenuButton(PLAYERPIC, PLAYER);
        Button settingsButton = createMenuButton(SETTINGSPIC, SETTINGS);

        buttons = new ArrayList<>(Arrays.asList(authoringButton, playerButton, settingsButton));
        myRoot.getChildren().addAll(buttons);
        ifPressed();
    }

    private void formatButtons() {
        int numButtons = buttons.size();

        double buttonXOffset = WIDTH/(numButtons+1);
        double buttonYOffset = HEIGHT*2/3;

        for (int i = 0; i < numButtons; i++) {
            setMenuButtonLayout(buttons.get(i), buttonXOffset*(i+1) - buttons.get(i).getBoundsInLocal().getWidth()/2, buttonYOffset);
        }
    }

    private void setMenuButtonLayout(Button button, double x, double y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    private Button createMenuButton(String imageName, String buttonText) {
        Button myButton = new Button();
        myButton.setGraphic(createImageView(imageName));

        myButton.setTooltip(new Tooltip(buttonText));
        myButton.setAccessibleText(buttonText);

        return myButton;
    }

    private void addTitle() {
        ImageView title = createImageView("resources/menuImages/VoogaTransparent.png");
        title.setScaleX(0.75);
        title.setScaleY(0.75);
        title.setLayoutX(WIDTH / 2 - title.getBoundsInLocal().getWidth() / 2);
        title.setLayoutY(HEIGHT * 1 / 3 - title.getBoundsInLocal().getHeight() / 2);
        myRoot.getChildren().add(title);
    }

    private ImageView createImageView(String picLocation) {
        File myFile = new File(picLocation);
        ImageView myImageView = new ImageView(myFile.toURI().toString());
        return myImageView;
    }

    public Stage getStage() {
        return myStage;
    }
}
