package main.utils;

import main.tools.ConfigurationParser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class PageBase {

    private Driver driver;

    public PageBase() {
        driver = new Driver();
        PageFactory.initElements(driver.getDriver(), this);
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

    protected boolean isElementFound(List<WebElement> element, int milliSeconds) {
        changeImplicitlyWaitTime(milliSeconds);
        if (element.size() > 0) {
            changeBackImplicitlyWaitTime();
            return true;
        }
        changeBackImplicitlyWaitTime();
        return false;
    }

    private void changeImplicitlyWaitTime(int milliSeconds) {
        driver.getDriver().manage().timeouts().implicitlyWait(milliSeconds, TimeUnit.MILLISECONDS);
    }

    private void changeBackImplicitlyWaitTime() {
        driver.getDriver().manage().timeouts().implicitlyWait(new ConfigurationParser().getImplicitlyWaitTime(), TimeUnit.SECONDS);
    }
}
