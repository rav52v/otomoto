package main.poms;

import main.utils.Log;
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
    private Log log;
    private int allOffers;
    private int mappedOffersSize;
    private long startTime;

    @FindBy(css = "div.offers.list > article")
    private List<WebElement> offersList;

    @FindBy(css = "span.icon-arrow_right")
    private List<WebElement> nextPageBtn;

    @FindBy(css = "span.fleft.tab.selected > span.counter")
    private WebElement offersCounter;

    public SearchPage() {
        this.allOffers = getAllOffers();
        this.log = new Log();
        this.startTime = System.currentTimeMillis();
        this.mapAllOffers();
    }

    private void addOffersFromCurrentPageToMap(Map<String, String> map, List<String> list) {
        for (WebElement offer : offersList) {
            map.put(offer.getAttribute("data-ad-id"), offer.getAttribute("data-href"));
            list.add(offer.getAttribute("data-ad-id"));
            mappedOffersSize = list.size();
        }
    }

    private void mapAllOffers() {
        log.logInfo("All offers {" + String.valueOf(allOffers) + "}");
        idAndLinkHolder = new HashMap<>();
        idList = new ArrayList<>();
        do {
            addOffersFromCurrentPageToMap(idAndLinkHolder, idList);
            click(nextPageBtn.get(0));
            if ((allOffers - getMappedOffersSize()) % 5 == 0){
                double percentDone = Double.parseDouble(String.format("%.2f", (100.0 * ((double) getMappedOffersSize()
                        / (double) allOffers))).replaceAll(",", "."));
                double passedTimeInMinutes = (System.currentTimeMillis() - startTime) / 60000.0;
                double timeLeftInMinutes = (100.0 / percentDone) * (passedTimeInMinutes) - passedTimeInMinutes + 1.0;
                log.logInfo("Operation is done in {" + percentDone + "%}, mapped offers {" + getMappedOffersSize() +
                        "}, estimated time {" + String.valueOf(timeLeftInMinutes).replaceAll(".\\d+$", "") + " minutes}");
            }
        } while (isElementFound(nextPageBtn, 5000));
        addOffersFromCurrentPageToMap(idAndLinkHolder, idList);
    }

    public int getMappedOffersSize() {
        return this.mappedOffersSize;
    }

    private int getAllOffers() {
        return Integer.parseInt(offersCounter.getText().replaceAll("[)( ]", ""));
    }

}