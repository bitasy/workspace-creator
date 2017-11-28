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

    private Pane myPane;
    private VBox videoLayout;
    private ResourceBundle videoLinks;
    private ChoiceBox<String> videosDropDown;
    private List<String> videos;
    private List<String> links;
    private List<WebView> loadedVideos;
    private WebView myVideo;


    public YoutubePanel() {
        myPane = new Pane();
        myPane.getStyleClass().add("testPane");
        setupVideoLinkMap();
        createDropDownMenu();

        videoLayout = new VBox();
        videoLayout.getChildren().add(videosDropDown);

        myPane.getChildren().add(videoLayout);
    }


    private void setupVideoLinkMap() {
        videoLinks = ResourceBundle.getBundle("tutorials");
        //https://kodejava.org/how-do-i-sort-items-in-a-set/
        videos = new ArrayList<>();
        videos.addAll(videoLinks.keySet());
        videos.removeAll(new ArrayList<String>(Arrays.asList("nodeStyle", "tool tip", "Information")));
        //https://stackoverflow.com/questions/2108103/can-the-key-in-a-java-property-include-a-blank-character
        //TODO: quick fix to get spaces in keys, can make better

        Collections.sort(videos, String.CASE_INSENSITIVE_ORDER);
        //TODO: should really make nodestyle a global property that can be accessed in any panel

        links = new ArrayList<>();
        loadedVideos = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            links.add(videoLinks.getString(videos.get(i)));
            loadedVideos.add(loadVideo(links.get(i)));
        }
    }

    private void createDropDownMenu() {
        //https://docs.oracle.com/javafx/2/ui_controls/choice-box.htm
        videosDropDown = new ChoiceBox<>(FXCollections.observableArrayList(videos));
        videosDropDown.getSelectionModel().selectFirst();
        videosDropDown.setStyle(videoLinks.getString("nodeStyle"));
        videosDropDown.setTooltip(new Tooltip(videoLinks.getString("tool tip")));

        videosDropDown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (videoLayout.getChildren().contains(myVideo)) {
                    videoLayout.getChildren().remove(myVideo);
                }
                myVideo = loadedVideos.get(newValue.intValue());
                videoLayout.getChildren().add(myVideo);
            }
        });
    }

    private WebView loadVideo(String video) {
        //https://stackoverflow.com/questions/35204638/using-javafx-project-to-play-youtube-videos-and-control-the-playback-functionali
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(video);
        return webView;
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
