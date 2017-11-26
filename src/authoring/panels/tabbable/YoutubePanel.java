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
 * youtube panel inside the authoring environment, used to view some default videos but can also be used to browse other youtube videos
 * @author Kelly Zhang
 */
public class YoutubePanel implements Panel {

    private Pane myPane = new Pane();
    private ResourceBundle videoLinks;
    private ChoiceBox<String> videosDropDown;
    private List<String> videos;
    private List<String> links;
    private WebView myVideo;


    public YoutubePanel() {
        myPane = new Pane();
        setupVideoLinkMap();
        createDropDownMenu();

        VBox videoLayout = new VBox(5);
        videoLayout.getChildren().addAll(videosDropDown);

        myPane.getChildren().add(videoLayout);
        //myPane.getChildren().add(new VBox(videosDropDown, myVideo));
    }


    private void setupVideoLinkMap() {
        videoLinks = ResourceBundle.getBundle("tutorials");
        //https://kodejava.org/how-do-i-sort-items-in-a-set/
        videos = new ArrayList<>();
        videos.addAll(videoLinks.keySet());
        //https://stackoverflow.com/questions/2108103/can-the-key-in-a-java-property-include-a-blank-character
        //TODO: quick fix to get spaces in keys, can make better

        Collections.sort(videos, String.CASE_INSENSITIVE_ORDER);
        //TODO: should really make nodestyle a global property that can be accessed in any panel

        links = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            links.add(getString(videos.get(i)));
        }
    }

    private void createDropDownMenu() {
        //https://docs.oracle.com/javafx/2/ui_controls/choice-box.htm
        videosDropDown = new ChoiceBox<>(FXCollections.observableArrayList(videos));
        videosDropDown.getSelectionModel().selectFirst();
        videosDropDown.setStyle(getString("nodeStyle"));
        videosDropDown.setTooltip(new Tooltip(getString("tool tip")));

        videosDropDown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myVideo = playVideo(links.get(newValue.intValue()));
            }
        });
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

    @Override
    public Region getRegion() {
        return myPane;
    }

    @Override
    public String title() {
        return "Youtube";
    }
}
