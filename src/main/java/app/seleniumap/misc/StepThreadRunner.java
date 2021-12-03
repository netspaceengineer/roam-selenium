package app.seleniumap.misc;

import app.seleniumap.models.reports.Result;
import app.seleniumap.models.test.Executor;
import app.seleniumap.models.test.TestStep;
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
