package main.poms;

import main.utils.Driver;
import main.utils.Log;
import main.utils.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SearchPage extends PageBase {

    /**
     * note that:
     * idAndLinkHolder.size() == idList.size()
     */

    private Map<String, String> idAndLinkHolder;
    private List<String> idList;
    private Log log;
    private int allOffers;

    @FindBy(css = "div.offers.list > article")
    private List<WebElement> offersList;

    @FindBy(css = "span.icon-arrow_right")
    private List<WebElement> nextPageBtn;

    @FindBy(css = "span.fleft.tab.selected > span.counter")
    private WebElement offersCounter;

    public SearchPage() {
        allOffers = getAllOffers();
        log = new Log();
        mapAllOffers();
    }

    private void addOffersFromCurrentPageToMap(Map<String, String> map) {
        for (WebElement offer : offersList) {
            map.put(offer.getAttribute("data-ad-id"), offer.getAttribute("data-href"));
            idList.add(offer.getAttribute("data-ad-id"));
        }
    }

    private void mapAllOffers() {
        log.logInfo("All offers {" + String.valueOf(allOffers) + "}");
        idAndLinkHolder = new HashMap<>();
        idList = new ArrayList<>();
        do {
            addOffersFromCurrentPageToMap(idAndLinkHolder);
            click(nextPageBtn.get(0));
            if ((allOffers - getMappedLinksSize()) % 25 == 0) {
                log.logInfo("Operation is done in {" + String.format("%.2f", ((Double) (100.0 / (allOffers / getMappedLinksSize())))) + "%} ...");
            }
        } while (isElementFound(nextPageBtn, 500));
        addOffersFromCurrentPageToMap(idAndLinkHolder);
    }

    public int getMappedLinksSize() {
        return idAndLinkHolder.size();
    }

    private int getAllOffers() {
        return Integer.parseInt(offersCounter.getText().replaceAll("[)( ]", ""));
    }
}