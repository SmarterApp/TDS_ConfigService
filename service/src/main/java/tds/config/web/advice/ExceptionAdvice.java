package tds.config.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tds.config.web.resources.ExceptionMessageResource;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionAdvice.class);

    // TODO:  Find better/more appropriate exception?  Make our own?
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    ResponseEntity<ExceptionMessageResource> handleNotFoundException(final NoSuchElementException ex) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Not Found exception occurred", ex);
        } else {
            LOG.error("Not Found exception occurred: {}", ex.getLocalizedMessage());
        }

        return new ResponseEntity<>(
                new ExceptionMessageResource(HttpStatus.NOT_FOUND.toString(), ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ResponseEntity<ExceptionMessageResource> handleException(final Exception ex) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Exception occurred", ex);
        } else {
            LOG.error("Exception occurred: {}", ex.getLocalizedMessage());
        }

        return new ResponseEntity<>(
                new ExceptionMessageResource(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
