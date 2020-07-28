package uk.gov.ons.collection.exception;

public class InvalidIdbrPeriodException extends Exception { 

    private static final long serialVersionUID = 3908853982036968970L;

    public InvalidIdbrPeriodException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidIdbrPeriodException(String errorMessage) {
        super(errorMessage);
    }
    
}