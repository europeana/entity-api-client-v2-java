package eu.europeana.entity.client.exception;

import eu.europeana.api.commons.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends EuropeanaApiException {

    public AuthenticationException(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public boolean doLogStacktrace() {
        return false;
    }
}
