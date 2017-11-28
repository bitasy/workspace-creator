package extensions;

import javafx.scene.Node;
import javafx.scene.web.WebView;
import util.PropertiesReader;

import java.io.File;

/**
 * Class used for creating and managing webviews for the
 * extensions added to the application. Note: This class
 * cannot be directly added to a scene; the view component
 * of the class can be obtained through the getView() method
 *
 * @author Walker Willetts
 */
public class ExtensionWebView {

	/* Instance Variables */
	private WebView myView;

	/**
	 * Creates a new ExtensionWebView with no initial page loaded
	 * @param height is a {@code double} that specifies the held WebView's height
	 * @param width is a {@code double} that specifies the held WebView's width
	 */
	public ExtensionWebView(double height, double width) {
		myView = new WebView();
		myView.setPrefHeight(height);
		myView.setPrefWidth(width);
	}

	/**
	 * Creates a new ExtensionWebView with the given page loaded
	 * @param page is a {@code String} representing the initial page to load
	 *             into the WebView maintained by the ExtensionWebView
	 * @param height is a {@code double} that specifies the held WebView's height
	 * @param width is a {@code double} that specifies the held WebView's width
	 */
	public ExtensionWebView(String page, double height, double width) {
		myView = new WebView();
		myView.setPrefHeight(height);
		myView.setPrefWidth(width);
		loadHTML(page);
	}

	/**
	 * Loads the specified html page into the WebView. If page is not contained
	 * within the resources/html/ directory then nothing will be loaded.
	 * @param page is a {@code String} representing the name of the file to load
	 */
	public void loadHTML(String page) {
		File html = new File(PropertiesReader.path("html_files") + page);
		if(html.exists()) myView.getEngine().load(html.toURI().toString());
	}

	/**
	 * Loads the given URL into the WebView
	 * @param url is a {@code String} specifying the appropriate URL to load
	 */
	public void loadSite(String url) {
		myView.getEngine().load(url);
	}

	/**
	 * Sets the width of the held WebView to the given width
	 * @param width is a {@code double} specifying the new width of the WebView
	 */
	public void setWidth(double width) { myView.setPrefWidth(width); }

	/**
	 * Sets the height of the held WebView to the given height
	 * @param height is a {@code double} specifying the new height of the WebView
	 */
	public void setHeight(double height) { myView.setPrefHeight(height); }

	/**
	 * @return A {@code Node} object that displays the ExtensionWebView
	 */
	public Node getView() { return myView; }
}
