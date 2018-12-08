package main.poms;

import main.utils.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends PageBase {

    @FindBy (css = "a.agree-button.cookiesBarClose")
    private WebElement cookiesBtn;

    public MainPage() {
        acceptCookiesClick();
    }

    private void acceptCookiesClick(){
        waitAndClick(cookiesBtn);
    }

}