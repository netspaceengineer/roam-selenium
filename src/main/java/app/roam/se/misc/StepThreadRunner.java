package app.roam.se.misc;

import app.roam.se.models.reports.Result;
import app.roam.se.models.test.Executor;
import app.roam.se.models.test.TestStep;
import org.openqa.selenium.WebDriver;

public class StepThreadRunner implements Runnable {
    private WebDriver driver;
    private TestStep testStep;
    private String variant;
    public  StepThreadRunner(TestStep testStep, WebDriver driver, String variant){
        this.driver = driver;
        this.testStep = testStep;
        this.variant = variant;
    }
    @Override
    public void run() {
        Result res = new Executor(driver,testStep, variant).execute();
        System.out.println(res.getResult());
    }
}
