package authoring.panels.tabbable;

import authoring.IPanelController;
import authoring.Panel;
import extensions.ExtensionWebView;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class SocialMediaPanel implements Panel {

    private VBox myArea;
    private ExtensionWebView myExtensionView;

    public SocialMediaPanel() {
        myArea = new VBox();
        myArea.fillWidthProperty().setValue(true);
        myExtensionView = new ExtensionWebView("SocialMedia.html",1000,600);
        myArea.getChildren().add(myExtensionView.getView());
        createHistoryButtons();
    }

    private void createHistoryButtons() {
        ToolBar bar = new ToolBar();
        Button forwards = new Button() {
            @Override
            public void fire() { myExtensionView.goForward(); }
        };
        Button backwards = new Button() {
            @Override
            public void fire() { myExtensionView.goBack(); }
        };
        forwards.setText("Forward >");
        backwards.setText("< Backward");
        bar.getItems().add(backwards);
        bar.getItems().add(forwards);
        myArea.getChildren().add(bar);
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
