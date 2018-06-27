package UI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import gamelogic.NewObjectStorage;
import gamelogic.ObjectInspector;
import objects.NewObject;

public class Console {

    private static String input;
    private static Object[] parameters;
    private static String[] rawParameters;
    private static Method method;
    private static String methodName;
    private static Object object;
    private static String className;
    private static Class<?>[] parameterTypes;

    private static boolean selectedObject = false;
    
    private static int parameterStart = 1;
    private static String defaultClassName = Console.class.getName();

    private static String regex = " ";
    private static String objectParseRegex = ".";
    private static String selected = "selected";
    private static String gameObjectRegex = "obj:";


    private static HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>() {
        private static final long serialVersionUID = 1L;
        {
            put("default", findClass(defaultClassName));
        }
    };

    //IO
    private static ArrayList<String> log = new ArrayList<String>();

    public static void enter(String command) {
        input = command;

        //parse input into parameters and method
        boolean validInput = parse();

        //if parsing was successful
        if (validInput) {
            //determine which method to use
            determineMethod();
            
            //execute the method
            executeMethod();
        }
    }

    private static boolean parse() {
        boolean successful = true;

        //split command into parameters
        rawParameters = input.split(regex);

        //set variables
        Runnable r = () -> {
            object = null;
            methodName = rawParameters[0];
            className = defaultClassName;
            parameterStart = 1;
        };

        //extract potential object on which to invoke method
        if (rawParameters[0].contains(objectParseRegex)) {

            String param1 = rawParameters[0];
            String[] arr = param1.split(Pattern.quote(objectParseRegex));

            if (arr.length == 2) {
                object = determineObject(arr[0]);
                methodName = arr[1];
                parameterStart = 1;
            }
            else {
                successful = false;
            }
        }
        else {
            r.run();
        }

        //define parameter types
        if (rawParameters.length != 0) {
            defineParameterTypes();
        }
        else {
            parameterTypes = (Class<?>[]) null;
        }

        return successful;
    }

    private static void defineParameterTypes() {

        Class<?>[] carr = new Class[rawParameters.length - parameterStart];
        parameters = new Object[carr.length];
        
        for (int i = parameterStart; i < rawParameters.length; i++) {

            String param = rawParameters[i];
            Class<?> c = null;

            //if parameter only contains numbers it is an integer
            boolean isInteger = true;
            int integer = 0;
            try {
                integer = Integer.parseInt(param);
            }
            catch (NumberFormatException e) {isInteger = false;}
            if (isInteger) {
                c = Integer.TYPE;
                parameters[i - parameterStart] = integer;
            }
            else {
                c = String.class;
                parameters[i - parameterStart] = param;
            }

            //if parameter is boolean
            if (param.equals("true") || param.equals("false")) {
                c = Boolean.TYPE;
                parameters[i - parameterStart] = Boolean.parseBoolean(param);
            }

            carr[i - parameterStart] = c;
        }

        parameterTypes = carr;
    }

    private static Object determineObject(String name) {
        Object object = null;

        selectedObject = false;
        //if objectstring equals the syntax for choosing the currently selected object in the ObjectInspector.
        if (name.equals(selected)) {
            object = getSelectedObject();
            className = object.getClass().getName();
            selectedObject = true;
        }
        //search objectStorage for object with name
        else if (name.startsWith(gameObjectRegex)) {
            name.replace(gameObjectRegex, "");
            object = NewObjectStorage.findObjects(name);

            if (object.equals(null)) {
                object = NewObjectStorage.getObjectList().get(NewObjectStorage.getObjectList().size() - 1);
            }
            className = object.getClass().getName();
        }
        else {
            className = determineClass(name).getName();
        }

        return object;
    }

    private static Class<?> determineClass(String shortname) {
        Class<?> c = null;

        c = classes.get(shortname);
        if (c.equals(null)) {
            c = classes.get("default");
        }

        return c;
    }

    private static NewObject getSelectedObject() {
        NewObject object = null;

        object = ObjectInspector.selectedObject();

        return object;
    }

    private static Class<?> findClass(String name) {
        Class<?> c = null;

        try {
            c = Class.forName(className);
        }
        catch (Exception e) {e.printStackTrace();}

        return c;
    }

    private static void determineMethod() {
        try {
            method = findClass(className).getMethod(methodName, parameterTypes);
        }
        catch (SecurityException e) {e.printStackTrace(); logError(e.getMessage());}
        catch (NullPointerException e) {e.printStackTrace(); logError("Class not found.");}
        catch (NoSuchMethodException e) {
            e.printStackTrace(); logError("There is no method in the class " + className + " with the name " + methodName);
            //if method being called on selected object resides in superclass.
            if (selectedObject) {
                className = findClass(className).getSuperclass().getName();
                selectedObject = false;
                determineMethod();
            }
        }
    }
    
    private static void executeMethod() {
        try {
            if (method.getReturnType().equals(null)) {
                method.invoke(object, parameters);
            }
            else {
                String s = String.valueOf(method.invoke(object, parameters));
                if (!s.equals("null")) {
                    log(s);
                }
            }
        }
        catch (IllegalArgumentException e) {e.printStackTrace();}
        catch (IllegalAccessException e) {e.printStackTrace();}
        catch (InvocationTargetException e) {e.printStackTrace();}
    }

    /* IO */
    public static String LOG_GREEN = ":g:", LOG_RED = ":r:";
    public static String colorRegex = ":";
    public static int colorLength = 3;

    public static void logError(String s) {logToConsole(LOG_RED + s);}
    public static void logSuccessful(String s) {logToConsole(LOG_GREEN + s);}
    public static void log(String s) {logToConsole(s);}

    private static void logToConsole(String s) {
        log.add(s);
        CreativeUI.refreshLog();
    }

    public static String[] getLog() {
        String[] arr = new String[log.size()];
        log.toArray(arr);
        return arr;
    }

}