package authoring;

import javafx.event.EventHandler;

/**
 * Defines the action for a MenuItem in the Menu Bar.
 * @author Brian Nieves
 */
public interface MenuAction {

    /**
     * Executes when the menu item is clicked.
     */
    void execute();
}