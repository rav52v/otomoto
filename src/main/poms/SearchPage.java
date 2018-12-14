package main.poms;

import main.utils.Log;
import main.utils.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPage extends PageBase {

    /**
     * note that:
     * idAndLinkHolder.size() == idList.size()
     */
    // <offerId, link>
    private static Map<String, String> idAndLinkHolder;
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
        if (idAndLinkHolder == null){
            idAndLinkHolder = new HashMap<>();
        }
        this.startTime = System.currentTimeMillis();
    }

    private void addOffersFromCurrentPageToMap(Map<String, String> map) {
        for (WebElement offer : offersList) {
            map.put(offer.getAttribute("data-ad-id"), offer.getAttribute("data-href"));
            mappedOffersSize = map.size();
        }
    }

    public void mapAllOffers() {
        log.logInfo("All offers {" + (allOffers) + "}");
        do {
            addOffersFromCurrentPageToMap(idAndLinkHolder);
            if (isElementFound(nextPageBtn, 3000)){
                click(nextPageBtn.get(0));
            }
            //dodawanie logów
            if ((allOffers - getMappedOffersSize()) % 5 == 0){
                double percentDone = Double.parseDouble(String.format("%.2f", (100.0 * ((double) getMappedOffersSize()
                        / (double) allOffers))).replaceAll(",", "."));
                double passedTimeInMinutes = (System.currentTimeMillis() - startTime) / 60000.0;
                double timeLeftInMinutes = (100.0 / percentDone) * (passedTimeInMinutes) - passedTimeInMinutes + 1.0;
                log.logInfo("Operation is done in {" + percentDone + "%}, mapped offers {" + getMappedOffersSize() +
                        "}, estimated time {" + String.valueOf(timeLeftInMinutes).replaceAll(".\\d+$", "") + " minutes}");
            }
        } while (isElementFound(nextPageBtn, 3000));
        addOffersFromCurrentPageToMap(idAndLinkHolder);
    }

    public int getMappedOffersSize() {
        return this.mappedOffersSize;
    }

    private int getAllOffers() {
        return Integer.parseInt(offersCounter.getText().replaceAll("[)( ]", ""));
    }

    public static Map<String, String> getIdAndLinkMap(){
        return idAndLinkHolder;
    }

}