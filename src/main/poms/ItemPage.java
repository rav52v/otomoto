package main.poms;

import main.tools.DataBaseReader;
import main.utils.Driver;
import main.utils.Log;
import main.utils.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPage extends PageBase {
    private enum ParamNames {
        AVAILABLE_PARAMETERS("Oferta od", "Kategoria", "Marka pojazdu", "Model pojazdu", "Wersja", "Rok produkcji", "Przebieg",
                "Pojemność skokowa", "VIN", "Rodzaj paliwa", "Moc", "Skrzynia biegów", "Uszkodzony", "Napęd", "Typ",
                "Liczba drzwi", "Liczba miejsc", "Kierownica po prawej (Anglik)", "Kolor", "Pierwsza rejestracja",
                "Kraj pochodzenia", "Numer rejestracyjny pojazdu", "Zarejestrowany w Polsce", "Tuning",
                "Serwisowany w ASO", "Zarejestrowany jako zabytek", "Bezwypadkowy", "Stan"),
        TRANSFORM_TO_PARAMETERS("ofertaOd", "kategoria", "marka", "model", "wersja", "rok", "przebieg",
                "pojemnosc", "vin", "paliwo", "moc", "skrzynia", "uszkodzony", "naped", "typ",
                "drzwi", "miejsca", "anglik", "kolor", "pierwszaRejestracja",
                "krajPochodzenia", "numerRej", "pl", "tuning",
                "aso", "zabytek", "bezwypadkowy", "stan");

        private String[] values;

        ParamNames(String... strings) {
            values = strings;
        }

        private boolean ifContains(String value) {
            for (String param : values) {
                if (param.equals(value))
                    return true;
            }
            return false;
        }

        private String transformParamName(String value) {
            for (int i = 0; i < values.length; i++) {
                if (values[i].equals(value)) {
                    return TRANSFORM_TO_PARAMETERS.values[i];
                }
            }
            //nigdy nie zwraca null
            return null;
        }
    }

    private Map<String, String> parametersMap;
    private Map<String, String> parametersStringMap;
    private Map<String, Integer> parametersIntMap;
    private Map<String, Long> parametersLongMap;
    private Driver driver;
    private DataBaseReader dataBase;
    private Log log;

    private String location;
    private String dateOfIssue;
    private String title;
    private int price;
    private long offerId;
    private long mobile;
    private String sellerName;
    private String equipment;
    private String description;

    @FindBy(css = ".offer-content__aside .seller-box__seller-address__label")
    private WebElement locationField;

    @FindBy(css = ".offer-header__actions span[data-path='multi_phone']")
    private List<WebElement> mobileBtnField;

    @FindBy(css = ".offer-header__actions span.phone-number")
    private WebElement mobileField;

    @FindBy(css = ".offer-content__aside .seller-box__seller-name")
    private WebElement sellerNameField;

    @FindBy(css = ".offer-content__metabar > div > span:first-child > span:last-child")
    private List<WebElement> dateOfIssueField;

    @FindBy(css = "div.offer-price")
    private WebElement priceField;

    @FindBy(css = "h1.offer-title.big-text")
    private List<WebElement> titleField;

    @FindBy(css = ".offer-content__metabar > div > span:last-child > span:last-child")
    private WebElement offerIdField;

    @FindBy(css = "a.read-more")
    private List<WebElement> readMoreBtnField;

    @FindBy(css = "#description > div")
    private WebElement descriptionField;

    @FindBy(css = "span.showPhoneButton")
    private List<WebElement> dislayNumberListField;

    @FindBy(css = "h4.offer-features__title")
    private List<WebElement> equipmentField;

    @FindBy(css = "div.offer-features__row li.offer-features__item")
    private List<WebElement> equipmentListField;

    @FindBy(css = "#parameters > ul li.offer-params__item")
    private List<WebElement> offerParameters;


    public ItemPage() {
        parametersMap = new HashMap<>();
        parametersStringMap = new HashMap<>();
        parametersIntMap = new HashMap<>();
        parametersLongMap = new HashMap<>();
        driver = new Driver();
        dataBase = new DataBaseReader();
        log = new Log();
    }

    private void setFeatures() {
        if (isElementFound(dateOfIssueField, 500))
            this.dateOfIssue = dateOfIssueField.get(0).getText().trim();
        else
            this.dateOfIssue = null;

        if (isElementFound(titleField, 500))
            this.title = titleField.get(0).getText().trim();
        else
            this.title = null;

        this.price = Integer.parseInt(priceField.getAttribute("data-price").replaceAll("[ ,.-]", ""));

        this.offerId = Long.parseLong(offerIdField.getText());

        if (isElementFound(mobileBtnField, 500)) {
            mobileBtnField.get(0).click();
            sleeper(50);
            this.mobile = Long.parseLong(mobileField.getText().replaceAll("[ +\\D]", ""));
        } else
            this.mobile = 0;

        if (isElementFound(equipmentField, 300)) {
            StringBuilder sb = new StringBuilder();
            for (WebElement feature : equipmentListField)
                sb.append(feature.getText().trim()).append(", ");
            this.equipment = sb.toString().replaceAll(", $", "");
        } else
            this.equipment = null;

        if (isElementFound(readMoreBtnField, 100)) {
            click(readMoreBtnField.get(0));
            if (isElementFound(dislayNumberListField, 0)) {
                for (WebElement displayNumber : dislayNumberListField)
                    click(displayNumber);
            }
            this.description = descriptionField.getText().replaceAll("['\"]", "-");
        } else
            this.description = null;

        this.location = locationField.getText().trim();

        this.sellerName = sellerNameField.getText().trim();
    }

    //wypełnienie map parametrów oraz dostosowanie danych typu int pod bazę danych
    private void fillParametersMap() {
        for (int i = 0; i < offerParameters.size(); i++) {
            String paramName = offerParameters.get(i).findElement(By.tagName("span")).getText().trim();
            String paramValue = offerParameters.get(i).findElement(By.tagName("div")).getText().trim();
            if (ParamNames.AVAILABLE_PARAMETERS.ifContains(paramName)) {
                String key = ParamNames.AVAILABLE_PARAMETERS.transformParamName(paramName);
                parametersMap.put(key, paramValue);
                if (key.equals("przebieg")) {
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" km| ", ""));
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("uszkodzony") || key.equals("anglik") || key.equals("pl") || key.equals("tuning")
                        || key.equals("aso") || key.equals("zabytek") || key.equals("bezwypadkowy")) {
                    parametersMap.replace(key, "1");
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("moc")) {
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" KM| ", ""));
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("pojemnosc")) {
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" cm3| ", ""));
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("miejsca") || key.equals("drzwi") || key.equals("rok")) {
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
            }
        }
        parametersStringMap = parametersMap;
    }

    public void openMultipleOffersAndSendDataToDataBase() {
        Map<String, String> offersMap = dataBase.cleanMapFromExistingRecords(SearchPage.getIdAndLinkMap());
        DataBaseReader dataBase = new DataBaseReader();
        int i = 1;
        long startTime = System.currentTimeMillis();

        for (String x : offersMap.keySet()) {
            if (i % 10 == 0){
                //dodawanie logów
                double percentDone = Double.parseDouble(String.format("%.2f", (100.0 * ((double) i
                        / (double) offersMap.size()))).replaceAll(",", "."));
                double passedTimeInMinutes = (System.currentTimeMillis() - startTime) / 60000.0;
                double timeLeftInMinutes = (100.0 / percentDone) * (passedTimeInMinutes) - passedTimeInMinutes + 1.0;

                log.logInfo("Operation is done in {" + percentDone +"%}, processing offer {" + i + "/" + offersMap.size() +
                        "}, estimated time {" + String.valueOf(timeLeftInMinutes).replaceAll(".\\d+$", "") + " minutes}");
            }

            driver.getDriver().get(offersMap.get(x));
            //sprawdzenie, czy wyszukiwarka przeniosła nas na właściwą stronę
            if (!driver.getDriver().getCurrentUrl().equals(offersMap.get(x)))
                continue;
            fillParametersMap();
            dataBase.executeQuery(generateSQLQuery());
            i++;
        }
        log.logInfo("Operation is done in {100.00%}, proceeded offers {" + i + "}, operation took {" +
                (System.currentTimeMillis() - startTime)/60000 + " minutes}, which is {" + i/((System.currentTimeMillis() - startTime)/60000) + "} offers per minute");
    }

    private String generateSQLQuery() {
        setFeatures();

        if (this.dateOfIssue != null) {
            parametersStringMap.put("dateOfIssue", this.dateOfIssue);
        }
        if (this.title != null) {
            parametersStringMap.put("title", this.title);
        }
        parametersIntMap.put("price", this.price);
        parametersLongMap.put("offerId", this.offerId);
        if (this.mobile != 0) {
            parametersLongMap.put("mobile", this.mobile);
        }
        parametersStringMap.put("location", this.location);
        parametersStringMap.put("sellerName", this.sellerName);
        if (this.equipment != null) {
            parametersStringMap.put("equipment", this.equipment);
        }
        if (this.description != null) {
            parametersStringMap.put("description", this.description);
        }

        String query = "INSERT INTO otomoto (";
        String queryFirstPart = "";

        for (String key : parametersStringMap.keySet()) {
            queryFirstPart += key.concat(", ");
        }
        for (String key : parametersIntMap.keySet()) {
            queryFirstPart += key.concat(", ");
        }
        for (String key : parametersLongMap.keySet()) {
            queryFirstPart += key.concat(", ");
        }

        query += queryFirstPart.replaceAll("(, )$", "") + ") values (";
        String querySecondPart = "";

        for (String key : parametersStringMap.keySet()) {
            querySecondPart += "'".concat(parametersStringMap.get(key)).concat("', ");
        }
        for (String key : parametersIntMap.keySet()) {
            querySecondPart += parametersIntMap.get(key) + ", ";
        }
        for (String key : parametersLongMap.keySet()) {
            querySecondPart += parametersLongMap.get(key) + ", ";
        }

        query += querySecondPart.replaceAll("(, )$", "") + ")";

        return query;
    }
}