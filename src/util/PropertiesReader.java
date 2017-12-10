package util;

import java.io.File;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Static class that reads in the properties files and returns requested
 * values from specific properties files
 *
 * @author  Walker Willetts
 * @author Kelly Zhang
 */
public class PropertiesReader {

    /* Final Variables */
    public static final String PROPERTIES_SUFFIX = "properties";
    private static final String PROPERTIES_FILES_DIRECTORY = "./resources/properties/";
    private static final Map<String, ResourceBundle> propertyBundles = readInPropertyFiles(PROPERTIES_FILES_DIRECTORY);
    private static final String PATH_PROPERTIES = "filepaths";
    private static final String NO_PROPERTIES = "No Properties Directory";
    private static final String NO_PROP_MESSAGE = String.format("Could not find the directory %s. Settings could not be loaded.", PROPERTIES_FILES_DIRECTORY);
    private static final String NO_PROP_FOUND = "The specified property or properties files is not loaded or does not exist.";

    /**
     * Reads in all the property files from the /resources/properties folder
     * @return A {@code Map<String, ResourceBundle>} that contains all the
     * properties files mapped to their respective resource bundle
     */
    private static Map<String, ResourceBundle> readInPropertyFiles(String folder) {
        Map<String, ResourceBundle> propertyBundles = new HashMap<>();

        File propertiesFolder = new File(folder);
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
            System.out.println(propertiesFile);
            throw new IllegalStateException(NO_PROP_FOUND);
        }
    }

    /**
     * Retrieves a Mapping of String to String corresponding to key to value
     * @param propertiesFile is a {@code String} representing the properties file (without the
     *                       extension) to look for the key inside of
     * @return {@code String} representing an ArrayList of all the keys in the properties file
     */
    public static Map<String, String> map(String propertiesFile) {
        try {
            Map<String, String> propertyMap = new HashMap<>();
            for(String key: propertyBundles.get(propertiesFile).keySet()) {
                String value = propertyBundles.get(propertiesFile).getString(key);
                propertyMap.put(key, value);
            }
            return propertyMap;
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
