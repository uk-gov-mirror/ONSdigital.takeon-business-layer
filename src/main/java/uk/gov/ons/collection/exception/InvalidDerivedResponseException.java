package uk.gov.ons.collection.exception;

public class InvalidDerivedResponseException extends Exception { 
    
    private static final long serialVersionUID = 1L;

    public InvalidDerivedResponseException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidDerivedResponseException(String errorMessage) {
        super(errorMessage);
    }
    
}