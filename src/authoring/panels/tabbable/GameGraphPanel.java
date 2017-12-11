package authoring.panels.tabbable;

import authoring.Panel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GameGraphPanel implements Panel {

    private static final String TITLE = "Game Graph";
    private static final double SCALING_FACTOR = 1.1;
    private VBox displayBox;
    private Group currentGroup;

    public GameGraphPanel() {
        displayBox = new VBox();
    }

    public void setGroup(Group root) {
        displayBox.getChildren().clear();
        currentGroup = root;
        displayBox.getChildren().add(root);
        displayBox.setOnScroll(event -> {
                event.consume();
                if(event.getDeltaY() == 0) {
                    return;
                }
                double scalingFactor = (event.getDeltaY() > 0) ? SCALING_FACTOR : 1/SCALING_FACTOR;
                currentGroup.setScaleX(currentGroup.getScaleX() * scalingFactor);
                currentGroup.setScaleY(currentGroup.getScaleY() * scalingFactor);
            }
        );
        displayBox.setOnMouseDragged(e -> {
            //currentGroup.setLayoutX(e.getX());
            //currentGroup.setLayoutY(e.getY());

            currentGroup.setTranslateX(e.getScreenX());
            currentGroup.setTranslateY(e.getY());
        });

    }

    @Override
    public Region getRegion() {
        return displayBox;
    }

    @Override
    public String title() {
        return TITLE;
    }
}