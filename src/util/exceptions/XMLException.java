package util.exceptions;

/**
 * Created to implement an exception caused by parsing the XML used to define the MenuBar.
 * @author Brian Nieves
 */
public class XMLException extends RuntimeException {
    public String getMessage() {
        return "Could not load menu bar. Check the XML file.";
    }
}
