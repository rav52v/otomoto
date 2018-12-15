package main.poms;

import main.tools.Converters;
import main.tools.DataBaseReader;
import main.utils.Driver;
import main.utils.Log;
import main.utils.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPage extends PageBase {
    private enum ParamNames {
        AVAILABLE_PARAMETERS("Oferta od", "Marka pojazdu", "Model pojazdu", "Wersja", "Rok produkcji", "Przebieg",
                "Pojemność skokowa", "VIN", "Rodzaj paliwa", "Moc", "Skrzynia biegów", "Uszkodzony", "Napęd", "Typ",
                "Liczba drzwi", "Liczba miejsc", "Kierownica po prawej (Anglik)", "Kolor", "Pierwsza rejestracja",
                "Kraj pochodzenia", "Numer rejestracyjny pojazdu", "Zarejestrowany w Polsce", "Tuning",
                "Serwisowany w ASO", "Zarejestrowany jako zabytek", "Bezwypadkowy", "Stan"),
        TRANSFORM_TO_PARAMETERS("ofertaOd", "marka", "model", "wersja", "rok", "przebieg",
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
    private Converters converter;
    private Map<String, String> offersMap;
    private Calendar calendar;
    private SimpleDateFormat time;

    private String location;
    private String dateOfIssue;
    private String title;
    private int price;
    private long offerId;
    private long mobile;
    private String sellerName;
    private String equipment;
    private String description;

    @FindBy(css = ".seller-box__seller-address > span:last-child")
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
        converter = new Converters();
        parametersMap = new HashMap<>();
        parametersStringMap = new HashMap<>();
        parametersIntMap = new HashMap<>();
        parametersLongMap = new HashMap<>();
        driver = new Driver();
        dataBase = new DataBaseReader();
        log = new Log();
    }

    private void setFeatures() {
        if (isElementFound(mobileBtnField, 5000)) {
            mobileBtnField.get(0).click();
            // sprawdzanie, czy zdążył pobrać cały numer
            if (mobileField.getText().length() < 5) {
                sleeper(300);
                this.mobile = Long.parseLong(mobileField.getText().replaceAll("[\\D]", ""));
            } else
                this.mobile = Long.parseLong(mobileField.getText().replaceAll("[\\D]", ""));

        } else
            this.mobile = 0;

        if (isElementFound(dateOfIssueField, 500))
            this.dateOfIssue = converter.getDateFromDateOfIssue(dateOfIssueField.get(0).getText().trim());
        else
            this.dateOfIssue = null;

        if (isElementFound(titleField, 500))
            this.title = titleField.get(0).getText().trim();
        else
            this.title = null;

        this.price = Integer.parseInt(priceField.getAttribute("data-price").replaceAll("[ ,.-]", ""));

        this.offerId = Long.parseLong(offerIdField.getText());

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
        } else {
            if (isElementFound(dislayNumberListField, 0))
                for (WebElement displayNumber : dislayNumberListField)
                    click(displayNumber);

            this.description = descriptionField.getText().replaceAll("['\"]", "-");
        }

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
                } else if (key.equals("pierwszaRejestracja")) {
                    parametersMap.replace(key, converter.getDateFromPierwszaRejestracja(parametersMap.get(key)));
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
                } else if (key.equals("ofertaOd")) {
                    if (parametersMap.get(key).equals("Osoby prywatnej"))
                        parametersMap.replace(key, "1");
                    else if (parametersMap.get(key).equals("Firmy"))
                        parametersMap.replace(key, "0");

                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("paliwo")) {
                    switch (parametersMap.get(key)) {
                        case "Benzyna":
                            parametersMap.replace(key, "0");
                            break;
                        case "Diesel":
                            parametersMap.replace(key, "1");
                            break;
                        case "Benzyna+LPG":
                            parametersMap.replace(key, "2");
                            break;
                        case "Benzyna+CNG":
                            parametersMap.replace(key, "3");
                            break;
                        case "Elektryczny":
                            parametersMap.replace(key, "4");
                            break;
                        case "Etanol":
                            parametersMap.replace(key, "5");
                            break;
                        case "Hybryda":
                            parametersMap.replace(key, "6");
                            break;
                        case "Wodór":
                            parametersMap.replace(key, "7");
                            break;
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("skrzynia")) {
                    switch (parametersMap.get(key)) {
                        case "Manualna":
                            parametersMap.replace(key, "0");
                            break;
                        case "Automatyczna hydrauliczna (klasyczna)":
                            parametersMap.replace(key, "1");
                            break;
                        case "Automatyczna bezstopniowa (CVT)":
                            parametersMap.replace(key, "2");
                            break;
                        case "Automatyczna dwusprzęgłowa (DCT, DSG)":
                            parametersMap.replace(key, "3");
                            break;
                        case "Półautomatyczna (ASG, Tiptronic)":
                            parametersMap.replace(key, "4");
                            break;
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("naped")) {
                    switch (parametersMap.get(key)) {
                        case "Na przednie koła":
                            parametersMap.replace(key, "0");
                            break;
                        case "Na tylne koła":
                            parametersMap.replace(key, "1");
                            break;
                        case "4x4 (dołączany automatycznie)":
                            parametersMap.replace(key, "2");
                            break;
                        case "4x4 (dołączany ręcznie)":
                            parametersMap.replace(key, "3");
                            break;
                        case "4x4 (stały)":
                            parametersMap.replace(key, "4");
                            break;
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("typ")) {
                    switch (parametersMap.get(key)) {
                        case "Auta małe":
                            parametersMap.replace(key, "0");
                            break;
                        case "Auta miejskie":
                            parametersMap.replace(key, "1");
                            break;
                        case "Kompakt":
                            parametersMap.replace(key, "2");
                            break;
                        case "Sedan":
                            parametersMap.replace(key, "3");
                            break;
                        case "Kombi":
                            parametersMap.replace(key, "4");
                            break;
                        case "Minivan":
                            parametersMap.replace(key, "5");
                            break;
                        case "SUV":
                            parametersMap.replace(key, "6");
                            break;
                        case "Kabriolet":
                            parametersMap.replace(key, "7");
                            break;
                        case "Coupe":
                            parametersMap.replace(key, "8");
                            break;
                        default:
                            parametersMap.replace(key, "100");
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("stan")) {
                    switch (parametersMap.get(key)) {
                        case "Używane":
                            parametersMap.replace(key, "0");
                            break;
                        case "Nowe":
                            parametersMap.replace(key, "1");
                            break;
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("kolor")) {
                    switch (parametersMap.get(key)) {
                        case "Beżowy":
                            parametersMap.replace(key, "0");
                            break;
                        case "Biały":
                            parametersMap.replace(key, "1");
                            break;
                        case "Bordowy":
                            parametersMap.replace(key, "2");
                            break;
                        case "Brązowy":
                            parametersMap.replace(key, "3");
                            break;
                        case "Czarny":
                            parametersMap.replace(key, "4");
                            break;
                        case "Czerwony":
                            parametersMap.replace(key, "5");
                            break;
                        case "Fioletowy":
                            parametersMap.replace(key, "6");
                            break;
                        case "Niebieski":
                            parametersMap.replace(key, "7");
                            break;
                        case "Srebrny":
                            parametersMap.replace(key, "8");
                            break;
                        case "Szary":
                            parametersMap.replace(key, "9");
                            break;
                        case "Zielony":
                            parametersMap.replace(key, "10");
                            break;
                        case "Złoty":
                            parametersMap.replace(key, "11");
                            break;
                        case "Żółty":
                            parametersMap.replace(key, "12");
                            break;
                        case "Inny kolor":
                            parametersMap.replace(key, "13");
                            break;
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                } else if (key.equals("krajPochodzenia")) {
                    switch (parametersMap.get(key)) {
                        case "Austria":
                            parametersMap.replace(key, "0");
                            break;
                        case "Belgia":
                            parametersMap.replace(key, "1");
                            break;
                        case "Białoruś":
                            parametersMap.replace(key, "2");
                            break;
                        case "Bułgaria":
                            parametersMap.replace(key, "3");
                            break;
                        case "Chorwacja":
                            parametersMap.replace(key, "4");
                            break;
                        case "Czechy":
                            parametersMap.replace(key, "5");
                            break;
                        case "Dania":
                            parametersMap.replace(key, "6");
                            break;
                        case "Estonia":
                            parametersMap.replace(key, "7");
                            break;
                        case "Finlandia":
                            parametersMap.replace(key, "8");
                            break;
                        case "Francja":
                            parametersMap.replace(key, "9");
                            break;
                        case "Grecja":
                            parametersMap.replace(key, "10");
                            break;
                        case "Holandia":
                            parametersMap.replace(key, "11");
                            break;
                        case "Hiszpania":
                            parametersMap.replace(key, "12");
                            break;
                        case "Irlandia":
                            parametersMap.replace(key, "13");
                            break;
                        case "Islandia":
                            parametersMap.replace(key, "14");
                            break;
                        case "Kanada":
                            parametersMap.replace(key, "15");
                            break;
                        case "Liechtenstein":
                            parametersMap.replace(key, "16");
                            break;
                        case "Litwa":
                            parametersMap.replace(key, "17");
                            break;
                        case "Luksemburg":
                            parametersMap.replace(key, "18");
                            break;
                        case "Łotwa":
                            parametersMap.replace(key, "19");
                            break;
                        case "Monako":
                            parametersMap.replace(key, "20");
                            break;
                        case "Niemcy":
                            parametersMap.replace(key, "21");
                            break;
                        case "Norwegia":
                            parametersMap.replace(key, "22");
                            break;
                        case "Polska":
                            parametersMap.replace(key, "23");
                            break;
                        case "Rosja":
                            parametersMap.replace(key, "24");
                            break;
                        case "Rumunia":
                            parametersMap.replace(key, "25");
                            break;
                        case "Słowacja":
                            parametersMap.replace(key, "26");
                            break;
                        case "Słowenia":
                            parametersMap.replace(key, "27");
                            break;
                        case "Stany Zjednoczone":
                            parametersMap.replace(key, "28");
                            break;
                        case "Szwajcaria":
                            parametersMap.replace(key, "29");
                            break;
                        case "Szwecja":
                            parametersMap.replace(key, "30");
                            break;
                        case "Turcja":
                            parametersMap.replace(key, "31");
                            break;
                        case "Ukraina":
                            parametersMap.replace(key, "32");
                            break;
                        case "Węgry":
                            parametersMap.replace(key, "33");
                            break;
                        case "Wielka Brytania":
                            parametersMap.replace(key, "34");
                            break;
                        case "Włochy":
                            parametersMap.replace(key, "35");
                            break;
                        case "Inny":
                            parametersMap.replace(key, "36");
                            break;
                    }
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
            }
        }
        parametersStringMap = parametersMap;
    }

    public void openMultipleOffersAndSendDataToDataBase() {
        offersMap = new SearchPage().getIdAndLinkMap();
        int proceededCounter = 1;
        int failedCounter = 0;
        long startTime = System.currentTimeMillis();

        for (String x : offersMap.keySet()) {
            if ((proceededCounter + failedCounter) % 10 == 0) {
                double percentDone = Double.parseDouble(String.format("%.2f", (100.0 * ((double) (proceededCounter + failedCounter)
                        / (double) offersMap.size()))).replaceAll(",", "."));
                double passedTimeInMinutes = (System.currentTimeMillis() - startTime) / 60000.0;
                double timeLeftInMinutes = (100.0 / percentDone) * (passedTimeInMinutes) - passedTimeInMinutes + 1.0;


                calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, (int) timeLeftInMinutes);
                time = new SimpleDateFormat("HH:mm");


                log.logInfo("Operation is done in {" + percentDone + "%}, processing offer {" + (proceededCounter
                        + failedCounter) + "/" + offersMap.size() + "}, estimated time {"
                        + String.valueOf(timeLeftInMinutes).replaceAll(".\\d+$", "")
                        + " minutes}, it is about {" + time.format(calendar.getTime()) + "}");
            }

            saveTextToFile(offersMap.get(x), "lastLink", false);

            driver.getDriver().get(offersMap.get(x));

            if (!driver.getDriver().getCurrentUrl().equals(offersMap.get(x))) {
                sleeper(500);
                if (!driver.getDriver().getCurrentUrl().equals(offersMap.get(x))) {
                    log.logInfo("ERROR while loading page {" + offersMap.get(x) + "} (probably offer is no longer available)");
                    failedCounter++;
                    continue;
                }
            }
            fillParametersMap();
            dataBase.executeQuery(generateSQLQuery());
            proceededCounter++;
        }
        proceededCounter--;
        long operationTime = System.currentTimeMillis() - startTime;
        // / by zero Exception
        if (operationTime > 65000)
            log.logInfo("Operation is done in {100.00%}, proceeded offers {" + proceededCounter + "}, operation took {" +
                    (operationTime / 60000) + " minutes}, which is {" + proceededCounter / (operationTime / 60000) + "} offers per minute");
        else if (offersMap.size() == 0)
            log.logInfo("Operation is done in {100.00%}, proceeded offers {0}");
        else
            log.logInfo("Operation is done in {100.00%}, proceeded offers {" + proceededCounter + "}, operation took {less then minute)");
    }

    private String generateSQLQuery() {
        setFeatures();

        if (this.dateOfIssue != null)
            parametersStringMap.put("dateOfIssue", this.dateOfIssue);

        if (this.title != null)
            parametersStringMap.put("title", this.title);

        parametersIntMap.put("price", this.price);
        parametersLongMap.put("offerId", this.offerId);
        if (this.mobile != 0)
            parametersLongMap.put("mobile", this.mobile);

        parametersStringMap.put("location", this.location);
        parametersStringMap.put("sellerName", this.sellerName);
        if (this.equipment != null)
            parametersStringMap.put("equipment", this.equipment);

        if (this.description != null)
            parametersStringMap.put("description", this.description);


        String query = "INSERT INTO otomoto (";
        String queryFirstPart = "";

        for (String key : parametersStringMap.keySet())
            queryFirstPart += key.concat(", ");

        for (String key : parametersIntMap.keySet())
            queryFirstPart += key.concat(", ");

        for (String key : parametersLongMap.keySet())
            queryFirstPart += key.concat(", ");


        query += queryFirstPart.replaceAll("(, )$", "") + ") values (";
        String querySecondPart = "";

        for (String key : parametersStringMap.keySet())
            querySecondPart += "'".concat(parametersStringMap.get(key)).concat("', ");

        for (String key : parametersIntMap.keySet())
            querySecondPart += parametersIntMap.get(key) + ", ";

        for (String key : parametersLongMap.keySet())
            querySecondPart += parametersLongMap.get(key) + ", ";

        parametersLongMap.clear();
        parametersIntMap.clear();
        parametersStringMap.clear();
        this.mobile = 0;
        this.equipment = null;
        this.description = null;
        query += querySecondPart.replaceAll("(, )$", "") + ")";

        return query;
    }
}