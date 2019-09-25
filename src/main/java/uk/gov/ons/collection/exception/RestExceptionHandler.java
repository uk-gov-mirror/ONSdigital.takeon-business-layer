package uk.gov.ons.collection.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uk.gov.ons.collection.entity.ErrorResponse;

@Log4j2
@RestControllerAdvice
public class RestExceptionHandler {

    private static final String ERROR_MESSAGE = "Contributor does not exist";
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    private static final int HTTP_STATUS_OK_NO_RECORDS = 200;
    private static final String NO_DATA_ERROR_MESSAGE = "No data in the Persistance Layer database";

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse unKnownException(Exception ex)
    {
        log.error("Exception in Business Layer { }", ex);
        return new ErrorResponse(HTTP_STATUS_NOT_FOUND, ERROR_MESSAGE, ex.getMessage());
    }

    @ExceptionHandler(value = { DataNotFondException.class })
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse noDataFoundException(Exception ex)
    {
        return new ErrorResponse(HTTP_STATUS_OK_NO_RECORDS, NO_DATA_ERROR_MESSAGE, ex.getMessage());
    }
}
