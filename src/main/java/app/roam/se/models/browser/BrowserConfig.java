package app.roam.se.models.browser;

import app.roam.se.utils.FilesUtil;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class BrowserConfig {
    public static final String CONFIG_NAME = "browserName";
    public static final String BROWSER_TYPE = "browserType";
    public static final String CHROME_OPTIONS ="chromeOptions";

    public static final String WEBDRIVER_LOCATION = "webDriverLocation";
    private String configName;
    private String browserType;
    private String webDriverLocation;
    //TODO: add more browser options
    private List<String> chromeOptions= new ArrayList<String>();

    private String location="";

    public static WebDriver initializeBrowser(File file) {
        WebDriver driver = null;
        BrowserConfig config = new BrowserConfig();
        config.loadBrowser(file.getAbsolutePath());
        if(config.getBrowserType().equals("chrome")){
            System.setProperty("webdriver.chrome.driver",config.getWebDriverLocation());
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(config.chromeOptions);
            driver = new ChromeDriver(chromeOptions);
        }else if(config.getBrowserType().equals("firefox")){
            driver =  FirefoxConfig.initializeBrowser(file);
        }
        return  driver;
    }

    public void saveBrowser(){
        JSONObject object = new JSONObject();
        object.put(CONFIG_NAME,configName);
        object.put(BROWSER_TYPE,browserType);
        object.put(WEBDRIVER_LOCATION,webDriverLocation);
        if(browserType.contains("chrome")){
            JSONArray coArray = new JSONArray();
            for(String c: chromeOptions){
                coArray.put(c);
            }
            JSONArray ceArray = new JSONArray();
            object.put(CHROME_OPTIONS,coArray);


        }
        FilesUtil.writeFile(getLocation(),object.toString());
    }

    public void loadBrowser(String path){
        String raw = FilesUtil.readFile(path);
        JSONObject object = new JSONObject(raw);
        configName = object.getString(CONFIG_NAME);
        browserType = object.getString(BROWSER_TYPE);
        webDriverLocation = object.getString(WEBDRIVER_LOCATION);
        if(browserType.contains("chrome")){
            JSONArray options = object.getJSONArray(CHROME_OPTIONS);
            for (int i=0;i<options.length();i++){
                chromeOptions.add((String) options.get(i));
            }

        }


    }
}
