package uk.gov.ons.collection.exception;

public class apiRequestException extends Exception { 

    private static final long serialVersionUID = -4931629994037347697L;

    public apiRequestException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public apiRequestException(String errorMessage) {
        super(errorMessage);
    }
    
}