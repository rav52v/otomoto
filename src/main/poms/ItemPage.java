package main.poms;

import main.tools.DataBaseReader;
import main.utils.Driver;
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

        ParamNames(String...strings){
            values = strings;
        }
        private boolean ifContains(String value){
            for (String param : values) {
                if (param.equals(value))
                    return true;
            }
            return false;
        }
        private String transformParamName(String value){
            for(int i = 0; i< values.length ; i++){
                if(values[i].equals(value)){
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
    private WebDriver driver;
    private DataBaseReader dataBase;

    @FindBy(css = ".offer-content__aside .seller-box__seller-address__label")
    private WebElement location;

    @FindBy(css = ".seller-phones__button")
    private WebElement mobileBtn;

    @FindBy(css = ".offer-content__aside .phone-number ")
    private WebElement mobile;

    @FindBy(css = ".offer-content__aside .seller-box__seller-name")
    private WebElement sellerName;

    @FindBy(css = ".offer-content__metabar > div > span:first-child > span:last-child")
    private WebElement dateOfIssue;

    @FindBy(css = "div.offer-price")
    private WebElement price;

    @FindBy(css = "h1.offer-title.big-text")
    private WebElement title;

    @FindBy(css = ".offer-content__metabar > div > span:last-child > span:last-child")
    private WebElement offerId;

    @FindBy(css = "a.read-more")
    private List<WebElement> readMoreBtn;

    @FindBy(css = "#description > div")
    private WebElement description;

    @FindBy(css = "span.showPhoneButton")
    private List<WebElement> dislayNumberList;

    @FindBy(css = "h4.offer-features__title")
    private List<WebElement> equipment;

    @FindBy(css = "div.offer-features__row li.offer-features__item")
    private List<WebElement> equipmentList;

    @FindBy(css = "#parameters > ul li.offer-params__item")
    private List<WebElement> offerParameters;


    public ItemPage() {
        parametersMap = new HashMap<>();
        parametersStringMap = new HashMap<>();
        parametersIntMap = new HashMap<>();
        parametersLongMap = new HashMap<>();
        driver = new Driver().getDriver();
        dataBase = new DataBaseReader();
    }

    private String getLocation() {
        return this.location.getText().trim();
    }

    private int getMobile() {
        mobileBtn.click();
        return Integer.parseInt(this.mobile.getText().replaceAll("[ ]", ""));
    }

    private String getSellerName() {
        return sellerName.getText().trim();
    }

    private String getDateOfIssue() {
        return this.dateOfIssue.getText().trim();
    }

    private int getPrice() {
        return Integer.parseInt(this.price.getAttribute("data-price").replaceAll("[ ]", ""));
    }

    private String getTitle() {
        return this.title.getText().trim();
    }

    private long getOfferId() {
        return Long.parseLong(this.offerId.getText());
    }

    private String getDescription() {
        if (isElementFound(readMoreBtn, 100))
            click(readMoreBtn.get(0));
        if (isElementFound(dislayNumberList, 0)) {
            for(WebElement displayNumber : dislayNumberList)
                click(displayNumber);
        }
        return description.getText();
    }

    private String getEquipment() {
        if (isElementFound(equipment, 300)) {
            StringBuilder sb = new StringBuilder();
            for (WebElement feature : equipmentList)
                sb.append(feature.getText().trim()).append(", ");
            return sb.toString().replaceAll(", $", "");
        } else {
            return null;
        }
    }

    //wypełnienie mapy parametrów oraz dostosowanie danych typu int pod bazę danych
    private void fillParametersMap() {
        for (int i = 0; i < offerParameters.size(); i++) {
            String paramName = offerParameters.get(i).findElement(By.tagName("span")).getText().trim();
            String paramValue = offerParameters.get(i).findElement(By.tagName("div")).getText().trim();
            if(ParamNames.AVAILABLE_PARAMETERS.ifContains(paramName)){
                String key = ParamNames.AVAILABLE_PARAMETERS.transformParamName(paramName);
                parametersMap.put(key, paramValue);
                if (key.equals("przebieg")){
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" km| ", ""));
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
                else if(key.equals("uszkodzony") || key.equals("anglik") || key.equals("pl") || key.equals("tuning")
                        || key.equals("aso") || key.equals("zabytek") || key.equals("bezwypadkowy")){
                    parametersMap.replace(key, "1");
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
                else if(key.equals("moc")){
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" KM| ", ""));
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
                else if(key.equals("pojemnosc")){
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" cm3| ", ""));
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
                else if(key.equals("miejsca") || key.equals("drzwi") || key.equals("rok")){
                    parametersIntMap.put(key, Integer.parseInt(parametersMap.get(key)));
                    parametersMap.remove(key);
                }
            }
        }
        parametersStringMap = parametersMap;
    }

    public void openMultipleOffersAndSendDataToDataBase(){
        Map<String, String> offersMap = dataBase.cleanMapFromExistingRecords(SearchPage.getIdAndLinkMap());

        for(String x : offersMap.keySet()){
            driver.get(offersMap.get(x));
            fillParametersMap();

            System.out.println(generateSQLQuery());
        }
    }

    private String generateSQLQuery(){
        parametersStringMap.put("location", getLocation());
        parametersStringMap.put("sellerName", getSellerName());
        parametersStringMap.put("dateOfIssue", getDateOfIssue());
        parametersStringMap.put("title", getTitle());
        if(getDescription() != null){
            parametersStringMap.put("description", getDescription());
        }
        if(getEquipment() != null){
            parametersStringMap.put("equipment", getEquipment());
        }
        parametersIntMap.put("mobile", getMobile());
        parametersIntMap.put("price", getPrice());
        parametersLongMap.put("offerId", getOfferId());

        int allMapsSize = parametersStringMap.size() + parametersIntMap.size() + parametersLongMap.size();
        String query = "INSERT INTO otomoto (";
        String queryFirstPart = "";

        for (String key : parametersStringMap.keySet()){
            queryFirstPart += key.concat(", ");
        }
        for (String key : parametersIntMap.keySet()){
            queryFirstPart += key.concat(", ");
        }
        for (String key : parametersLongMap.keySet()){
            queryFirstPart += key;
        }
        queryFirstPart.replaceAll("(, )$", "");
        query += queryFirstPart + ")";
        return query;
    }
}