package util.pubsub.messages;

import javafx.scene.control.TabPane;

public class MoveTabMessage extends Message{
    private String myTabName;
    private TabPane myTabPane;

    public MoveTabMessage(String tab, TabPane pane) {
        myTabName = tab;
        myTabPane = pane;
    }

    public String tab() {
        return myTabName;
    }

    public TabPane pane() {
        return myTabPane;
    }
}
