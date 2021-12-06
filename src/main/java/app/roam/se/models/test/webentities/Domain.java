package app.roam.se.models.test.webentities;

import app.roam.se.models.reports.Result;
import org.openqa.selenium.WebDriver;

public class Domain extends BaseEntity {


    public Domain(WebDriver driver, String name, String location) {
        super(driver, name, location);
    }


    public Result navigate(String url) {
        try {
            driver.get(url);
            return new Result("navigate", name, url);
        } catch (Exception ex) {
            return new Result( "navigate",ex);
        }

    }

    public Result refresh() {
        try {
            driver.navigate().refresh();
            return new Result( "refresh", "browser", "");
        } catch (Exception ex) {
            return new Result( "refresh",ex );
        }
    }

    public Result back() {
        try {
            driver.navigate().back();
            return new Result( "back", "browser history", "");
        } catch (Exception ex) {
            return new Result( "back",ex);
        }
    }

    public Result forward() {
        try {
            driver.navigate().forward();
            return new Result( "forward", "browser history", "");
        } catch (Exception ex) {
            return new Result( "forward",ex);
        }
    }

    public Result accept() {
        try {
            driver.switchTo().alert().accept();
            return new Result( "accept", "alert box", "");
        } catch (Exception ex) {
            return new Result("accept" ,ex);
        }
    }

    public Result dismiss() {
        try {
            driver.switchTo().alert().dismiss();
            return new Result("dismiss", "alert box", "");
        } catch (Exception ex) {
            return new Result( "dismiss",ex);
        }
    }

    public Result switchWindowByIndex(int index){
        try{
            driver.switchTo().frame(name);
            return new Result("switchwindowbyname" , name,String.valueOf(index));
        }catch(Exception ex){
            return new Result("switchwindowbyname",ex);
        }
    }

    public Result switchWindowByName(String windowName){
        try{
            driver.switchTo().frame(name);
            return new Result("switchwindowbyname" , windowName,windowName);
        }catch(Exception ex){
            return new Result( "switchwindowbyname",ex);
        }
    }
}
