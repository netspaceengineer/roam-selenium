package app.seleniumap.test;

import app.seleniumap.App;
import app.seleniumap.utils.FilesUtil;
import org.testng.TestNG;
import org.testng.collections.Lists;

import java.io.File;
import java.util.List;

public class TestInitiator {

    public void initializeTest(){
        List<String> masterSuites = Lists.newArrayList();
        for(File s:FilesUtil.getFiles(App.testProject.getLocation()+ "/Suites")){
            masterSuites.add(s.getAbsolutePath());
        }
        TestNG TNG = new TestNG();
        TNG.setTestSuites(masterSuites);

        try {
            TNG.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
           System.out.println("Completed All the test");
        }
    }
}
