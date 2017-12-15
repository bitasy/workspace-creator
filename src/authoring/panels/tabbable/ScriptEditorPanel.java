package authoring.panels.tabbable;

import authoring.Panel;
import authoring.PanelController;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import util.ErrorDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ScriptEditorPanel implements Panel {

    private static final String NAME_HINT = "File Name";
    private static final String SAVE = "Save Script";
    private static final String LOAD = "Load Script";
    private static final String DIRECTORY = "data/filedata/scripts/";
    private static final String SCRIPT_CREATOR = "Script Creator";
    public static final String FILE_NOT_FOUND = "File not Found";
    public static final String SCRIPT_EDITOR_CAN_T_LOAD_STUFF = "Script Editor can't load stuff";
    private static final String LOAD_TITLE = "Choose groovy script";
    private static final String GROOVY = "*.groovy";
    private static final String SCRIPT = "Groovy Script";

    private Button save = new Button(SAVE);
    private Button load =new Button(LOAD);
    private TextArea editor = new TextArea();
    private TextField name = new TextField();

    public ScriptEditorPanel(){
        name.setPromptText(NAME_HINT);
        save.setOnAction(e -> {
            if(!name.getText().trim().isEmpty()){
                File script = new File(DIRECTORY + name.getText() + ".groovy");
                PrintStream writer = null;
                try {
                    writer = new PrintStream(script);
                    writer.print(editor.getText());
                    writer.close();
                } catch (FileNotFoundException e1) {
                    new ErrorDisplay(FILE_NOT_FOUND, SCRIPT_EDITOR_CAN_T_LOAD_STUFF).displayError();
                }
            }
        });

        FileChooser scriptchoice = new FileChooser();
        scriptchoice.setTitle(LOAD_TITLE);
        scriptchoice.setInitialDirectory(new File(DIRECTORY));
        scriptchoice.getExtensionFilters().add(new FileChooser.ExtensionFilter(SCRIPT, GROOVY));
        load.setOnAction(e -> {
            File potentialScript = scriptchoice.showOpenDialog(null);
            if(potentialScript.exists()){
                try {
                    Scanner s = new Scanner(potentialScript);
                    editor.clear();
                    while(s.hasNextLine()){
                        editor.appendText(s.nextLine()+"\n");
                    }
                    String filename = potentialScript.getName();
                    name.setText(filename.substring(0, filename.indexOf(".")));
                    s.close();
                } catch (FileNotFoundException e1) {
                    new ErrorDisplay(FILE_NOT_FOUND, SCRIPT_EDITOR_CAN_T_LOAD_STUFF).displayError();
                }
            }
        });

    }

    @Override
    public Region getRegion() {
        HBox top = new HBox(name, save, load);
        VBox.setVgrow(editor, Priority.ALWAYS);
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
