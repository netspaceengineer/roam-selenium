package app.seleniumap.utils;

import app.seleniumap.App;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesUtil {
    public static void writeFile(String path, String body) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println("[Info] Generating/Writing to " + path);


            bw.write(body);

            bw.close();
            fw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }




    }

    public static String readFile(String path)  {

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String raw = sb.toString();
            br.close();
            return raw;

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            return "";
        }
    }
    public static void checkFolder(String folderPath) {
        File f = new File(folderPath);
        if (!f.exists()) {
            f.mkdirs();
        }
    }
    public static File[] getFiles(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }
    public static List<File> getViewableFiles(String path){
        List<File> listOfFiles = new ArrayList<File>();
        for(File f:getFiles(path)) {
            if(!f.getAbsolutePath().endsWith(".json") || f.getAbsolutePath().contains(App.testProject.getLocation() + "\\Browsers" )) {
                listOfFiles.add(f);
            }
        };
        return listOfFiles;
    }
    public static List<File> getListFiles(String path){
        List<File> listOfFiles = new ArrayList<File>();
        for(File f:getFiles(path)) {
            if(f.isFile()) {
                listOfFiles.add(f);
            }
        };
        return listOfFiles;
    }
    public static void cleanFolder(String path) {
        for(File f:getListFiles(path)) {
            f.delete();
        }
    }
    public static List<File>  getNonDefaultFiles(String path){
        List<File> listOfFiles = new ArrayList<File>();
        for(File f:getFiles(path)) {
            if(f.isFile() && !f.getAbsolutePath().endsWith("default.json")) {
                listOfFiles.add(f);
            }
        };
        return listOfFiles;
    }

    public static String getFileName(String featurePath) {
        String fileName = new File(featurePath).getName();
        return fileName.substring(0,fileName.indexOf("."));
    }
    public static String generateTestCaseSuite(String testPlanName, List<String> testCases, String browser, String variant) {
        XmlSuite suite = new XmlSuite();
        suite.setName("Suite_" + testPlanName);

        for (String t : testCases) {
            XmlTest test = new XmlTest();
            test.setName("Test_" + t);
            Map<String, String> params = new HashMap<String, String>();
            params.put("testCase", t);
            params.put("browserConfig", browser);
            params.put("variant", variant);
            test.setParameters(params);
            XmlClass cls = new XmlClass();
            cls.setName("app.seleniumap.test.TestRunner");
            test.getClasses().add(cls);
            suite.getTests().add(test);
        }
        checkFolder(App.testProject.getLocation()+ "/Suites");
        String suiteName =App.testProject.getLocation() + "/Suites/" +  testPlanName+"_"+TimeUtil.getTimeStamp()+ ".xml";
        FilesUtil.writeFile(suiteName, suite.toXml());
        return suiteName;
    }
}
