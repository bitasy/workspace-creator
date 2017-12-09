package authoring;

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.VoogaPeaches;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * TabManager handles the tabs and their location in the various TabPanes on the screen for a workspace. It uses and conforms with the requirements of the inner VoogaTab class, and allows for workspace customization.
 * @author Brian Nieves
 */
public class TabManager {
    private final List<TabPane> tabPanes = new ArrayList<>();
    private final Stage markerStage;

    /**
     * Creates a new TabManager, adds the panes for every position in the workspace, and initializes the indicator for moving DraggableTabs.
     * @param positions all positions specified by the workspace.
     */
    public TabManager(Positions positions) {
        for(String position : positions.allPositions()){
            tabPanes.add(positions.getPosition(position).getPane());
        }
        markerStage = new Stage();
        markerStage.setAlwaysOnTop(true);
        markerStage.initStyle(StageStyle.UNDECORATED);
        Rectangle dummy = new Rectangle(3, 10, Color.web("#555555"));
        StackPane markerStack = new StackPane();
        markerStack.getChildren().add(dummy);
        Scene myScene = new Scene(markerStack);
        myScene.getStylesheets().add(VoogaPeaches.getUser().getThemeName());
        PubSub.getInstance().subscribe(
                "THEME_MESSAGE",
                (message) -> {
                    if (myScene.getStylesheets().size() >= 1) {
                        myScene.getStylesheets().remove(0);
                    }
                    myScene.getStylesheets().add(((StringMessage) message).readMessage());
                });
        markerStage.setScene(myScene);

    }

    /**
     * Removes the specified panel from its tabPane, if it exists.
     * @param panel the name of the panel to be removed
     */
    public void remove(String panel){
        for(TabPane tabs : tabPanes){
            for(Tab tab : tabs.getTabs()){
                String tabpanel = ((VoogaTab)tab).getPanelName();
                if(tabpanel.equals(panel)){
                    tabs.getTabs().remove(tab);
                    return;
                }
            }
        }
    }

    /**
     * Creates a new tab of the correct type to be added to any tabPane on the screen.
     * @param title the title of the tab
     * @return the new tab
     */
    public Tab newTab(String title){
        VoogaTab tab = new VoogaTab(title, markerStage, tabPanes);
        if(onTabClose == null) throw new IllegalStateException();
        tab.setOnCloseRequest(e -> onTabClose.accept(e));
        return tab;
    }

    private Consumer<Event> onTabClose;

    public void setOnTabClose(Consumer<Event> close){
        this.onTabClose = close;
    }
}