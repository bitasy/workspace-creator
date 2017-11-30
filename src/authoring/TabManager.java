package authoring;

import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.pubsub.PubSub;
import util.pubsub.messages.ThemeMessage;

import java.util.ArrayList;
import java.util.List;


/**
 * TabManager handles the tabs and their location in the various TabPanes on the screen for a workspace. It uses and conforms with the requirements of the inner DraggableTab class, and allows for workspace customization.
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
        PubSub.getInstance().subscribe(
                PubSub.Channel.THEME_MESSAGE,
                (message) -> myScene.getStylesheets().add(((ThemeMessage) message).readMessage()));
        markerStage.setScene(myScene);
    }

    /**
     * Creates a new tab of the correct type to be added to any tabPane on the screen.
     * @param title the title of the tab
     * @return the new tab
     */
    public Tab newTab(String title){
        return new DraggableTab(title);
    }

    public void setOnWindowClose(){

    }

    /**
     * A draggable tab that can optionally be detached from its tab pane and shown
     * in a separate window. This can be added to any normal TabPane, however a
     * TabPane with draggable tabs must *only* have DraggableTabs, normal tabs and
     * DraggableTabs mixed will cause issues! Edited for reuse by Brian Nieves.
     * <p>
     * @author Michael Berry
     * @author Brian Nieves
     * @see <a href = "http://berry120.blogspot.co.uk/2014/01/draggable-and-detachable-tabs-in-javafx.html">Draggable and detachable tabs in JavaFX 2</a>
     */
    public class DraggableTab extends Tab {

        private Label nameLabel;
        private Text dragText;
        private Stage dragStage;
        private boolean detachable;

        /**
         * Create a new draggable tab. This can be added to any normal TabPane,
         * however a TabPane with draggable tabs must *only* have DraggableTabs,
         * normal tabs and DraggableTabs mixed will cause issues!
         * <p>
         * @param text the text to appear on the tag label.
         */
        public DraggableTab(String text) {
            nameLabel = new Label(text);
            setGraphic(nameLabel);
            detachable = true;
            dragStage = new Stage();
            dragStage.setAlwaysOnTop(true);
            dragStage.initStyle(StageStyle.UNDECORATED);
            StackPane dragStagePane = new StackPane();
            dragStagePane.setStyle("-fx-background-color:#DDDDDD;");
            dragText = new Text(text);
            StackPane.setAlignment(dragText, Pos.CENTER);
            dragStagePane.getChildren().add(dragText);
            dragStage.setScene(new Scene(dragStagePane));
            nameLabel.setOnMouseDragged(t -> {
                dragStage.setWidth(nameLabel.getWidth() + 10);
                dragStage.setHeight(nameLabel.getHeight() + 10);
                dragStage.setX(t.getScreenX());
                dragStage.setY(t.getScreenY());
                dragStage.show();
                Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                InsertData data = getInsertData(screenPoint);
                if(data == null || data.getInsertPane().getTabs().isEmpty()) {
                    markerStage.hide();
                }
                else {
                    int index = data.getIndex();
                    boolean end = false;
                    if(index == data.getInsertPane().getTabs().size()) {
                        end = true;
                        index--;
                    }
                    Rectangle2D rect = getAbsoluteRect(data.getInsertPane().getTabs().get(index));
                    if(end) {
                        markerStage.setX(rect.getMaxX() + 13);
                    }
                    else {
                        markerStage.setX(rect.getMinX());
                    }
                    markerStage.setY(rect.getMaxY() + 10);
                    markerStage.show();
                }
            });
            nameLabel.setOnMouseReleased(t -> {
                markerStage.hide();
                dragStage.hide();
                if(!t.isStillSincePress()) {
                    Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                    TabPane oldTabPane = getTabPane();
                    int oldIndex = oldTabPane.getTabs().indexOf(DraggableTab.this);
                    InsertData insertData = getInsertData(screenPoint);
                    if(insertData != null) {
                        int addIndex = insertData.getIndex();
                        if(oldTabPane == insertData.getInsertPane() && oldTabPane.getTabs().size() == 1) {
                            return;
                        }
                        oldTabPane.getTabs().remove(DraggableTab.this);
                        if(oldIndex < addIndex && oldTabPane == insertData.getInsertPane()) {
                            addIndex--;
                        }
                        if(addIndex > insertData.getInsertPane().getTabs().size()) {
                            addIndex = insertData.getInsertPane().getTabs().size();
                        }
                        insertData.getInsertPane().getTabs().add(addIndex, DraggableTab.this);
                        insertData.getInsertPane().selectionModelProperty().get().select(addIndex);
                        return;
                    }
                    if(!detachable) {
                        return;
                    }
                    Stage newStage = new Stage();
                    TabPane pane = new TabPane();
                    tabPanes.add(pane);
                    newStage.setOnHiding(t1 -> {
                        tabPanes.remove(pane);
                        tabPanes.get(0).getTabs().addAll(((TabPane)newStage.getScene().getRoot()).getTabs());
                    });
                    getTabPane().getTabs().remove(DraggableTab.this);
                    pane.getTabs().add(DraggableTab.this);
                    pane.getTabs().addListener((ListChangeListener<Tab>) change -> {
                        if(pane.getTabs().isEmpty()) {
                            newStage.hide();
                        }
                    });
                    Scene newScene = new Scene(pane);
                    PubSub.getInstance().subscribe(
                            PubSub.Channel.THEME_MESSAGE,
                            (message) -> newScene.getStylesheets().add(((ThemeMessage) message).readMessage()));
                    //newScene.getStylesheets().add("panel");
                    newStage.setScene(newScene);
                    newStage.initStyle(StageStyle.UTILITY);
                    newStage.setX(t.getScreenX());
                    newStage.setY(t.getScreenY());
                    newStage.setWidth(oldTabPane.getWidth());
                    newStage.setHeight(oldTabPane.getHeight());
                    newStage.show();
                    newStage.setAlwaysOnTop(true);
                    pane.requestLayout();
                    pane.requestFocus();
                }
            });
        }

        /**
         * Set whether it's possible to detach the tab from its pane and move it to
         * another pane or another window. Defaults to true.
         * <p>
         * @param detachable true if the tab should be detachable, false otherwise.
         */
        public void setDetachable(boolean detachable) {
            this.detachable = detachable;
        }

        /**
         * Set the label text on this draggable tab. This must be used instead of
         * setText() to set the label, otherwise weird side effects will result!
         * <p>
         * @param text the label text for this tab.
         */
        public void setLabelText(String text) {
            nameLabel.setText(text);
            dragText.setText(text);
        }

        private InsertData getInsertData(Point2D screenPoint) {
            for(TabPane tabPane : tabPanes) {
                Rectangle2D tabAbsolute = getAbsoluteRect(tabPane);
                if(tabAbsolute.contains(screenPoint)) {
                    int tabInsertIndex = 0;
                    if(!tabPane.getTabs().isEmpty()) {
                        Rectangle2D firstTabRect = getAbsoluteRect(tabPane.getTabs().get(0));
                        if(firstTabRect.getMaxY()+60 < screenPoint.getY() || firstTabRect.getMinY() > screenPoint.getY()) {
                            return null;
                        }
                        Rectangle2D lastTabRect = getAbsoluteRect(tabPane.getTabs().get(tabPane.getTabs().size() - 1));
                        if(screenPoint.getX() < (firstTabRect.getMinX() + firstTabRect.getWidth() / 2)) {
                            tabInsertIndex = 0;
                        }
                        else if(screenPoint.getX() > (lastTabRect.getMaxX() - lastTabRect.getWidth() / 2)) {
                            tabInsertIndex = tabPane.getTabs().size();
                        }
                        else {
                            for(int i = 0; i < tabPane.getTabs().size() - 1; i++) {
                                Tab leftTab = tabPane.getTabs().get(i);
                                Tab rightTab = tabPane.getTabs().get(i + 1);
                                if(leftTab instanceof DraggableTab && rightTab instanceof DraggableTab) {
                                    Rectangle2D leftTabRect = getAbsoluteRect(leftTab);
                                    Rectangle2D rightTabRect = getAbsoluteRect(rightTab);
                                    if(betweenX(leftTabRect, rightTabRect, screenPoint.getX())) {
                                        tabInsertIndex = i + 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    return new InsertData(tabInsertIndex, tabPane);
                }
            }
            return null;
        }

        private Rectangle2D getAbsoluteRect(Control node) {
            return new Rectangle2D(node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getX() + node.getScene().getWindow().getX(),
                    node.localToScene(node.getLayoutBounds().getMinX(), node.getLayoutBounds().getMinY()).getY() + node.getScene().getWindow().getY(),
                    node.getWidth(),
                    node.getHeight());
        }

        private Rectangle2D getAbsoluteRect(Tab tab) {
            Control node = ((DraggableTab) tab).getLabel();
            return getAbsoluteRect(node);
        }

        private Label getLabel() {
            return nameLabel;
        }

        private boolean betweenX(Rectangle2D r1, Rectangle2D r2, double xPoint) {
            double lowerBound = r1.getMinX() + r1.getWidth() / 2;
            double upperBound = r2.getMaxX() - r2.getWidth() / 2;
            return xPoint >= lowerBound && xPoint <= upperBound;
        }


    }

    private static class InsertData {

        private final int index;
        private final TabPane insertPane;

        InsertData(int index, TabPane insertPane) {
            this.index = index;
            this.insertPane = insertPane;
        }

        int getIndex() {
            return index;
        }

        TabPane getInsertPane() {
            return insertPane;
        }

    }
}
