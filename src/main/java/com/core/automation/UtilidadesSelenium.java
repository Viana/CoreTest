package com.core.automation;

import cucumber.api.Scenario;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.core.automation.BrowserDriver.getCurrentDriver;
import static org.junit.Assert.*;


public class UtilidadesSelenium {

    static boolean highLight = false;

    public static void aguardeElementoAparecerTela(By locator, int segundos) throws ConfigurationException, IOException {
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), segundos);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void aguardeElementoAparecerTela(By locator) throws ConfigurationException, IOException, InterruptedException {
        Thread.sleep(100);
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 90);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void aguardeURLDesaparecerTela(String valor) throws ConfigurationException, IOException, InterruptedException {
        Thread.sleep(100);
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 90);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains(valor)));
    }

    public static void aguardeElementoAparecerTela(WebElement locator) throws ConfigurationException, IOException {
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 90);
        wait.until(ExpectedConditions.visibilityOf(locator));
    }

    public static void aguardeElementoAparecer(By locator) throws ConfigurationException, IOException {
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 90);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void aguardeElementoDesaparecer(By locator) throws InterruptedException, ConfigurationException, IOException {
        Thread.sleep(1000);
        if (elementIsPresent(locator)) {
            WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 90);
            wait.until(ExpectedConditions.invisibilityOf(getCurrentDriver().findElement(locator)));
        }
    }

    public static WebElement elementoDisplayed(By locator) throws IOException, ConfigurationException, InterruptedException {
        WebElement obj = null;
        aguardeElementoAparecer(locator);
        scrollElement(locator);
        List<WebElement> locators = quantidadeElementos(locator);
        if (locators.size() > 0) {
            for (WebElement el : locators) {
                if (el.isDisplayed()) {
                    obj = el;
                    aguardeElementoAparecerTela(obj);
                    break;
                }
            }
        } else {
            obj = getCurrentDriver().findElement(locator);
        }
        return obj;
    }

    public static String getTexto(By locator) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        return obj.getText();
    }

    public static String getAttribute(By locator, String atributo) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        return obj.getAttribute(atributo);
    }

    public static void setAttribute(By locator, String atributo, String info) throws ConfigurationException, InterruptedException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        ((JavascriptExecutor) getCurrentDriver()).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", obj, atributo, info);
    }

    public static String getValue(By locator) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        return obj.getAttribute("value");
    }

    public static void cliqueNoElemento(By locator) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        obj.click();
    }

    public static void mouserNoElemento(By locator) throws InterruptedException, ConfigurationException, IOException {
        Actions action = new Actions(getCurrentDriver());
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        action.moveToElement(obj).build().perform();
    }

    public static void cliqueNoElementoJavaScripts(By locator) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        ((JavascriptExecutor) getCurrentDriver()).executeScript("arguments[0].click();", obj);
    }

    public static void digiteNoElementoJavaScripts(By locator, String info) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        obj.clear();
        ((JavascriptExecutor) getCurrentDriver()).executeScript("arguments[0].value='" + info + "';", obj);
    }

    public static void digiteNoElemento(By locator, String info) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        obj.clear();
        obj.sendKeys(info);
    }

    public static void digiteCaracterNoElemento(By locator, String info) throws InterruptedException, ConfigurationException, IOException {
        WebElement obj = elementoDisplayed(locator);
        highLighterMethod(getCurrentDriver(), obj);
        obj.clear();
        obj.click();
        obj.sendKeys(Keys.HOME);
        for (int i = 0; i < info.length(); i++) {
            char c = info.charAt(i);
            String letra = new StringBuilder().append(c).toString();
            Thread.sleep(200);
            obj.sendKeys(letra);
        }
    }

    public static void scrollElement(By locator) throws ConfigurationException, IOException, InterruptedException {
        WebElement obj = getCurrentDriver().findElement(locator);
        ((JavascriptExecutor) getCurrentDriver()).executeScript("arguments[0].scrollIntoView(false);", obj);
        Thread.sleep(200);
    }

    public static void acessarFrame(By locator) throws ConfigurationException, IOException {
        getCurrentDriver().switchTo().defaultContent();
        getCurrentDriver().switchTo().frame(getCurrentDriver().findElement(locator));
    }

    public static void highLighterMethod(WebDriver driver, WebElement element) throws ConfigurationException, IOException, InterruptedException {
        if (highLight) {
            JavascriptExecutor js = (JavascriptExecutor) getCurrentDriver();
            js.executeScript("arguments[0].setAttribute('style', 'background: ; border: 2px solid red;');", element);
        }
    }

    public static boolean elementIsPresent(By locator) throws ConfigurationException, IOException {
        return getCurrentDriver().findElements(locator).size() > 0;
    }

    public static void openNewTab(String url) throws ConfigurationException, IOException {
        ((JavascriptExecutor) getCurrentDriver()).executeScript("window.open()");
        selecionarUltimaJanelaAberta();
        acessarUrl(url);
    }

    public static void fecharBrowser() throws ConfigurationException, IOException, InterruptedException {
        getCurrentDriver().close();
        Thread.sleep(1000);
        selecionarUltimaJanelaAberta();
    }

    public static void selecionarUltimaJanelaAberta() throws ConfigurationException, IOException {
        List<String> abas =  new ArrayList<>(getCurrentDriver().getWindowHandles());
        getCurrentDriver().switchTo().window(abas.get(abas.size() - 1));
    }

    public static void atualizaBrowser() throws ConfigurationException, IOException {
        getCurrentDriver().navigate().refresh();
    }

    public static void acessarUrl(String url) throws ConfigurationException, IOException {
        getCurrentDriver().get(url);
    }

    public static List<WebElement> quantidadeElementos(By locator) throws ConfigurationException, IOException, InterruptedException {
        aguardeElementoAparecer(locator);
        List<WebElement> locators = getCurrentDriver().findElements(locator);
        List<WebElement> elements = new ArrayList<>();
        for (WebElement el : locators) {
            if (el.isDisplayed()) {
                elements.add(el);
            }
        }
        return elements;
    }

    public static String mensagemErroIgual(String esperado, String atual) {
        return "Divergência!!! Esperado: '" + esperado + "' a tela informou: '" + atual + "'";
    }

    public static String mensagemErroContains(String tela, String esperado) {
        return "Divergência!!! O '" + tela + "' não contains '" + esperado + "'";
    }

    public static void executarComandoServico(String acao, String servico) {
        String[] command = {"cmd.exe", "/c", "net", acao, servico};
        try {
            Process process = new ProcessBuilder(command).start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                Thread.sleep(2000);
            }
        } catch (Exception ex) {
            System.out.println("Exception : " + ex);
        }
    }

    public static void validarComparacao(String esperado, String atual, Scenario scenario) throws WebDriverException, ConfigurationException, IOException {
        assertEquals(mensagemErroIgual(esperado, atual), esperado, atual);
        takeScreenShot(scenario);
    }

    public static void validarComparacaoArray(String mensagem, String[] esperado, String[] atual, Scenario scenario) throws WebDriverException, ConfigurationException, IOException {
        assertArrayEquals(mensagem, esperado, atual);
        takeScreenShot(scenario);
    }

    public static void validarComparacaoContains(String tela, String esperado, Scenario scenario) throws WebDriverException, ConfigurationException, IOException {
        assertTrue(mensagemErroContains(tela, esperado), tela.contains(esperado));
        takeScreenShot(scenario);
    }

    public static void validarComparacaoNaoContains(String tela, String esperado, Scenario scenario) throws WebDriverException, ConfigurationException, IOException {
        assertFalse(mensagemErroContains(tela, esperado), tela.contains(esperado));
        takeScreenShot(scenario);
    }

    public static void takeScreenShot(Scenario scenario) throws WebDriverException, ConfigurationException, IOException {
        final byte[] screenshot = ((TakesScreenshot) BrowserDriver.getCurrentDriver()).getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }

    public static void takeScreenShot(String pathName) throws WebDriverException, ConfigurationException, IOException {
        File screenShot = ((TakesScreenshot) getCurrentDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenShot, new File(pathName));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage(), ioe);
        }
    }

    public static String verificarNomeAmbiente() throws IOException {
        return ConfiguracaoArquivoPropertiesUsers.getProp().getProperty("config.url").split("//")[1].split("\\.")[0];
    }

    public static void aguardeTelaCarregar() throws InterruptedException, ConfigurationException, IOException {
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 120);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[starts-with(@class,'pace-progress') and @data-progress-text='100%']")));
    }
}


