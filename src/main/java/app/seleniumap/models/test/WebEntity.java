package app.seleniumap.models.test;


import app.seleniumap.App;
import app.seleniumap.models.test.webentities.*;
import app.seleniumap.ui.common.UIUtil;
import app.seleniumap.utils.ClassUtil;
import app.seleniumap.utils.FilesUtil;
import lombok.Data;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class WebEntity {

    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String LOCATION = "location";

    public static enum TYPES{
        DOMAIN("domain"),
        PAGE("page"),
        SECTION("section"),
        CLICKABLE("clickable"),
        TEXTBOX("textbox"),
        CHECKBOX("checkbox"),
        RADIOBOX("radiobox"),
        DROPDOWN("dropdown"),
        COLLECTION("collection");
        private final String name;

        TYPES(String s) {
            name = s;
        }

        public boolean equals(String otherName) {

            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    private String name;
    private String type;
    private String location;
    private String path;
    private String variant;
    public List<WebEntity> variants = new ArrayList<WebEntity>();
    public List<WebEntity> children = new ArrayList<WebEntity>();
    public void initialize(String path,String variant){
        path=path.startsWith(App.testProject.getLocation())?path:App.testProject.getLocation()+ "/" + path;
        String target =path+"/"+variant;
        if(!target.endsWith(".json")){
            target+=".json";
        }
        this.path = target;
        String raw = FilesUtil.readFile(target);

        JSONObject object = new JSONObject(raw);
        setName(object.getString(NAME));
        setType(object.getString(TYPE));
        setVariant(variant.replace(".json",""));
        setLocation(object.getString(LOCATION));

    }


    public void initializeAll(String path) {
        initialize(path,"default");
        List<File> files = FilesUtil.getListFiles(path);
        for(File f: files){
            if(!f.getName().contains("default")){
                WebEntity w = new WebEntity();
                w.initialize(path, f.getName());
                variants.add(w);
            }
        }
    }
    public boolean saveEntity(String parent) {
        try {
            JSONObject object = new JSONObject();
            object.put(NAME, name);
            object.put(TYPE, type);
            object.put(LOCATION, location);

            String targetFolder= parent + "/" + name;
            FilesUtil.checkFolder(targetFolder);
            FilesUtil.writeFile(targetFolder + "/default.json", object.toString());
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public boolean saveVariant(String parent, String variant) {
        try {
            JSONObject object = new JSONObject();
            object.put(NAME, name);
            object.put(TYPE, type);
            object.put(LOCATION, location);


            FilesUtil.writeFile(parent + "/" + variant + ".json", object.toString());
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    public static void deleteEntity(String path) {
        FilesUtil.cleanFolder(path);
        if(!new File(path).delete()){
            UIUtil.showErrorMessage(null,"Deletion error","Unable to delete object @" + path+ ".");
        }
    }
    public static void deleteVariant(String path,String variant) {
        String target = path+"/"+variant ;
        if(!target.endsWith(".json")){
            target+=".json";
        }
        if(!new File(target).delete()){
            UIUtil.showErrorMessage(null,"Deletion error","Unable to delete variant @" + target+ ".");

        }
    }
//
//    public Object getEntityClass(WebDriver driver, String type){
//        if(type.equals(TYPES.DOMAIN.toString())){
//            return new Domain(driver, name, location);
//        }else if(type.equals(TYPES.PAGE.toString())) {
//            return new Page(driver, name, location);
//        }else if(type.equals(TYPES.SECTION.toString())) {
//            return new Section(driver, name, location);
//        }else if(type.equals(TYPES.CLICKABLE.toString())) {
//            return new Clickable(driver, name, location);
//        }else if(type.equals(TYPES.CHECKBOX.toString())) {
//            return new CheckBox(driver, name, location);
//        }else if(type.equals(TYPES.RADIOBOX.toString())) {
//            return new RadioButton(driver, name, location);
//        }else if(type.equals(TYPES.DROPDOWN.toString())) {
//            return new Dropdown(driver, name, location);
//        }else if(type.equals(TYPES.COLLECTION.toString())) {
//            return new Collection(driver, name, location);
//        }
//        return null;
//    }
    public List<String> getActions(){
        Class cls = null;
        if (type.equals(WebEntity.TYPES.DOMAIN.toString())) {
            cls = Domain.class;
        } else if (type.equals(WebEntity.TYPES.PAGE.toString())) {
            cls = Page.class;
        } else if (type.equals(WebEntity.TYPES.SECTION.toString())) {
            cls = Section.class;
        } else if (type.equals(WebEntity.TYPES.CLICKABLE.toString())) {
            cls = Clickable.class;
        } else if (type.equals(WebEntity.TYPES.TEXTBOX.toString())) {
            cls = TextBox.class;
        } else if (type.equals(WebEntity.TYPES.RADIOBOX.toString())) {
            cls = RadioButton.class;
        } else if (type.equals(WebEntity.TYPES.CHECKBOX.toString())) {
            cls = CheckBox.class;
        } else if (type.equals(WebEntity.TYPES.COLLECTION.toString())) {

        }
        return ClassUtil.getEntityActions(cls);
    }
}
