package app.roam.se.models.test;

import app.roam.se.models.reports.Result;
import app.roam.se.App;
import app.roam.se.models.test.exceptions.TestFailureException;
import app.roam.se.models.test.webentities.*;

import org.openqa.selenium.WebDriver;

public class Executor {
    private WebDriver driver;
    private TestStep testStep;
    private String variant;
    public Executor(WebDriver driver, TestStep testStep, String variant) {
        this.driver = driver;
        this.testStep = testStep;
        this.variant=variant;
    }

    public Result execute() {
        WebEntity entity = new WebEntity();
        try {

            entity.initialize(testStep.getPath(), App.testProject.getCurrentVariant());
            switch (testStep.getAction().toUpperCase()) {
                case "ACCEPT":
                    return new Domain(driver, entity.getName(), entity.getLocation()).accept();
                case "SWITCHWINDOWBYNAME":
                    return new Domain(driver, entity.getName(), entity.getLocation()).switchWindowByName(testStep.getData());
                case "SWITCHWINDOWBYINDEX":
                    return new Domain(driver, entity.getName(), entity.getLocation()).switchWindowByIndex(Integer.parseInt(testStep.getData()));
                case "FORWARD":
                    return new Domain(driver, entity.getName(), entity.getLocation()).forward();
                case "NAVIGATE":
                    return new Domain(driver, entity.getName(), entity.getLocation()).navigate(entity.getLocation());
                case "REFRESH":
                    return new Domain(driver, entity.getName(), entity.getLocation()).refresh();
                case "DISMISS":
                    return new Domain(driver, entity.getName(), entity.getLocation()).dismiss();
                case "BACK":
                    return new Domain(driver, entity.getName(), entity.getLocation()).back();
                case "VERIFYCHECKED":
                    return new CheckBox(driver, entity.getName(), entity.getLocation()).verifyChecked();
                case "VERIFYUNCHECKED":
                    return new CheckBox(driver, entity.getName(), entity.getLocation()).verifyUnchecked();
                case "DOUBLECLICK":
                    return new Clickable(driver, entity.getName(), entity.getLocation()).doubleclick();
                case "RIGHTCLICK":
                    return new Clickable(driver, entity.getName(), entity.getLocation()).rightclick();
                case "HOVER":
                    return new Clickable(driver, entity.getName(), entity.getLocation()).hover();
                case "CLICK":
                    return new Clickable(driver, entity.getName(), entity.getLocation()).click();
                case "SELECTBYINDEX":
                    return new Dropdown(driver, entity.getName(), entity.getLocation()).selectByIndex(Integer.parseInt(testStep.getData()));
                case "SELECTBYTEXT":
                    return new Dropdown(driver, entity.getName(), entity.getLocation()).selectByText(testStep.getData());
                case "SELECTBYVALUE":
                    return new Dropdown(driver, entity.getName(), entity.getLocation()).selectByValue(testStep.getData());
                case "CLEARTEXT":
                    return new TextBox(driver, entity.getName(), entity.getLocation()).clearText();
                case "SENDTEXT":
                    return new TextBox(driver, entity.getName(), entity.getLocation()).sendText(testStep.getData());
                case "APPENDTEXT":
                    return new TextBox(driver, entity.getName(), entity.getLocation()).appendText(testStep.getData());
            }
            return new Result(entity.getName(), new TestFailureException("Unknown test command!"));
        }catch (Exception ex){
            return new Result(entity.getName(), new TestFailureException(ex.getMessage()));
        }
    }


}
