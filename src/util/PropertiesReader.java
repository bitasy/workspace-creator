package util;

import javafx.application.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Static class that reads in the properties files and returns requested
 * values from specific properties files
 *
 * @author  Walker Willetts
 */
public class PropertiesReader {

    /* Final Variables */
    private static final Map<String, ResourceBundle> propertyBundles = readInPropertyFiles();
    private static final String PROPERTIES_FILES_DIRECTORY = "./resources/properties/";
    private static final String PROPERTIES_SUFFIX = "properties";
    private static final String PATH_PROPERTIES = "filepaths";
    private static final String NO_PROPERTIES = "No Properties Directory";
    private static final String NO_PROP_MESSAGE = String.format("Could not find the directory %s. Settings could not be loaded.", PROPERTIES_FILES_DIRECTORY);
    private static final String NO_PROP_FOUND = "The specified property or properties files is not loaded or does not exist.";

    /**
     * Reads in all the property files from the /resources/properties folder
     * @return A {@code Map<String, ResourceBundle>} that contains all the
     * properties files mapped to their respective resource bundle
     */
    private static Map<String, ResourceBundle> readInPropertyFiles() {
        Map<String, ResourceBundle> propertyBundles = new HashMap<>();
        String[] propfiles = new String[0];
        try {
            propfiles = Loader.validFiles(PROPERTIES_FILES_DIRECTORY, PROPERTIES_SUFFIX);
        } catch (FileNotFoundException e) {
            ErrorDisplay ed = new ErrorDisplay(NO_PROPERTIES);
            ed.addMessage(NO_PROP_MESSAGE);
            ed.displayError();
            Platform.exit();
        }

        for(String file : propfiles) {
            propertyBundles.put(file, ResourceBundle.getBundle(file));
        }
        return propertyBundles;
    }

    /**
     * Checks to make sure that the passed in file is a proper properties file
     * @param file is a {@code File} representing the file being checked as a properties file
     * @return {@code true} if the passed in file is a properties file, and {@code false} otherwise
     */
    private static boolean isPropertiesFile(File file) {
        String[] splitFileName = file.getName().split("\\.");
        if(splitFileName.length < 2 || !splitFileName[splitFileName.length - 1].equals(PROPERTIES_SUFFIX)) return false;
        return true;
    }

    /**
     * Retrieves the key value
     * @param propertiesFile is a {@code String} representing the properties file (without the
     *                       extension) to look for the key inside of
     * @param key is a {@code String} representing the key whose value needs to be retrieved
     *            from the specified properties file
     * @return {@code String} representing the value matching up to the provided key
     */
    public static String value(String propertiesFile, String key) {
        try {
            return (String) propertyBundles.get(propertiesFile).getObject(key);
        } catch (Exception e) {
            throw new IllegalStateException(NO_PROP_FOUND);
        }
    }

    /**
     * Helper used to retrieve the directory path for the given key. This method is the only
     * additional properties file method added as retrieving directory paths is extremely common
     * @param path is a {@code String} that represents the path to retrieve
     */
    public static String path(String path) {
        return value(PATH_PROPERTIES, path);
    }

}
