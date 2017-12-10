package authoring.buttons.strategies;

import authoring.buttons.CustomButton;
import javafx.scene.image.ImageView;

import java.io.File;

public class MenuButton extends CustomButton {

    private static final double BUTTON_SIZE = 100.0;
    private static final double IMAGE_SIZE = BUTTON_SIZE * 0.8;

    public MenuButton(IButtonStrategy strategy, String image) {
        super(strategy,"");
        ImageView pic = getImageView(image);
        this.getButton().setGraphic(pic);
        applyStyling();
    }

    private void applyStyling() {
        this.getButton().setMaxSize(BUTTON_SIZE,BUTTON_SIZE);
        this.getButton().setMinSize(BUTTON_SIZE,BUTTON_SIZE);
    }

    private ImageView getImageView(String image){
        File img = new File(image);
        ImageView view = new ImageView(img.toURI().toString());
        view.setFitWidth(IMAGE_SIZE);
        view.setFitHeight(IMAGE_SIZE);
        return view;
    }


}
