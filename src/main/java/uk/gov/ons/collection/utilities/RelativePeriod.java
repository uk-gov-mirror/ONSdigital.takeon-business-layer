package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.exception.InvalidIdbrPeriodException;
import uk.gov.ons.collection.exception.InvalidPeriodicityException;

public class RelativePeriod {
    
    private final String periodicity;
    private int monthlyFrequency;
    
    public RelativePeriod(String periodicity) throws InvalidPeriodicityException {
        if (periodicity == null) {
            throw new InvalidPeriodicityException("Null periodicity given");
        }
        this.periodicity = periodicity;
        setMonthFrequency();
    }

    public String calculateRelativePeriod(int offset, String startingPeriod) throws InvalidIdbrPeriodException {
        String outputPeriod = startingPeriod;
        for (int i = 0; i < offset; i++){
            outputPeriod = getPreviousPeriod(outputPeriod);
        }
        return outputPeriod;
    }

    private static int getYear(String idbrPeriod) throws InvalidIdbrPeriodException {
        return safeNumberExtract(idbrPeriod,0,4);
    }

    private static int getMonth(String idbrPeriod) throws InvalidIdbrPeriodException {
        int month = safeNumberExtract(idbrPeriod,4,6);
        if (month > 12) {
            throw new InvalidIdbrPeriodException("Given starting period month is too large: " + idbrPeriod);
        }   
        return month;     
    }

    private static int safeNumberExtract(String idbrPeriod, int start, int end) throws InvalidIdbrPeriodException {    
        try {
            return Integer.parseInt(idbrPeriod.substring(start,end)); 
        }
        catch (NullPointerException err) {
            throw new InvalidIdbrPeriodException("Null starting period provided", err);
        }
        catch (NumberFormatException err) {
            throw new InvalidIdbrPeriodException("Unable to convert starting period into numeric format: " + idbrPeriod, err);
        }
        catch (IndexOutOfBoundsException err) {
            throw new InvalidIdbrPeriodException("Given starting period is too short: " + idbrPeriod, err);
        }    
    }

    public static void validatePeriodLength(String idbrPeriod) throws InvalidIdbrPeriodException {
        if (idbrPeriod.length() != 6) {
            throw new InvalidIdbrPeriodException("Given period is wrong length: " + idbrPeriod);
        }  
    }

    public String getPreviousPeriod(String startingPeriod) throws InvalidIdbrPeriodException {         
        int currentYear = getYear(startingPeriod);
        int currentMonth = getMonth(startingPeriod);  
        validatePeriodLength(startingPeriod);        
        int previousMonth = currentMonth - monthlyFrequency;
        int previousYear = currentYear;    
        if (previousMonth < 1) {
            previousMonth += 12;
            previousYear -= 1;
        }    
        return String.valueOf(previousYear) + paddedMonth(previousMonth);
    }

    public String paddedMonth(int month) {
        if (month < 10) {
            return "0" + String.valueOf(month);
        }
        return String.valueOf(month);
    }

    private void setMonthFrequency() throws InvalidPeriodicityException {
        switch (periodicity.toLowerCase()) {
            case "monthly":     monthlyFrequency=1; break;
            case "quarterly":   monthlyFrequency=3; break;
            case "biannual":    monthlyFrequency=6; break;
            case "annual":      monthlyFrequency=12; break;
            default:            throw new InvalidPeriodicityException("Invalid periodicity given: " + periodicity);
        }
    }

}
