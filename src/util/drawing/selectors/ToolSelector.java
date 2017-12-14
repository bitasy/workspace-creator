package util.drawing.selectors;

import javafx.scene.control.ChoiceBox;
import util.drawing.DrawingTool;
import util.drawing.ImageCanvas;
import util.drawing.ToolFactory;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Allows Tools to be chosen and notifies listener. By default, uses those in
 * the drawingTools properties file.
 * 
 * @author Ian Eldridge-Allegra
 *
 */
public class ToolSelector extends ChoiceBox<DrawingTool> {

	private ImageCanvas canvas;

	public ToolSelector(ImageCanvas canvas, ResourceBundle bundle) {
		this.canvas = canvas;
		setTools(ToolFactory.getTools(canvas, bundle));
		setOnAction(e -> canvas.setTool(getValue()));
	}

	/**
	 * @param tools
	 *            Sets the possible tools, replacing the old ones.
	 */
	public void setTools(List<DrawingTool> tools) {
		getItems().clear();
		getItems().addAll(tools);
		getSelectionModel().selectFirst();
		canvas.setTool(getValue());
	}

}
