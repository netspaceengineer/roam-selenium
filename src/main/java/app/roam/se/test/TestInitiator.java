package app.roam.se.test;

import app.roam.se.models.reports.Report;
import app.roam.se.ui.common.Loader;
import app.roam.se.utils.FilesUtil;
import app.roam.se.App;
import app.roam.se.utils.TimeUtil;
import org.testng.TestNG;
import org.testng.collections.Lists;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class TestInitiator{



    public void initializeTest(){


        List<String> masterSuites = Lists.newArrayList();
        for(File s: FilesUtil.getFiles(App.testProject.getLocation()+ "/Suites")){
            masterSuites.add(s.getAbsolutePath());
        }
        TestNG TNG = new TestNG();
        TNG.setTestSuites(masterSuites);
        TNG.setOutputDirectory(App.testProject.getLocation()+"/Results");
        App.report = new Report("ROAM_SUITE");
        App.report.date= TimeUtil.getTimeStamp("dd-MMM-yyyy");
        App.report.startTime = System.currentTimeMillis();
        try {

            TNG.run();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            System.out.println("Completed All the test");
            App.report.endTime = System.currentTimeMillis();
            App.report.saveReport();
        }

    }

}
