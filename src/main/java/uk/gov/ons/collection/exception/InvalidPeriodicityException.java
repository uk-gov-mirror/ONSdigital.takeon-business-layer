package uk.gov.ons.collection.exception;

public class InvalidPeriodicityException extends Exception { 
    public InvalidPeriodicityException(String errorMessage) {
        super(errorMessage);
    }
}