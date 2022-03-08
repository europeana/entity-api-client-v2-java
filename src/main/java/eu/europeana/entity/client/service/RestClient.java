package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.EntityNotFoundException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.Exceptions;

import java.net.URI;
import java.util.function.Function;

public class RestClient {

    public WebClient.ResponseSpec executeGet(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction) throws TechnicalRuntimeException {
        try {
            return webClient
                    .get()
                    .uri(uriBuilderURIFunction)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                            HttpStatus.UNAUTHORIZED::equals,
                            response -> response.bodyToMono(String.class).map(TechnicalRuntimeException::new))
                    .onStatus(HttpStatus.NOT_FOUND::equals,
                            response -> response.bodyToMono(String.class).map(EntityNotFoundException::new));
        } catch (Exception e) {
            /*
             * Spring WebFlux wraps exceptions in ReactiveError (see Exceptions.propagate())
             * So we need to unwrap the underlying exception, for it to be handled by callers of this method
             **/
            Throwable t = Exceptions.unwrap(e);
            if (t instanceof TechnicalRuntimeException) {
                throw new TechnicalRuntimeException("User is not authorised to perform this action");
            }
            if (t instanceof EntityNotFoundException) {
                return null;
            }
            // all other exception should be propagated
            throw e;
        }
    }
}
