package authoring.panels.tabbable;

import authoring.Panel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.*;

/**
 * youtube panel inside the authoring environment, used to view some default tutorials but can also be used to browse other youtube videos
 * @author Kelly Zhang
 */
public class YoutubePanel implements Panel {

    private Pane myPane = new Pane();
    private ResourceBundle videoLinks = ResourceBundle.getBundle("tutorials");
    private ChoiceBox<String> tutorialList;
    private List<String> linkList;
    private WebView myVideo;


    public YoutubePanel() {
        myPane = new Pane();
        videoLinks = ResourceBundle.getBundle("tutorials");
        //https://kodejava.org/how-do-i-sort-items-in-a-set/
        List<String> tutorials = new ArrayList<>();
        tutorials.addAll(videoLinks.keySet());
        Collections.sort(tutorials, String.CASE_INSENSITIVE_ORDER);
        //TODO: should really make nodestyle a global property that can be accessed in any panel

        linkList = new ArrayList<>();
        for (int i = 0; i < tutorials.size(); i++) {
            linkList.add(getString(tutorials.get(i)));
        }

        //https://docs.oracle.com/javafx/2/ui_controls/choice-box.htm
        tutorialList = new ChoiceBox<>(FXCollections.observableArrayList(tutorials));
        tutorialList.getSelectionModel().selectFirst();
        tutorialList.setStyle(getString("nodeStyle"));
        tutorialList.setTooltip(new Tooltip(getString("tool tip")));

        tutorialList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myVideo = playVideo(linkList.get(newValue.intValue()));
            }
        });

        myPane.getChildren().add(tutorialList);
        //myPane.getChildren().add(new VBox(tutorialList, myVideo));
    }

    @Override
    public Region getRegion() {
        return myPane;
    }

    @Override
    public String title() {
        return "Youtube";
    }



    private WebView playVideo(String video) {
        //https://stackoverflow.com/questions/35204638/using-javafx-project-to-play-youtube-videos-and-control-the-playback-functionali
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getString(video));

        return webView;
    }

    private String getString(String key) {
        return videoLinks.getString(key);
    }
}
