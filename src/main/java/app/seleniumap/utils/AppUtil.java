package app.seleniumap.utils;

import app.seleniumap.App;
import app.seleniumap.models.test.TestProject;

import java.io.*;
import java.util.Properties;

public class AppUtil {

    public static String app_config= System.getProperty("user.dir") + "/config.properties";
    public static void setProject(String location) {
        try {
            OutputStream output = new FileOutputStream(app_config);

            Properties prop = new Properties();
            prop.setProperty("workinglocation", location);
            prop.store(output, null);

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static TestProject getProjectDetails(){
        try{
            InputStream input = new FileInputStream(app_config);
            Properties props = new Properties();
            props.load(input);
            TestProject testProject = new TestProject();
            testProject.loadTestProject(props.getProperty("workinglocation"));
            return testProject;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static void closeProject(){
        new File(app_config).delete();
    }
}
