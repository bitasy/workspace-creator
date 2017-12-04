package authoring;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.HashSet;
import java.util.Set;

public class Positions {

    private final Position[] positions;

    public Positions(String... names){
        Set<Position> set = new HashSet<Position>();
        for(String name : names){
            set.add(new Position(name));
        }
        positions = set.toArray(new Position[set.size()]);
    }

    public Position getPosition(String name){
        for(Position position : positions){
            if(position.toString().equals(name)) return position;
        }
        return null;
    }

    public String[] allPositions(){
        String[] names = new String[positions.length];
        for(int i = 0; i < positions.length; i++){
            names[i] = positions[i].toString();
        }
        return names;
    }

    public class Position {
        private String position;
        private TabPane pane;

        private Position(String position){
            this.position = position;
            pane = new TabPane();
            pane.setMinWidth(0);
            pane.setMinHeight(0);
        }

        public void addTab(Tab tab) {
            pane.getTabs().add(tab);
        }

        public TabPane getPane(){
            return pane;
        }

        @Override
        public String toString(){
            return position;
        }
    }
}
