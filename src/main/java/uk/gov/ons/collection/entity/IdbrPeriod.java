package uk.gov.ons.collection.entity;

import java.time.Period;

public class IdbrPeriod{
    private final String period;
    private final String periodicity;

    
    public IdbrPeriod(String period, String periodicity) {
        this.period = period;
        this.periodicity = periodicity;
    }


    public String calculatePeriod(int offset) {
        String year = period.substring(0,4);
        String month = period.substring(4);
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        if (periodicity.equals("Annual")){
            yearInt--;
            return String.valueOf(yearInt) + "12";
        }
        else if (periodicity.equals("Quarterly")){
            return year + "0" + String.valueOf(monthInt-3);            
        };
        return period;
    }
}

// Create a class + methods to calculate periods

