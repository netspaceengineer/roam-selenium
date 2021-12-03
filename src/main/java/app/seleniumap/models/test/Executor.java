package app.seleniumap.models.test;

import app.seleniumap.App;
import app.seleniumap.models.reports.Result;
import app.seleniumap.models.test.exceptions.TestFailureException;
import app.seleniumap.models.test.webentities.*;
import org.openqa.selenium.WebDriver;

public class Executor {
    private WebDriver driver;
    private TestStep testStep;

    public Executor(WebDriver driver, TestStep testStep) {
        this.driver = driver;
        this.testStep = testStep;
    }

    public Result execute() {
        WebEntity entity = new WebEntity();
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
    }


}
