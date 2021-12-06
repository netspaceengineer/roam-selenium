package app.roam.se.models.test;

import app.roam.se.utils.DBUtil;
import app.roam.se.utils.FilesUtil;
import app.roam.se.utils.TimeUtil;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Set;

@Data
public class TestProject {
    public static final String PROJECT_NAME = "projectname";
    private static final String DRIVERS = "drivers" ;
    public static String CREATED_DATE="createddate";
    public static String AUTHOR="author";
    public static String LAST_MODIFIED="lastmodified";
    public static String LAST_RUN="lastrun";
    public static String CHROME_DRIVER="webdriver.chrome.driver";
    public static String FIREFOX_DRIVER = "webdriver.gecko.driver";
    public long wait =30;
    private String name;
    private String author;
    private String createdDate;
    private String lastRun;
    private String lastModifiedDate;
    private LinkedHashMap<String, String> drivers = new LinkedHashMap<String,String>();
    private String location="";
    private String currentVariant ="default";
    public static boolean createProject(String name, String path){
        try{
            JSONObject object = new JSONObject();
            object.put(PROJECT_NAME,name);
            object.put(AUTHOR,System.getProperty("user.name"));
            object.put(CREATED_DATE, TimeUtil.getTimeStamp());
            object.put(LAST_MODIFIED, TimeUtil.getTimeStamp());
            object.put(LAST_RUN, "");
            JSONArray drivers = new JSONArray();
            JSONObject chr = new JSONObject();
            chr.put("name",CHROME_DRIVER);
            chr.put("value","");

            JSONObject ff = new JSONObject();
            ff.put("name",FIREFOX_DRIVER);
            ff.put("value","");
            drivers.put(chr);
            drivers.put(ff);
            object.put(DRIVERS,drivers);
            FilesUtil.writeFile(path+"/project.json",object.toString());
            return true;
        }catch (Exception ex){
            return false;
        }
    }


    public boolean loadTestProject(String working_location) {
        location = working_location;
        if (new File(working_location+"/project.json").exists()){
            String raw=FilesUtil.readFile(working_location+"/project.json");
            JSONObject jsonObject = new JSONObject(raw);
            setName(jsonObject.getString(PROJECT_NAME));
            setAuthor(jsonObject.getString(AUTHOR));
            setLastRun(jsonObject.getString(LAST_RUN));
            setLastModifiedDate(jsonObject.getString(LAST_MODIFIED));
            setCreatedDate(jsonObject.getString(CREATED_DATE));
            JSONArray array = jsonObject.getJSONArray(DRIVERS);
            drivers.clear();
            for(int i=0;i<array.length();i++){
                JSONObject dObject =array.getJSONObject(i);
                drivers.put(dObject.optString("name"),dObject.optString("value") );
            }


            return true;
        }else{

            return false;
        }


    }

    public boolean saveTestProject(){
        try{
            JSONObject object = new JSONObject();
            object.put(PROJECT_NAME,name);
            object.put(AUTHOR,System.getProperty("user.name"));
            object.put(CREATED_DATE, getCreatedDate());
            object.put(LAST_MODIFIED, TimeUtil.getTimeStamp());
            object.put(LAST_RUN, getLastRun());
            JSONArray drivers = new JSONArray();

            Set<String> driverKeys = getDrivers().keySet();
            for (String key : driverKeys) {
                System.out.println(getDrivers().get(key));
                JSONObject d = new JSONObject();
                d.put("name",key);
                d.put("value",getDrivers().get(key));
                drivers.put(d);
            }
            object.put(DRIVERS,drivers);
            FilesUtil.writeFile(location+"/project.json",object.toString());
            return true;
        }catch (Exception ex){
            return false;
        }

    }


    public void initialize() {
        FilesUtil.checkFolder(location+"/Library");
        FilesUtil.checkFolder(location+"/Test Groups");
        FilesUtil.checkFolder(location+"/Test Cases");
        FilesUtil.checkFolder(location+"/Browsers");
        FilesUtil.checkFolder(location+"/Result");
        FilesUtil.checkFolder(location+"/Resources");
        DBUtil.getConnection();
    }


}
