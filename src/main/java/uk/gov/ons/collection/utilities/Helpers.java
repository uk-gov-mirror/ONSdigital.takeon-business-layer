package uk.gov.ons.collection.utilities;

public class Helpers {

    public String buildUriParameters(String reference, String period, String survey){
        return "reference="+reference+";"+"period="+period+";"+"survey="+survey;
    }

}
