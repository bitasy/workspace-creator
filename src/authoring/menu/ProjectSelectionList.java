package authoring.menu;

import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.Map;

public class ProjectSelectionList extends ListView<String> {

    private static final String GAME_NAMES = "gameNames";
    private Map<String, String> gameUIDS;

    public ProjectSelectionList(double width, double height) {
        gameUIDS = new HashMap<>();
        this.setPrefSize(width,height);
        loadGameList();
        applyStyles();
    }

    private void applyStyles() { }

    private void loadGameList() {
        //todo: replace this with something that looks at the files user workspace files?
    }

    public String getSelectedUID(){
        if(this.getSelectionModel().getSelectedItem() != null)
            return gameUIDS.get(this.getSelectionModel().getSelectedItem());
        return null;
    }
}