package app.roam.se.misc;

import app.roam.se.models.reports.Result;
import app.roam.se.models.test.Executor;
import app.roam.se.models.test.TestStep;
import org.openqa.selenium.WebDriver;

import javax.swing.*;

public class StepThreadRunner extends SwingWorker {
    private WebDriver driver;
    private TestStep testStep;
    private String variant;
    public  StepThreadRunner(TestStep testStep, WebDriver driver, String variant){
        this.driver = driver;
        this.testStep = testStep;
        this.variant = variant;
    }

    @Override
    protected Object doInBackground() throws Exception {
        Result res = new Executor(driver,testStep, variant).execute();
        System.out.println(res.getResult());
        return null;
    }


}
