package uk.gov.ons.collection.exception;

public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7446956748714875568L;

    public DataNotFoundException(String message) {
        super(message);
    }
    
}
