package authoring.panels.tabbable;

import authoring.Panel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class YoutubePanel implements Panel {

    @Override
    public Region getRegion() {
        return new Pane();
    }

    @Override
    public String title() {
        return "Youtube";
    }
}
