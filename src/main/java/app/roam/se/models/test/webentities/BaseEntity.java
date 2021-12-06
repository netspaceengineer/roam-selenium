package app.roam.se.models.test.webentities;

import app.roam.se.App;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BaseEntity implements Entity {

    protected WebDriver driver;
    protected String name;
    protected String location;
    protected boolean status=false;
    protected String message ="";
    protected String exception = "";

    public BaseEntity(WebDriver driver,String name, String location) {
        this.driver = driver;
        this.location = location;
        this.name = name;
    }



    public WebElement getElement() {

        By by;
        if (location.trim().toLowerCase().startsWith("css=")) {
            by = By.cssSelector(location.replace("css=", ""));
        } else if (location.trim().toLowerCase().startsWith("id=")) {
            by = By.id(location.replace("id=", ""));
        } else if (location.trim().toLowerCase().startsWith("name=")) {
            by = By.name(location.replace("name=", ""));
        } else if (location.trim().toLowerCase().startsWith("class=")) {
            by = By.className(location.replace("class=", ""));
        } else {
            by = By.xpath(location.replace("xpath=", ""));
        }
        try {
            return driver.findElement(by);
        } catch (Exception ex) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(App.testProject.wait));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        }
    }


    @Override
    public String[] getActions() {
        List<String> actions = new ArrayList<String>();
        for (Method m : this.getClass().getMethods()) {
            if (m.getName().equals(this.getClass().getName())) {
                actions.add(m.getName().toLowerCase());
            }
        }

        return (String[]) actions.toArray();
    }

}
