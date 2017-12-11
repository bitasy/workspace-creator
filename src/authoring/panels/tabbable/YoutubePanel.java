package authoring.panels.tabbable;

import authoring.Panel;
import extensions.ExtensionWebView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import util.PropertiesReader;

import java.util.*;

/**
 * youtube panel inside the authoring environment, used to view some default videos
 * @author Kelly Zhang
 */
public class YoutubePanel implements Panel {

    private HBox myAreaOut;
    private VBox myArea;
    private List<String> videoLinks;
    private ChoiceBox<String> videosDropDown;
    private List<WebView> loadedVideos;
    private WebView myVideo;


    public YoutubePanel() {
        myAreaOut = new HBox();
        myArea = new VBox();
        myAreaOut.getChildren().add(myArea);
        myArea.fillWidthProperty().setValue(true);
        setupVideoLinkMap();
        createDropDownMenu();
        myArea.getChildren().add(videosDropDown);

        getRegion().getStyleClass().add("panel");
    }

    /**
     * creates a map for the video names to their links using the properties reader
     */
    private void setupVideoLinkMap() {
        videoLinks = new ArrayList(PropertiesReader.map("tutorials").keySet());
        //TODO: quick fix to get spaces in keys, can make better

        Collections.sort(videoLinks, String.CASE_INSENSITIVE_ORDER);

        loadedVideos = new ArrayList<>();
        for (int i = 0; i < videoLinks.size(); i++) {
            loadedVideos.add(loadVideo(PropertiesReader.value("tutorials", videoLinks.get(i))));
        }
    }

    /**
     * formats the dropdown selection for the videos
     */
    private void createDropDownMenu() {
        //https://docs.oracle.com/javafx/2/ui_controls/choice-box.htm
        videosDropDown = new ChoiceBox<>(FXCollections.observableArrayList(videoLinks));
        videosDropDown.setTooltip(new Tooltip("Select a video"));

        videosDropDown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (myArea.getChildren().contains(myVideo)) {
                    myArea.getChildren().remove(myVideo);
                }
                myVideo = loadedVideos.get(newValue.intValue());
                myArea.getChildren().add(myVideo);
            }
        });
    }

    /**
     * loads the video in a webview
     * @param video
     * @return a WebView with the embedded video loaded
     */
    private WebView loadVideo(String video) {
        //https://stackoverflow.com/questions/35204638/using-javafx-project-to-play-youtube-videos-and-control-the-playback-functionali
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(video);
        return webView;
    }


    @Override
    public Region getRegion() {
        return myAreaOut;
    }

    @Override
    public String title() {
        return "Youtube";
    }
}
