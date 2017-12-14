package util.drawing.selectors;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

/**
 * Allows Colors to be chosen and notifies listener.
 * 
 * @author Ian Eldridge-Allegra
 *
 */
public class ColorSelector extends ColorPicker {
	private static final Color DEFAULT_COLOR = Color.BLACK;
	
	public ColorSelector(Consumer<Color> listener) {
		this(listener, DEFAULT_COLOR);
	}
	
	public ColorSelector(Consumer<Color> listener, Color startingColor) {
		super(startingColor);
		setOnAction(e -> listener.accept(getValue()));
		listener.accept(getValue());
	}
}
