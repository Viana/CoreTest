package com.core.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

import static com.core.automation.BrowserDriver.arquivoBaixado;

public enum BrowserName {

    CHROME1 {
        @Override
        public WebDriver getBrowser() {
            WebDriverManager.chromedriver().setup();
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.prompt_for_download", "false");
            chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
            chromePrefs.put("download.default_directory", arquivoBaixado);
            ChromeOptions option = new ChromeOptions();
            option.setExperimentalOption("prefs", chromePrefs);
            option.addArguments("-–incognito");
            option.addArguments("--start-maximized");
            System.setProperty("webdriver.chrome.silentOutput", "true");
            return new ChromeDriver(option);
        }
    }, FIREFOX1 {
        @Override
        public WebDriver getBrowser() {
            WebDriverManager.firefoxdriver().setup();
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            FirefoxProfile profile = new FirefoxProfile();
            FirefoxOptions options = new FirefoxOptions();
            profile.setPreference("browser.download.dir", arquivoBaixado);  // folder
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");  // MIME type
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download.panel.shown", false);
            options.setProfile(profile);
            return new FirefoxDriver(options);
        }
    }, FIREFOX2 {
        @Override
        public WebDriver getBrowser() {
            WebDriverManager.firefoxdriver().setup();
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            FirefoxProfile profile = new FirefoxProfile();
            FirefoxOptions options = new FirefoxOptions();
            profile.setPreference("browser.download.dir", arquivoBaixado);  // folder
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");  // MIME type
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download.panel.shown", false);
            options.addArguments("--headless");
            options.addArguments("window-size=1980,1080");
            options.setProfile(profile);
            return new FirefoxDriver(options);
        }
    }, CHROME2 {
        @Override
        public WebDriver getBrowser() {
            WebDriverManager.chromedriver().setup();
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.prompt_for_download", "false");
            chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
            chromePrefs.put("download.default_directory", arquivoBaixado);
            ChromeOptions option = new ChromeOptions();
            option = new ChromeOptions();
            option.addArguments("--headless");
            option.addArguments("-–incognito");
            option.addArguments("disable-infobars");
            option.addArguments("--disable-extensions");
            option.addArguments("--disable-gpu");
            option.addArguments("--no-sandbox");
            option.addArguments("--disable-dev-shm-usage");
            option.addArguments("--log-level=3");
            option.addArguments("--silent");
            option.addArguments("enable-features=NetworkServiceInProcess");
            option.addArguments("disable-features=NetworkService");
            option.setExperimentalOption("prefs", chromePrefs);
            option.addArguments("window-size=1980,1080");
            option.addArguments("--force-device-scale-factor=1");
            System.setProperty("webdriver.chrome.silentOutput", "true");
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            return new ChromeDriver(option);
        }
    }, EDGE1 {
        @Override
        public WebDriver getBrowser() {
            WebDriverManager.edgedriver().setup();
            EdgeOptions option = new EdgeOptions();
            return new EdgeDriver(option);
        }
    };

    public abstract WebDriver getBrowser();
}
