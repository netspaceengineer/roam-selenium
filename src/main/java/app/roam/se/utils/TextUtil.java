package app.roam.se.utils;



import java.util.ArrayList;
import java.util.List;

public class TextUtil {
    public static boolean isAlphaNumeric(String text) {
        return text.matches("^[a-zA-Z0-9-_. ,]*$");
    }

    public static boolean isBlank(String text) {
        try {
            return text.equals("");
        }catch(Exception ex) {
            return false;
        }
    }

    public static String prepend(String root, String toprepend) {
        return toprepend + root;
    }

    public static String append(String root, String append) {
        return root + append;
    }

    public static String prepend(String root, String toprepend, String delimeter) {
        if (root.equals("")) {
            delimeter="";
        }
        return toprepend + delimeter + root;
    }

    public static String append(String root, String append, String delimeter) {
        if (root.equals("")) {
            delimeter="";
        }
        return root + delimeter + append;
    }

    public static  ArrayList<String> removeDuplicates(List<String> envi)
    {


        ArrayList<String> newList = new ArrayList<String>();

        // Traverse through the first list
        for (String element : envi) {


            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public static String toOrdinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    public static String replace(String main, String wildcard) {
        return main.replace("%s", wildcard);
    }
    public static boolean isValidName(String text){
        return isAlphaNumeric(text) && !isBlank(text);
    }

}
