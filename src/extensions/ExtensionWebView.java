package extensions;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URL;


public class ExtensionWebView extends Dialog {

	private static final double VIEW_SIZE = 400;
	private static final String HTML_DIR = "resources/html/";

	protected WebView myView;

	public ExtensionWebView(String page) {
		addWebView();
		addCloseButton();
		loadHTML(page);
	}

	public void loadHTML(String page) {
		File html = new File(HTML_DIR + "SocialMedia.html");
		myView.getEngine().load(html.toURI().toString());
	}

	public void loadSite(String url) {
		myView.getEngine().load(url);
	}

	private void addCloseButton() {
		this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		Node closeButton = this.getDialogPane().lookupButton(ButtonType.CLOSE);
		closeButton.managedProperty().bind(closeButton.visibleProperty());
		closeButton.setVisible(false);
	}

	private void addWebView() {
		myView = new WebView();
		myView.setPrefHeight(VIEW_SIZE);
		myView.setPrefWidth(VIEW_SIZE);
		this.getDialogPane().setContent(myView);
	}
}
