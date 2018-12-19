package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class PageBase {

    private Driver driver;
    private Path path;

    protected PageBase() {
        driver = new Driver();
        PageFactory.initElements(driver.getDriver(), this);
        path = Paths.get("src", "test", "outputFolder");
    }

    protected void click(WebElement element) {
        new Actions(driver.getDriver()).moveToElement(element).click().perform();
    }

    protected void waitAndClick(WebElement element) {
        new WebDriverWait(driver.getDriver(), 10, 10).until(ExpectedConditions.visibilityOf(element));
        new Actions(driver.getDriver()).moveToElement(element).click().perform();
    }

    protected void waitForElement(WebElement element) {
        new WebDriverWait(driver.getDriver(), 10, 10).until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean isElementFound(WebElement element, int maxWaitTimeSec) {
        changeImplicitlyWaitTime(0);
        try {
            new WebDriverWait(driver.getDriver(), maxWaitTimeSec, 10).until(ExpectedConditions.visibilityOf(element));
            changeBackImplicitlyWaitTime();
            return true;
        } catch (TimeoutException e) {
            changeBackImplicitlyWaitTime();
            return false;
        }
    }

    protected boolean isElementFound(List<WebElement> element, int maxWaitTimeMillis) {
        changeImplicitlyWaitTime(maxWaitTimeMillis);
        if (element.size() > 0) {
            changeBackImplicitlyWaitTime();
            return true;
        } else {
            changeBackImplicitlyWaitTime();
            return false;
        }
    }

    protected void sleeper(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void captureScreenShoot(String fileName, int zoom) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
        js.executeScript("document.body.style.zoom='" + zoom + "'");
        File scrFile = ((TakesScreenshot) driver.getDriver()).getScreenshotAs(OutputType.FILE);
        File target = new File("src\\test\\outputFolder\\" + fileName + ".png");
        try {
            if (target.exists())
                target.delete();

            Files.copy(scrFile.toPath(), target.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        js.executeScript("document.body.style.zoom='0'");
    }

    protected void saveTextToFile(String textValue, String fileName, boolean append) {
        File target = new File(path.toAbsolutePath().toString() + "/" + fileName + ".txt");

        try (FileWriter fw = new FileWriter(target, append);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.print(textValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeImplicitlyWaitTime(int milliSeconds) {
        driver.getDriver().manage().timeouts().implicitlyWait(milliSeconds, TimeUnit.MILLISECONDS);
    }

    private void changeBackImplicitlyWaitTime() {
        driver.getDriver().manage().timeouts().implicitlyWait(new ConfigurationParser().getImplicitlyWaitTime(), TimeUnit.SECONDS);
    }

    protected void changePageLoadTimeout(int seconds) {
        driver.getDriver().manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
    }

    protected void changeBackPageLoadTimeout() {
        driver.getDriver().manage().timeouts().pageLoadTimeout(60000, TimeUnit.MILLISECONDS);
    }
}
