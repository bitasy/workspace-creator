package util.drawing.drawingTools;

import javafx.geometry.Point2D;
import util.drawing.ImageCanvas;

/**
 * Draws smooth curves on the canvas. 
 * 
 * @author Ian Eldridge-Allegra
 */
public class CurveDrawer extends SmoothDrawer {

	public CurveDrawer(String name, ImageCanvas canvas) {
		super(name, canvas);
	}

	@Override
	protected void draw(Point2D lastLoc, Point2D point) {
		canvas.drawLine(lastLoc, point);
	}
}
