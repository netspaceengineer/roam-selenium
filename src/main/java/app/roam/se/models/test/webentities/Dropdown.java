package app.roam.se.models.test.webentities;

import app.roam.se.models.reports.Result;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Dropdown extends BaseEntity{
    public Dropdown(WebDriver driver, String name, String location) {
        super(driver, name, location);
    }

    public Result selectByIndex(int index){
        try{
            Select select = new Select(getElement());
            select.selectByIndex(index);
            return new Result("selectbyindex",name,String.valueOf(index));
        }catch (Exception ex){
            return new Result("selectbyindex",ex);
        }
    }

    public Result selectByValue(String value){
        try{
            Select select = new Select(getElement());
            select.selectByValue(value);
            return new Result("selectbyvalue",name,value);
        }catch (Exception ex){
            return new Result("selectbyvalue",ex);
        }
    }
    public Result selectByText(String text){
        try{
            Select select = new Select(getElement());
            select.selectByVisibleText(text);
            return new Result("selectbytext",name,String.valueOf(text));
        }catch (Exception ex){
            return new Result("selectbytext",ex);
        }
    }
}
