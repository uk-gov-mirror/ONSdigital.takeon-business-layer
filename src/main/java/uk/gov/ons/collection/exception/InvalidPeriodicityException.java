package uk.gov.ons.collection.exception;

public class InvalidPeriodicityException extends Exception { 

    private static final long serialVersionUID = 3778061376233663441L;

    public InvalidPeriodicityException(String errorMessage) {
        super(errorMessage);
    }
}