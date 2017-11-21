package authoring.panels.tabbable;

import authoring.Panel;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.ResourceBundle;

/**
 * youtube panel inside the authoring environment, used to view some default tutorials but can also be used to browse other youtube videos
 * @author Kelly Zhang
 */
public class YoutubePanel implements Panel {

    private Pane myPane = new Pane();
    private ResourceBundle videoLinks = ResourceBundle.getBundle("tutorials");
    private ChoiceBox<String> myTutorial;


    public YoutubePanel() {
        myPane = new Pane();
        videoLinks = ResourceBundle.getBundle("tutorials");

        //TODO: create a dropdown for some default tutorials/videos that the user can access
/*        myTutorial = new ChoiceBox<String>();
        myTutorial.getItems().addAll("");
        myTutorial.getSelectionModel().selectFirst();
        myTutorial.setStyle(getString("nodeStyle"));*/


        playVideo("video");
    }

    @Override
    public Region getRegion() {
        return myPane;
    }

    @Override
    public String title() {
        return "Youtube";
    }



    private void playVideo(String video) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getString("catVideo"));

        myPane.getChildren().add(webView);
    }

    private String getString(String key) {
        return videoLinks.getString(key);
    }
}
