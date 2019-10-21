package uk.gov.ons.collection.exception;

public class InvalidJsonException extends Exception { 

    private static final long serialVersionUID = 662214831615700686L;

    public InvalidJsonException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidJsonException(String errorMessage) {
        super(errorMessage);
    }
    
}