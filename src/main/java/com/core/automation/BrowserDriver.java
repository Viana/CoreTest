package com.core.automation;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserDriver {
    private static WebDriver driver;
    static String arquivoControle = System.getProperty("user.dir") + "\\target\\classes\\controleUsuario.txt";
    static String arquivoBaixado = System.getProperty("user.dir") + "\\target\\arq";

    public synchronized static WebDriver getCurrentDriver() throws ConfigurationException, IOException {
        if (null == driver) {
            BrowserName browser;
            if (System.getProperty("browser") == null) {
                browser = BrowserName.CHROME1;
            } else {
                browser = BrowserName.valueOf(System.getProperty("browser").toUpperCase());
            }
            try {
                driver = browser.getBrowser();
            } finally {
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        try {
                            fecharBrowser();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            }
        }

        return driver;
    }

    public static void fecharBrowser() throws IOException {
        // Removendo o usuário do arquivo no path "arquivoControle"
        if (driver != null)
            ConfiguracaoArquivoPropertiesUsers.liberarUser(arquivoControle, ((RemoteWebDriver) driver).getSessionId());
        try {
            if (null != driver) {
                driver.manage().deleteAllCookies();
                driver.quit();
            }
        } catch (UnreachableBrowserException ignore) {
        }
        driver = null;
    }

    public static boolean isClosed() {
        return (null == driver);
    }
}