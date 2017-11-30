package util;

import java.io.File;
import java.util.*;

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

    /**
     * Reads in all the property files from the /resources/properties folder
     * @return A {@code Map<String, ResourceBundle>} that contains all the
     * properties files mapped to their respective resource bundle
     */
    private static Map<String, ResourceBundle> readInPropertyFiles() {
        Map<String, ResourceBundle> propertyBundles = new HashMap<>();
        File propertiesFolder = new File(PROPERTIES_FILES_DIRECTORY);
        for(File file : propertiesFolder.listFiles()) {
            if(isPropertiesFile(file)){
                // Remove the .properties suffix at the end of the string and at the bundle to the map
                String fileName = file.getName().substring(0, file.getName().length() - PROPERTIES_SUFFIX.length() - 1);
                propertyBundles.put(fileName, ResourceBundle.getBundle(fileName));
            }
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
            //TODO: reload from database to try to find the correct propertiesFile and/or key
            // if that fails then return empty string/error message
            return "";
        }
    }

    /**
     * Retrieves the set of keys
     * @param propertiesFile is a {@code String} representing the properties file (without the
     *                       extension) to look for the key inside of
     * @return {@code String} representing an ArrayList of all the keys in the properties file
     */
    public static ArrayList<String> keySet(String propertiesFile) {
        try {
            return new ArrayList(propertyBundles.keySet());
        } catch (Exception e) {
            return new ArrayList<String>(Arrays.asList("hi"));
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
