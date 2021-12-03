package app.seleniumap.misc;

import app.seleniumap.models.reports.Result;
import app.seleniumap.models.test.Executor;
import app.seleniumap.models.test.TestStep;
import org.openqa.selenium.WebDriver;

public class StepThreadRunner implements Runnable {
    private WebDriver driver;
    private TestStep testStep;
    public  StepThreadRunner(TestStep testStep, WebDriver driver){
        this.driver = driver;
        this.testStep = testStep;
    }
    @Override
    public void run() {
        Result res = new Executor(driver,testStep).execute();
        System.out.println(res.getResult());
    }
}
