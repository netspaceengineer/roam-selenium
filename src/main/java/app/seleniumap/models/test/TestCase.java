package app.seleniumap.models.test;

import app.seleniumap.utils.FilesUtil;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class TestCase {
    public final static String NAME ="name";
    public final static String FEATURES = "features";
    public final static String DESCRIPTION = "description";
    private String name="";
    private List<String> featurePaths = new ArrayList<String>();
    private String description="";
    private String location ="";
    public void initialize(String path){
        this.location = path;

        String raw = FilesUtil.readFile(path);
        JSONObject object = new JSONObject(raw);
        setName(object.getString(NAME));
        setDescription(object.getString(DESCRIPTION));
        JSONArray features = object.getJSONArray(FEATURES);
        for(int i = 0 ; i<features.length();i++){
            featurePaths.add((String) features.get(i));
        }
    }

    public void saveTestCase(String path, String variant){
        JSONObject object = new JSONObject();
        object.put(NAME, getName());
        object.put(DESCRIPTION,description);
        JSONArray array = new JSONArray();
        for(String f:featurePaths){
            array.put(f);
        }
        object.put(FEATURES,array);
        variant = variant.endsWith(".json")?variant:variant +".json";
        FilesUtil.writeFile(path + "/" +variant,object.toString());
    }

    public void saveTestCase(String path){
        JSONObject object = new JSONObject();
        object.put(NAME, getName());
        object.put(DESCRIPTION,description);
        JSONArray array = new JSONArray();
        for(String f:featurePaths){
            array.put(f);
        }
        object.put(FEATURES,array);

        FilesUtil.writeFile(path ,object.toString());
    }

    public void addPath(String path){
        featurePaths.add(path);
    }
    public void removePath(int index){
        featurePaths.remove(index);
    }

    public void sortUp(int index) {
        if (index > 0) {
            Collections.swap(featurePaths,index,index-1);
        }
    }

    public void sortDown(int index) {
        if (index < featurePaths.size() - 1) {
            Collections.swap(featurePaths,index,index+1);
        }
    }
}
