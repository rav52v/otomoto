package main.poms;

import main.utils.PageBase;
import org.openqa.selenium.By;
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
        private String[] availableParameters;
        ParamNames(String...strings){
            availableParameters = strings;
        }
        private boolean ifContains(String value){
            for (String param : availableParameters) {
                if (param.equals(value))
                    return true;
            }
            return false;
        }
        private String transformParamName(String value){
            for(int i = 0 ; i<availableParameters.length ; i++){
                if(availableParameters[i].equals(value)){
                    return ParamNames.TRANSFORM_TO_PARAMETERS.availableParameters[i];
                }
            }
            return null;
        }
    }

    private Map<String, String> parametersMap;

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

    @FindBy(css = "h4.offer-features__title")
    private List<WebElement> equipment;

    @FindBy(css = "div.offer-features__row li.offer-features__item")
    private List<WebElement> equipmentList;

    // OFFER PARAMETERS //
    @FindBy(css = "#parameters > ul li.offer-params__item")
    private List<WebElement> offerParameters;
    //\OFFER PARAMETERS\//


    public ItemPage() {
        parametersMap = new HashMap<>();
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
        if (isElementFound(readMoreBtn, 1000)) {
            click(readMoreBtn.get(0));
        }
        return description.getText();
    }

    // może zwracać null //
    private String getEquipment() {
        if (isElementFound(equipment, 800)) {
            StringBuilder sb = new StringBuilder();
            for (WebElement feature : equipmentList) {
                sb.append(feature.getText().trim()).append(", ");
            }
            return sb.toString().replaceAll(", $", "");
        } else {
            return null;
        }
    }

    //wypełnienie mapy parametrów oraz dostosowanie danych typu int pod bazę danych
    public void fillParametersMap() {
        for (int i = 0; i < offerParameters.size(); i++) {
            String paramName = offerParameters.get(i).findElement(By.tagName("span")).getText().trim();
            String paramValue = offerParameters.get(i).findElement(By.tagName("div")).getText().trim();
            if(ParamNames.AVAILABLE_PARAMETERS.ifContains(paramName)){
                String key = ParamNames.AVAILABLE_PARAMETERS.transformParamName(paramName);
                parametersMap.put(key, paramValue);
                if (key.equals("przebieg")){
                    parametersMap.replace(key, parametersMap.get(key).replaceAll(" km| ", ""));
                }
                else if(key.equals("uszkodzony") || key.equals("anglik") || key.equals("pl") || key.equals("tuning")
                        || key.equals("aso") || key.equals("zabytek") || key.equals("bezwypadkowy")){
                    parametersMap.replace(key, "1");
                }
            }
        }
    }
}