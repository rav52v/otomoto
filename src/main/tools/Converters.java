package main.tools;

public class Converters {

    public String getDateFromDateOfIssue(String dateOfIssue){
        return convertDateToSqlFormat(dateOfIssue.replaceAll("^.+, ", ""));
    }

    public String getDateFromPierwszaRejestracja(String pierwszaRejestracja){
        return convertDateToSqlFormat(pierwszaRejestracja);
    }

    private String convertDateToSqlFormat(String date){
        return String.format("%04d-%02d-%02d", getYear(date), getMonth(date), getDay(date));
    }

    private int getYear(String date){
        return Integer.parseInt(date.substring(date.length() - 4));
    }

    private int getMonth(String date){
        String result = date.replaceAll("[\\d]|[\\s]", "");

        if (result.equals("styczeń") || result.equals("stycznia")){
            result = "01";
        } else if (result.equals("luty") || result.equals("lutego")){
            result = "02";
        } else if (result.equals("marzec") || result.equals("marca")){
            result = "03";
        } else if (result.equals("kwiecień") || result.equals("kwietnia")){
            result = "04";
        } else if (result.equals("maj") || result.equals("maja")){
            result = "05";
        } else if (result.equals("czerwiec") || result.equals("czerwca")){
            result = "06";
        } else if (result.equals("lipiec") || result.equals("lipca")){
            result = "07";
        } else if (result.equals("sierpień") || result.equals("sierpnia")){
            result = "08";
        } else if (result.equals("wrzesień") || result.equals("września")){
            result = "09";
        } else if (result.equals("październik") || result.equals("października")){
            result = "10";
        } else if (result.equals("listopad") || result.equals("listopada")){
            result = "11";
        } else if (result.equals("grudzień") || result.equals("grudnia")){
            result = "12";
        }
        return Integer.parseInt(result);
    }

    private int getDay(String date){
        return Integer.parseInt(date.replaceAll(" .+ \\d{4}$", ""));
    }
}
