package eu.europeana.entity.client.exception;

import eu.europeana.api.commons.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

public class EntityMismatchException extends EuropeanaApiException {

    public EntityMismatchException(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public boolean doLogStacktrace() {
        return false;
    }
}

