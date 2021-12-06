package app.roam.se.models.test.webentities;

import org.openqa.selenium.WebDriver;

public class Collection implements Entity{


    protected WebDriver driver;
    protected String name;
    protected String location;
    protected boolean status=false;
    protected String message ="";
    protected String exception = "";

    public Collection(WebDriver driver,String name, String location) {
        this.driver = driver;
        this.location = location;
        this.name = name;
    }
    @Override
    public String[] getActions() {
        return new String[0];
    }
}
