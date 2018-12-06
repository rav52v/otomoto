package main.poms;

import main.utils.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ItemPage extends PageBase {
    @FindBy(css = ".offer-content__aside .seller-box__seller-address__label")
    private WebElement location;

    @FindBy(css = ".offer-content__aside .seller-phones__button")
    private WebElement mobileBtn;

    @FindBy(css = ".offer-content__aside .phone-number ")
    private WebElement mobile;

    @FindBy(css = ".offer-content__aside .seller-box__seller-name")
    private WebElement sellerName;

    @FindBy(css = ".offer-content__metabar > div > span:first-child > span:last-child")
    private WebElement dateOfIssue;

    // OFFER PARAMETERS //

    @FindBy(css = "#parameters > ul li.offer-params__item")
    private WebElement offerParameters;



    public String getLocation(){
        return this.location.getText().trim();
    }

    public String getMobile(){
        mobileBtn.click();
        return this.mobile.getText();
    }

    public String getSellerName(){
        return sellerName.getText().trim();
    }

    public String getDateOfIssue(){
        return this.dateOfIssue.getText().trim();
    }




}
