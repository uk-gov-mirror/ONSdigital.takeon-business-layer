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

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse unKnownException(Exception ex)
    {
        log.error("Exception in Business Layer { }", ex);
        return new ErrorResponse(404, "Contributor does not exist", ex.getMessage());
    }

    @ExceptionHandler(value = { DataNotFondException.class })
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse noDataFoundException(Exception ex)
    {
        return new ErrorResponse(200, "No data in the Persistance Layer database", ex.getMessage());
    }
}
