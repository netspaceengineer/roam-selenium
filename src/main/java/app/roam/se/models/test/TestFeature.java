package app.roam.se.models.test;

import app.roam.se.utils.FilesUtil;
import app.roam.se.App;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class TestFeature {
    private static final String NAME = "name";
    private static final String STEPS = "steps";
    private static final String VARIANT = "variant";
    private static final String DESCRIPTION = "description";
    private String name;
    private String variant;
    private String description;
    private String location;
    private List<TestStep> steps = new ArrayList<TestStep>();

    public void initialize(String path){
        path = path.endsWith(".json")?path:path+ ".json";
        this.location =path;
        System.out.println(path);
        String raw = FilesUtil.readFile(path);
        JSONObject object = new JSONObject(raw);
        setName(object.getString(TestFeature.NAME));
        setVariant(new File(raw).getName());
        setDescription(object.getString(TestFeature.DESCRIPTION));
        JSONArray arraySteps = object.getJSONArray(STEPS);
        for(int i =0 ; i< arraySteps.length();i++){
           JSONObject stepOnbject = arraySteps.getJSONObject(i);
            steps.add(new TestStep(stepOnbject.getString(TestStep.PATH),stepOnbject.getString(TestStep.ACTION),stepOnbject.getString(TestStep.DATA)));
        }
    }
    public void saveTestFeature(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TestFeature.NAME,name);
        jsonObject.put(TestFeature.DESCRIPTION,description);
        JSONArray array = new JSONArray();
        for(TestStep p:steps){
            JSONObject stepObject = new JSONObject();
            stepObject.put(TestStep.PATH,p.getPath());
            stepObject.put(TestStep.ACTION,p.getAction());
            stepObject.put(TestStep.DATA,p.getData());
            array.put(stepObject);
        }
        jsonObject.put(TestFeature.STEPS,array);
        if(!variant.endsWith(".json")){
            variant+=".json";
        }
        FilesUtil.writeFile(getLocation(), jsonObject.toString());
    }
    public void saveTestFeature(String path,String variant){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TestFeature.NAME,name);
        jsonObject.put(TestFeature.DESCRIPTION,description);
        JSONArray array = new JSONArray();
        for(TestStep p:steps){
            JSONObject stepObject = new JSONObject();
            stepObject.put(TestStep.PATH,p.getPath());
            stepObject.put(TestStep.ACTION,p.getAction());
            stepObject.put(TestStep.DATA,p.getData());
            array.put(stepObject);
        }
        jsonObject.put(TestFeature.STEPS,array);
        if(!variant.endsWith(".json")){
            variant+=".json";
        }
        FilesUtil.writeFile(path+"/" + variant, jsonObject.toString());
    }
    public void removeStep(int index) {
        steps.remove(index);
    }

    public void addStep(TestStep step) {
        steps.add(step);
    }

    public void sortUp(int index) {
        if (index > 0) {
            Collections.swap(steps,index,index-1);
        }
    }

    public void sortDown(int index) {
        if (index < steps.size() - 1) {
            Collections.swap(steps,index,index+1);
        }
    }

    public WebEntity get(int index, String variant) {
        WebEntity webEntity = new WebEntity();
        webEntity.initialize(App.testProject.getLocation() + "/" + steps.get(index).getPath(), variant);
        return webEntity;
    }


}
