package authoring.panels.tabbable;

import authoring.*;
import extensions.ExtensionWebView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class SocialMediaPanel implements Panel{

    private ExtensionWebView myExtensionView;
    private VBox myArea;

    public SocialMediaPanel() {
        myArea = new VBox();
        myExtensionView = new ExtensionWebView("SocialMedia.html",300,600);
        myArea.getChildren().add(myExtensionView.getView());
    }

    @Override
    public Region getRegion() {
        return myArea;
    }

    @Override
    public void setController(IPanelController controller) {
        //TODO: Create controller
    }

    @Override
    public String title(){
        return "Social Media";
    }

}
