package main.poms;

import main.tools.ConfigurationParser;
import main.tools.DataBaseReader;
import main.utils.Driver;
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
    private int existingRecords;
    private String offerId;
    private String link;
    private DataBaseReader dataBase;
    private ConfigurationParser config;
    private String actuallyPage;
    private Driver driver;

    @FindBy(css = "div.offers.list > article")
    private List<WebElement> offersList;

    @FindBy(css = "span.icon-arrow_right")
    private List<WebElement> nextPageBtn;

    @FindBy(css = "span.fleft.tab.selected > span.counter")
    private WebElement offersCounter;

    public SearchPage() {
        this.dataBase = new DataBaseReader();
        this.config = new ConfigurationParser();
        this.allOffers = getAllOffers();
        this.log = new Log();
        this.driver = new Driver();
        if (idAndLinkHolder == null)
            idAndLinkHolder = new HashMap<>();
        this.startTime = System.currentTimeMillis();
    }

    private void addOffersFromCurrentPageToMap(Map<String, String> map) {
        for (WebElement offer : offersList) {
            offerId = offer.getAttribute("data-ad-id");
            link = offer.getAttribute("data-href");

            if (!dataBase.checkIfOfferIdExist(offerId)) {
                map.put(offerId, link);
                mappedOffersSize = map.size();
            } else
                this.existingRecords++;
        }
    }

    public void mapAllOffers() {
        log.logInfo("All offers {" + (allOffers) + "}");
        int allAvailablePages = allOffers / 32;
        int maxExistingRecords = config.getMaxExistingOffers();

        do {
            addOffersFromCurrentPageToMap(idAndLinkHolder);

            if (existingRecords > maxExistingRecords) {
                log.logInfo("Mapping over, is interrupted due to {" + existingRecords + "} repeated records in database.");
                break;
            }

            if (isElementFound(nextPageBtn, 3000)){
                click(nextPageBtn.get(0));
                actuallyPage = driver.getDriver().getCurrentUrl().replaceAll("^.+page=", "");
            }

            if ((allOffers - getMappedOffersSize()) % 5 == 0)
                log.logInfo("Adding offers to map, already mapped {" + getMappedOffersSize() + "}, actually page {" + actuallyPage + "/" + allAvailablePages + "}...");

        } while (isElementFound(nextPageBtn, 3000));

        addOffersFromCurrentPageToMap(idAndLinkHolder);
    }

    public int getMappedOffersSize() {
        return this.mappedOffersSize;
    }

    private int getAllOffers() {
        return Integer.parseInt(offersCounter.getText().replaceAll("[)( ]", ""));
    }

    public Map<String, String> getIdAndLinkMap() {
        return idAndLinkHolder;
    }
}