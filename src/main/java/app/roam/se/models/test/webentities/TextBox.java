package app.roam.se.models.test.webentities;

import app.roam.se.models.reports.Result;
import org.openqa.selenium.WebDriver;

public class TextBox extends BaseEntity {
    public TextBox(WebDriver driver, String name, String location) {
        super(driver, name, location);
    }

    public Result clearText(){
        try{
            getElement().clear();
            return new Result("cleartext",name,"");

        }catch (Exception ex){
            return new Result("cleartext",ex);
        }
    }

    public Result sendText(String text){
        try{
            clearText();
            getElement().sendKeys(text);
            return new Result("sendtext",name, text);
        }catch (Exception ex){
            return new Result("sendtext",ex);
        }
    }


    public Result appendText(String text){
        try{
            getElement().sendKeys(text);
            return new Result("appendtext",name, text);
        }catch (Exception ex){
            return new Result("appendtext",ex);
        }
    }

}
