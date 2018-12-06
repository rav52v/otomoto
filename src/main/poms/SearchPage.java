package main.poms;

import main.utils.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage extends PageBase {

    @FindBy (css = "a.button.fakeClick.groupCustomFilter[data-custom-filter='group11'] > span")
    private WebElement carStatusBtn;





}