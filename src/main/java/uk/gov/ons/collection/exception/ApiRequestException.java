package uk.gov.ons.collection.exception;

public class ApiRequestException extends Exception {

    private static final long serialVersionUID = -4931629994037347697L;

    public ApiRequestException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ApiRequestException(String errorMessage) {
        super(errorMessage);
    }
    
}