package authoring.buttons;

import authoring.buttons.strategies.IButtonStrategy;
import javafx.scene.control.Button;

public class CustomButton {
    private IButtonStrategy strategy;
    private Button button;
    public CustomButton(IButtonStrategy strategy) {
        button = new Button();
        this.strategy = strategy;
        button.setOnAction(e -> strategy.fire());
    }

    public Button getButton() {
        return button;
    }
}
