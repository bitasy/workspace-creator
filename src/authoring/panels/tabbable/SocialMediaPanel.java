package authoring.panels.tabbable;

import authoring.Panel;
import authoring.PanelController;
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
        getRegion().getStyleClass().add("panel");
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
        Button history = new Button() {
            @Override
            public void fire() {
                myExtensionView.loadHTML("SocialMedia.html");
            }
        };
        forwards.setText("Forward >");
        backwards.setText("< Backward");
        history.setText("Home");
        bar.getItems().add(backwards);
        bar.getItems().add(forwards);
        bar.getItems().add(history);
        myArea.getChildren().add(bar);
    }

    @Override
    public Region getRegion() {
        return myArea;
    }

    @Override
    public void setController(PanelController controller) {}

    @Override
    public String title(){
        return "Social Media";
    }

}
