package app.seleniumap.models.test.webentities;

import app.seleniumap.models.reports.Result;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class Clickable extends BaseEntity{
    public Clickable(WebDriver driver, String name, String location) {
        super(driver, name, location);
    }

    public Result hover(){
        try {
            Actions action = new Actions(driver);
            action.moveToElement(getElement()).build().perform();

            return new Result( "hover", name, "");
        } catch (Exception ex) {
            try{
                String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
                ((JavascriptExecutor) driver).executeScript(mouseOverScript,
                        getElement());
                return new Result( "hover", name, "");
            }catch (Exception ex2){
                return new Result("hover",ex,ex2);
            }

        }
    }

    public Result click(){
        try {
            hover();
            getElement().click();

            return new Result( "click", name, "");
        } catch (Exception ex) {
            return new Result( "click", ex);
        }
    }

    public Result doubleclick(){
        try {
            hover();
            Actions action = new Actions(driver);
            action.doubleClick(getElement()).build().perform();

            return new Result( "doubleclick", name, "");
        } catch (Exception ex) {
            return new Result( "doubleclick" ,ex);
        }
    }

    public Result rightclick(){
        try {
            hover();
            Actions action = new Actions(driver);
            action.contextClick(getElement()).build().perform();

            return new Result( "rightclick", name, "");
        } catch (Exception ex) {
            return new Result( "rightclick", ex);
        }
    }
}
