package app.roam.se.test;


import app.roam.se.App;
import app.roam.se.models.browser.BrowserConfig;
import app.roam.se.models.reports.Result;
import app.roam.se.models.reports.TestCaseReport;
import app.roam.se.models.test.Executor;
import app.roam.se.models.test.TestCase;
import app.roam.se.models.test.TestFeature;
import app.roam.se.models.test.TestStep;
import app.roam.se.utils.FilesUtil;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;

public class TestRunner {
    private WebDriver driver;
    private TestCase testcase;
    private String variant;
    private String reportPath;
    private TestCaseReport testCaseReport;

    @BeforeTest(alwaysRun = true)
    @Parameters({"testCase", "browserConfig", "variant"})
    public void setUp(String testCase, String browserConfig, String variant) {
        driver = BrowserConfig.initializeBrowser(new File(App.testProject.getLocation() + "/Browsers/" + browserConfig + ".json"));
        this.testcase = new TestCase();
        this.variant = variant;
        this.testcase.initialize(App.testProject.getLocation() + "/Test Cases/" + testCase + "/" + variant + ".json");
        this.testCaseReport = new TestCaseReport(testCase + "_" + variant);
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

        App.report.browser = cap.getBrowserName();
        App.report.browserVersion = cap.getBrowserVersion();
        App.report.os =cap.getPlatformName().name();


    }

    @Test(alwaysRun = true)
    public void test() {
        for (String f : this.testcase.getFeaturePaths()) {
            TestFeature tf = new TestFeature();
            tf.initialize(App.testProject.getLocation() + "/" + f + "/" + this.variant + ".json");
            for (TestStep s : tf.getSteps()) {
                Executor exe = new Executor(driver, s, this.variant);
                Result result =exe.execute();
                result.screenshot = exe.getScreenshot();
                testCaseReport.steps.add(result);
                System.out.println(result.getResult());
                assert (result.isPassed);

            }
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        driver.close();
        driver.quit();
        App.report.testCases.add(testCaseReport);
    }
}
