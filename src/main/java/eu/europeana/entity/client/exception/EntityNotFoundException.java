package eu.europeana.entity.client.exception;

import eu.europeana.api.commons.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends EuropeanaApiException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public boolean doLogStacktrace() {
        return false;
    }
}

