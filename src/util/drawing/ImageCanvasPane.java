package util.drawing;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import util.drawing.selectors.ColorSelector;
import util.drawing.selectors.StrokeSelector;
import util.drawing.selectors.ToolSelector;

/**
 * 
 * The Pane representing the full paint program -- contains an ImageCanvas,
 * ColorSelector, StrokeSelector, ToolSelector, and Save Button.
 * 
 * This is the primary class for use, and by default holds everything necessary
 * to simulate paint as a javafx component.
 * 
 * @author Ian Eldridge-Allegra
 *
 */
public class ImageCanvasPane extends BorderPane {
	private static final String DRAWING_TOOLS_PROPERTIES = ".drawingTools.drawingTools";
	private ImageCanvas imageCanvas;
	private Consumer<Image> saveTo;

	/**
	 * @param width
	 *            of the canvas
	 * @param height
	 *            of the canvas
	 * @param saveTo
	 *            The consumer to save images to -- for example, the consumer may
	 *            save to a particular file location or display the image elsewhere.
	 */
	public ImageCanvasPane(double width, double height, Consumer<Image> saveTo) {
		this(width, height);
		if (saveTo == null)
			saveTo = i -> {
			};
		this.saveTo = saveTo;
	}

	/**
	 * @param width
	 *            of the canvas
	 * @param height
	 *            of the canvas
	 * @param fileChooser
	 *            The image will use this to decide where to save the file.
	 */
	public ImageCanvasPane(double width, double height, Supplier<File> fileChooser) {
		this(width, height);
		final Supplier<File> chooser;
		if (fileChooser == null)
			chooser = () -> null;
		else
			chooser = fileChooser;
		saveTo = i -> saveTo(i, chooser.get());
	}

	private ImageCanvasPane(double width, double height) {
		imageCanvas = new ImageCanvas(width, height);
		setCenter(imageCanvas);
		setTop(new FlowPane());
		addToTop(new ColorSelector(c -> imageCanvas.setColor(c)));
		addToTop(new ToolSelector(imageCanvas, getToolsAndNames()));
		addToTop(new StrokeSelector(s -> imageCanvas.setStroke(s)));
		addSaveButton();
	}

	private ResourceBundle getToolsAndNames() {
		return ResourceBundle.getBundle(getClass().getPackage().getName() + DRAWING_TOOLS_PROPERTIES);
	}

	private void addSaveButton() {
		Button b = new Button("Save");
		b.setOnAction(e -> saveTo.accept(getImage()));
		addToTop(b);
	}

	private void addToTop(Node n) {
		((Pane) getTop()).getChildren().add(n);
	}

	private Image getImage() {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		Image image = imageCanvas.snapshot(params,
				new WritableImage((int) imageCanvas.getWidth(), (int) imageCanvas.getHeight()));
		return image;
	}

	private void saveTo(Image image, File location) {
		if (location == null || image == null)
			return;
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", location);
		} catch (IOException e) {
			throw new PaintException("Illegal File: " + location.getAbsolutePath());
		}
	}
}
