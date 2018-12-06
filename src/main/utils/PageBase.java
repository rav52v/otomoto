package main.utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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

    protected boolean isElementFound(List<WebElement> element) {
        return element.size() > 0;
    }
}
