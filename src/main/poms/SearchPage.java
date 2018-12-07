package main.poms;

import main.utils.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPage extends PageBase {

    /**
     * note that:
     * idAndLinkHolder.size() == idList.size()
     */

    private Map<String, String> idAndLinkHolder;
    private List<String> idList;

    @FindBy(css = "div.offers.list > article")
    private List<WebElement> offersList;

    @FindBy(css = "span.icon-arrow_right")
    private List<WebElement> nextPageBtn;

    private void addOffersFromCurrentPageToMap(Map<String, String> map) {
        for (WebElement offer : offersList) {
            map.put(offer.getAttribute("data-ad-id"), offer.getAttribute("data-href"));
            idList.add(offer.getAttribute("data-ad-id"));
        }
    }

    public void mapAllOffers() {
        idAndLinkHolder = new HashMap<>();
        idList = new ArrayList<>();
        do {
            addOffersFromCurrentPageToMap(idAndLinkHolder);
            click(nextPageBtn.get(0));
        } while (isElementFound(nextPageBtn, 1000));
        addOffersFromCurrentPageToMap(idAndLinkHolder);
    }
}