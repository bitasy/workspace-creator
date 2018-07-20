package authoring;

import com.google.gson.Gson;
import util.ErrorDisplay;
import util.PropertiesReader;
import util.pubsub.PubSub;
import util.pubsub.messages.StringMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User object that is used to store information about workspace and theme preferences.
 *
 * @author Kelly Zhang
 * @author Simran Singh
 */
public class User {

    private String userName;
    private String themeName;
    private String workspaceName;
    private Map<String, Map<String, String>> properties;
    private ArrayList<String> projects;


    private transient ErrorDisplay errorMessage;

    public User(String name) {
        userName = name;
        themeName = PropertiesReader.value("defaults","theme");
        workspaceName = PropertiesReader.value("defaults", "workspace");
        projects = new ArrayList<>();
        properties = new HashMap<>();

        errorMessage = new ErrorDisplay(PropertiesReader.value("reflect", "errortitle"));

        PubSub.getInstance().subscribe(
                "THEME MESSAGE",
                (message) -> themeName = ((StringMessage)message).readMessage()
        );
    }

    /**
     * Create a user reflectively from a JSON file.
     */
    private User(){}

    /**
     * @return the username associated with the profile
     */
    public String getUserName() {
        return userName;
    }

    /**
     * sets the user's theme to the current one in use
     * @param theme the new theme
     */
    public void setTheme(String theme) {
        themeName = theme;
    }

    public void setWorkspace(String workspace) {workspaceName = workspace;}

    public String getWorkspaceName() { return workspaceName; }

    /**
     * @return the string of the current theme that the user has active in the authoring environment
     */
    public String getThemeName() {
        return themeName;
    }

    public Map<String, Map<String, String>> getProperties() { return properties; }

    public ArrayList<String> getProjects() { return projects; }

    public void addProject(String project) { projects.add(project); }

    public void save(){
        String path = PropertiesReader.value("filepaths", "users");
        try(Writer writer = new OutputStreamWriter(new FileOutputStream(path + userName + ".json") , StandardCharsets.UTF_8)){

            Gson gson = new Gson();
            gson.toJson(this, writer);
        } catch (FileNotFoundException e) {
            errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "nopath"), path));
            errorMessage.displayError();
        } catch (IOException e) {
            errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "IOerror"), e.getMessage()));
            errorMessage.displayError();
        }
    }
}
