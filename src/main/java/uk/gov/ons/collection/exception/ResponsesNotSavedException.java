package uk.gov.ons.collection.exception;

public class ResponsesNotSavedException extends Exception { 

    private static final long serialVersionUID = -2616675764562140610L;

    public ResponsesNotSavedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ResponsesNotSavedException(String errorMessage) {
        super(errorMessage);
    }
    
}