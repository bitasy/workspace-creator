package authoring.menu;

import authoring.Screen;
import authoring.buttons.strategies.Logout;
import authoring.buttons.strategies.MenuButton;
import authoring.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Peaches;
import util.PropertiesReader;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.io.File;

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

    private static final String MENU_LAYOUT = "menulayout";
    private static final double WIDTH = Double.parseDouble(PropertiesReader.value(MENU_LAYOUT, "width"));
    private static final double HEIGHT = Double.parseDouble(PropertiesReader.value(MENU_LAYOUT, "height"));
    private static final String AUTHORING_TITLE = "Peaches: A Programmers for Peaches Production -- ";
    private static final String AUTHORING_IMAGE = PropertiesReader.value(MENU_LAYOUT,"authorpic");
    private static final String NEW_GAME_IMAGE = PropertiesReader.value(MENU_LAYOUT,"newprojectpic");
    private static final String TITLE = PropertiesReader.value(MENU_LAYOUT,"title");
    private static final String TITLE_IMAGE_PATH = PropertiesReader.value(MENU_LAYOUT, "voogapic");
    private static final double SELECTION_HEIGHT_RATIO = 0.28;
    private static final int SELECTION_LIST_WIDTH = 200;
    private static final int SELECTION_LIST_HEIGHT = 150;
    private static final String THEME_MESSAGE = "THEME_MESSAGE";
    private static final String PANEL = "panel";
    private static final int HGAP = 50;
    private static final double GRID_HEIGHT_RATIO = 0.7;
    private static final int SELECTION_LIST_XOFFSET = 100;
    private static final double SELECTION_WIDTH_RATIO = 0.5;
    private static final double TITLE_SCALEX = 0.75;
    private static final double TITLE_SCALEY = 0.75;
    private static final double TITLE_WIDTH_RATIO = 0.5;
    private static final double TITLE_HEIGHT_RATIO = 0.2;
    private static final int TITLE_WIDTH_CENTER = 2;
    private static final int TITLE_HEIGHT_CENTER = 2;
    private static final String AUTHORING_TOOLTIP = "authoring";
    private static final String NEWGAME_TOOLTIP = "newproject";
    private static final String DASH = " -- ";
    private static final String USER = "User: ";

    private Pane myRoot;
    private Stage myStage;
    private Screen authoring;
    private Stage authoringStage = new Stage();
    private Stage gamingStage = new Stage();
    private ProjectSelectionList list;
    private User user;

    public Menu(Stage stage, User user) {
        myStage = stage;
        this.user = user;
        setupStage();
        addTitle();
        setupProjects();
        addButtons();
        updateTheme();
    }

    private void setupStage(){
        myRoot = new Pane();
        Scene s = new Scene(myRoot, WIDTH, HEIGHT);
        myStage.setScene(s);
        myStage.setResizable(false);
        myStage.setTitle(TITLE);
        myStage.show();
    }

    /**
     * Adds the project selector in the middle of the screen.
     */
    private void setupProjects() {
        list = new ProjectSelectionList(SELECTION_LIST_WIDTH, SELECTION_LIST_HEIGHT);
        list.setLayoutX(WIDTH * SELECTION_WIDTH_RATIO - SELECTION_LIST_XOFFSET);
        list.setLayoutY(HEIGHT * SELECTION_HEIGHT_RATIO);
        myRoot.getChildren().add(list);
    }

    /**
     * Used to set the initial theme, subscribe to PubSub and get new themes as they are published
     */
    private void updateTheme() {
        String initialTheme = Peaches.getUser().getThemeName();
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
    private void openProjectPressed() {
        if (validOpen()) {
            Peaches.setIsGaming(false);
            authoringStage.setTitle(AUTHORING_TITLE + DASH + list.getSelectionModel().getSelectedItem());
            authoringStage.setMaximized(true);
            authoringStage.setResizable(false);
            this.authoring = new Screen(authoringStage, user);
            authoringStage.setOnCloseRequest(event -> {
                myStage.close();
                authoring.save();
            });
        }
    }

    private boolean validOpen() {
        return !gamingStage.isShowing() && !gamingStage.isShowing() && list.getSelectionModel().getSelectedItem() != null;
    }

    private void newProjectPressed(){
        authoringStage.setTitle(AUTHORING_TITLE);
        authoringStage.setMaximized(true);
        authoringStage.setResizable(false);
        authoring = new Screen(authoringStage, user);
        authoringStage.setOnCloseRequest(event -> {
            myStage.close();
            authoring.save();
        });
    }

    /**
     * Creates the two buttons and connects them to opening the Authoring and Project Playing Environments
     */
    private void addButtons() {
        Button authoringButton = new MenuButton(() -> openProjectPressed(), AUTHORING_IMAGE).getButton();
        authoringButton.setTooltip(new Tooltip(PropertiesReader.value(MENU_LAYOUT, AUTHORING_TOOLTIP)));
        Button newProject = new MenuButton(() -> newProjectPressed(), NEW_GAME_IMAGE ).getButton();
        newProject.setTooltip(new Tooltip(PropertiesReader.value(MENU_LAYOUT, NEWGAME_TOOLTIP)));
        GridPane grid = new GridPane();
        grid.add(newProject, 0,0);
        grid.add(authoringButton,1,0);
        grid.setHgap(HGAP);
        double gridOffset = WIDTH / 2 - (1.5) * newProject.getMinWidth() - HGAP;
        grid.setLayoutX(gridOffset);
        grid.setLayoutY(HEIGHT * GRID_HEIGHT_RATIO);

        javafx.scene.control.Menu user = new javafx.scene.control.Menu(USER + Peaches.getUser().getUserName());
        user.getItems().add(new Logout(grid));
        MenuBar bar = new MenuBar(user);
        myRoot.getChildren().addAll(bar, grid);
    }

    /**
     * Adds the Peaches text to the menu
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