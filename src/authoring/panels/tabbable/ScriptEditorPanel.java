package authoring.panels.tabbable;

import authoring.Panel;
import authoring.PanelController;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ScriptEditorPanel implements Panel {

    private static final String NAME_HINT = "File Name";
    private static final String SAVE = "Save Script";
    private static final String DIRECTORY = "data/scripts/";
    private static final String SCRIPT_CREATOR = "Script Creator";

    private Button save = new Button(SAVE);
    private TextArea editor = new TextArea();
    private TextField name = new TextField();

    public ScriptEditorPanel(){
        name.setPromptText(NAME_HINT);
        save.setOnAction(e -> {
            if(!name.getText().trim().isEmpty()){
                File script = new File(DIRECTORY + name.getText() + ".groovy");
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(script);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                writer.print(editor.getText());
                writer.close();
            }
        });
    }

    @Override
    public Region getRegion() {
        HBox top = new HBox(name, save);

        VBox panel = new VBox(top, editor);
        return panel;
    }

    @Override
    public void setController(PanelController controller) {

    }

    @Override
    public String title() {
        return SCRIPT_CREATOR;
    }
}
