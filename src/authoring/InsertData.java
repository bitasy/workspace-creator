package authoring;

import javafx.scene.control.TabPane;

public class InsertData {

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