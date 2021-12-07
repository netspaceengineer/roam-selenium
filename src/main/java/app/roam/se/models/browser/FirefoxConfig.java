package app.roam.se.models.browser;

import app.roam.se.utils.FilesUtil;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class FirefoxConfig extends BrowserConfig {
    private static final String FIREFOX_ARGUMENTS = "arguments";
    private static final String FIREFOX_PREFERENCE = "preferences";
    private static final String FIREFOX_HEADLES = "headless";
    private List<String> firefoxArguments = new ArrayList<String>();
    private List<String> firefoxPreferences = new ArrayList<String>();
    private boolean isHeadless;

    public FirefoxConfig() {
        this.setBrowserType("firefox");
    }

    @Override
    public void saveBrowser() {
        JSONObject object = new JSONObject();
        object.put(CONFIG_NAME, getConfigName());
        object.put(BROWSER_TYPE, getBrowserType());
        object.put(WEBDRIVER_LOCATION, getWebDriverLocation());

        JSONArray argsArray = new JSONArray();
        for (String a : firefoxArguments) {
            argsArray.put(a);
        }
        JSONArray prefArray = new JSONArray();
        for (String p : firefoxPreferences) {
                prefArray.put(p);
        }
        object.put(FIREFOX_ARGUMENTS, argsArray);
        object.put(FIREFOX_PREFERENCE, prefArray);
        object.put(FIREFOX_HEADLES, isHeadless);
        FilesUtil.writeFile(getLocation(), object.toString());
    }

    @Override
    public void loadBrowser(String path) {
        String raw = FilesUtil.readFile(path);
        JSONObject object = new JSONObject(raw);
        setConfigName(object.getString(CONFIG_NAME));
        setBrowserType(object.getString(BROWSER_TYPE));
        setWebDriverLocation(object.getString(WEBDRIVER_LOCATION));
        JSONArray jsonArgs = object.getJSONArray(FIREFOX_ARGUMENTS);
        for (int i = 0; i < jsonArgs.length(); i++) {
            firefoxArguments.add((String) jsonArgs.get(i));
        }
        JSONArray jsonPref = object.getJSONArray(FIREFOX_PREFERENCE);
        for (int i = 0; i < jsonPref.length(); i++) {
            firefoxPreferences.add((String) jsonPref.get(i));
        }
        isHeadless = object.getBoolean(FIREFOX_HEADLES);

    }

    public static WebDriver initializeBrowser(File file) {
        WebDriver driver = null;
        FirefoxConfig config = new FirefoxConfig();
        config.loadBrowser(file.getAbsolutePath());

        System.setProperty("webdriver.gecko.driver", config.getWebDriverLocation());
        FirefoxOptions ffOptions = new FirefoxOptions();
        ffOptions.addArguments(config.firefoxArguments);
        ffOptions.setHeadless(config.isHeadless());
        for (String pref : config.getFirefoxPreferences()) {
            String[] pair = pref.split(":");
            ffOptions.addPreference(pair[0],pair[1]);
        }

        driver = new FirefoxDriver(ffOptions);

        return driver;
    }

}
