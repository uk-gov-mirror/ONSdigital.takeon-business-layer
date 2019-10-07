package uk.gov.ons.collection.exception;

public class InvalidJsonException extends Exception { 
    public InvalidJsonException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public InvalidJsonException(String errorMessage) {
        super(errorMessage);
    }
}