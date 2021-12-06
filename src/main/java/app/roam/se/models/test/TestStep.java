package app.roam.se.models.test;

import lombok.Data;
import org.json.JSONObject;

@Data
public class TestStep {
    public static final String PATH = "path";
    public static final String ACTION = "action";
    public static  final String DATA = "data";
    private String path;
    private String action;
    private String data;
    public TestStep(String path, String action, String data){
        this.path = path;
        this.action = action;
        this.data = data;
    }

    public JSONObject getJSONArray(){
        JSONObject object = new JSONObject();
        object.put(PATH,path);
        object.put(ACTION,action);
        object.put(DATA,data);
        return object;
    }
}
