package authoring.panels.tabbable;

import authoring.Panel;
import authoring.PanelController;
import extensions.ExtensionWebView;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class SocialMediaPanel implements Panel {

    private static final int HEIGHT = 1000;
    private static final int WIDTH = 600;
    private static final String SOCIALMEDIA_HTML = "SocialMedia.html";
    private static final String PANEL = "panel";
    private static final String FORWARD = "Forward >";
    private static final String BACKWARD = "< Backward";
    private static final String HOME = "Home";
    private static final String SOCIAL_MEDIA = "Social Media";
    private VBox myArea;
    private ExtensionWebView myExtensionView;

    public SocialMediaPanel() {
        myArea = new VBox();
        myArea.fillWidthProperty().setValue(true);
        myExtensionView = new ExtensionWebView(SOCIALMEDIA_HTML, HEIGHT, WIDTH);
        myArea.getChildren().add(myExtensionView.getView());
        createHistoryButtons();
        getRegion().getStyleClass().add(PANEL);
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
                myExtensionView.loadHTML(SOCIALMEDIA_HTML);
            }
        };
        forwards.setText(FORWARD);
        backwards.setText(BACKWARD);
        history.setText(HOME);
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
        return SOCIAL_MEDIA;
    }

}
