package uk.gov.ons.collection.exception;

public class InvalidIdbrPeriodException extends Exception { 
    public InvalidIdbrPeriodException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public InvalidIdbrPeriodException(String errorMessage) {
        super(errorMessage);
    }
}