package authoring;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages the various screen positions using TabPane objects.
 * @author Brian Nieves
 */
public class Positions {

    private final Position[] positions;

    /**
     * Creates a new Positions object with the names of the positions.
     * @param names the names of each position to create
     */
    public Positions(String... names){
        Set<Position> set = new HashSet<Position>();
        for(String name : names){
            set.add(new Position(name));
        }
        positions = set.toArray(new Position[set.size()]);
    }

    /**
     * Gets the position given the name of that position.
     * @param name the position's name
     * @return the position
     */
    public Position getPosition(String name){
        for(Position position : positions){
            if(position.toString().equals(name)) return position;
        }
        return null;
    }

    /**
     * Gets the position given the associated TabPane.
     * @param newPane the TabPane
     * @return the position
     */
    public Position getPosition(TabPane newPane) {
        for(Position pos : positions){
            if(pos.pane.equals(newPane)) return pos;
        }
        return null;
    }

    /**
     * Returns the names of all of the positions.
     * @return a String array of position names
     */
    public String[] allPositions(){
        String[] names = new String[positions.length];
        for(int i = 0; i < positions.length; i++){
            names[i] = positions[i].toString();
        }
        return names;
    }

    /**
     * Represents a single position that consists of a single TabPane.
     * @author Brian Nieves
     */
    public class Position {
        private String position;
        private TabPane pane;

        private Position(String position){
            this.position = position;
            pane = new TabPane();
            pane.setMinWidth(0);
            pane.setMinHeight(0);
            pane.getStyleClass().add("dragTab");
        }

        /**
         * Adds a tab to this position's TabPane.
         * @param tab
         */
        public void addTab(Tab tab) {
            pane.getTabs().add(tab);
        }

        /**
         * Returns the inner TabPane.
         * @return tabPane
         */
        public TabPane getPane(){
            return pane;
        }

        @Override
        public String toString(){
            return position;
        }
    }
}
