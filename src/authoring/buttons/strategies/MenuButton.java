package authoring.buttons.strategies;

import authoring.buttons.CustomButton;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * Defines functionality for the button's on the initial
 * menu seen when loading up the game
 *
 * @author Walker Willetts
 */
public class MenuButton extends CustomButton {

    private static final double BUTTON_SIZE = 100.0;
    private static final double IMAGE_SIZE = BUTTON_SIZE * 0.8;
    private static final String EMPTY_TITLE = "";

    /**
     *
     * @param strategy is a {@code IButtonStrategy} that defines the action to take
     *                 when the MenuButton is pressed
     * @param image is the {@String} defining the path of the image to use for the
     *              given menu button
     */
    public MenuButton(IButtonStrategy strategy, String image) {
        super(strategy, EMPTY_TITLE);
        ImageView pic = getImageView(image);
        this.getButton().setGraphic(pic);
        applyStyling();
    }

    /**
     * Applies basic styling to the button's seen in the first window of the application
     */
    private void applyStyling() {
        this.getButton().setMaxSize(BUTTON_SIZE,BUTTON_SIZE);
        this.getButton().setMinSize(BUTTON_SIZE,BUTTON_SIZE);
    }

    /**
     * Creates the appropriate ImageView for the menu button
     * @param image is the {@String} corresponding to the path of the image
     * @return An {@code ImageView} representing the ImageView for the button
     */
    private ImageView getImageView(String image){
        File img = new File(image);
        ImageView view = new ImageView(img.toURI().toString());
        view.setFitWidth(IMAGE_SIZE);
        view.setFitHeight(IMAGE_SIZE);
        return view;
    }
}