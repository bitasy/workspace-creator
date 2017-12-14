package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility resource for loading files from a directory. Loader can also create objects from java files in a directory.
 * @author Brian Nieves
 */
public class Loader{
    private static final String JAVA_EXT = "java";
    private static final String TITLE = "Error loading object(s).";
    private static final ErrorDisplay errorMessage = new ErrorDisplay(TITLE);

    /**
     * Returns the string names of files with matching names in a given directory.
     * @param path path to the directory
     * @param extension the extension of the files to be validated
     * @return an array of file names, extension excluded
     * @throws FileNotFoundException if the directory does not exist
     */
    public static String[] validFiles(String path, String extension) throws FileNotFoundException {
        File directory = new File(path);
        if(!directory.exists()) throw new FileNotFoundException();
        File[] files = directory.listFiles();
        List<String> names = new ArrayList<>();
        for(File file : files){
            String[] name = file.getName().split("\\.");
            if(name[name.length - 1].equals(extension))
                names.add(file.getName().substring(0, file.getName().length() - extension.length() - 1));
        }
        return names.toArray(new String[names.size()]);
    }

    /**
     * Searches a directory for java files and attempts to create an object of each one. The objects are stored in a map with class names for keys and the object as the value. All java files are expected to be concrete classes with the same constructor.
     * @param path the path to the directory
     * @param args the arguments to the constructor of the classes in the directory
     * @return a map of class names to the instances of each class
     * @throws FileNotFoundException if the directory does not exist
     */
    public static Map<String, Object> loadObjects(String path, Object... args) throws FileNotFoundException {
        Map<String, Object> objects = new HashMap<>();
        for(String name : validFiles(path, JAVA_EXT)){
            System.out.println(name);
            try {
                Class klass = Class.forName(pathToQualifier(path) + name);
                Class[] classes = new Class[args.length];
                for(int i = 0; i < args.length; i++){
                    classes[i] = checkType(args[i].getClass());
                }
                Constructor constructor = klass.getConstructor(classes);
                System.out.println(args);
                Object object = constructor.newInstance(args);
                System.out.println(object);
                objects.put(name, object);
            } catch (ClassNotFoundException e) {
                errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "nopanel"), e.getMessage()));
            } catch (NoSuchMethodException e) {
                errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "noconstructor"), e.getMessage()));
            } catch (InstantiationException e) {
                errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "noinstance"), e.getMessage()));
            } catch (IllegalAccessException e) {
                errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "hiddenconstructor"), e.getMessage()));
            } catch (InvocationTargetException e) {
                System.out.println(name);
                errorMessage.addMessage(String.format(PropertiesReader.value("reflect", "noinvocation"), e.getMessage()));
            }
        }
        errorMessage.displayError();
        return objects;
    }

    /**
     * Checks if the given class is a common primitive and returns the primitive class value. Otherwise returns the given class.
     * @param aClass the class
     * @return the class, or the primitive type if applicable
     */
    private static Class checkType(Class<?> aClass) {
        if(aClass == (Integer.class)){
            return Integer.TYPE;
        } else {

        } if(aClass == (Double.class)){
            return Double.TYPE;
        } else {

        } if(aClass == (Boolean.class)){
            return Boolean.TYPE;
        } else {

        } if(aClass == (Character.class)){
            return Character.TYPE;
        } else {
            return aClass;
        }
    }

    private static String pathToQualifier(String path){
        return path.replace("src/", "").replace("/", ".") + ".";
    }
}
