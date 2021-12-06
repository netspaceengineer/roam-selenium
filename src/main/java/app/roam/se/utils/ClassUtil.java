package app.roam.se.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    public static List<String> getEntityActions(Class c){
        List<String> actions = new ArrayList<String>();
        List<String> exclude = new ArrayList<>();
        for(Method m: ClassUtil.class.getMethods()){
            exclude.add(m.getName());
        }
        exclude.add("getElement");
        exclude.add("getActions");
        for(Method m: c.getMethods()){
           if(!exclude.contains(m.getName())) {
               actions.add(m.getName().toUpperCase());
           }
        }
        return actions;
    }
}
