package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.EntityNotFoundException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.model.EntityRetrievalResponse;
import eu.europeana.entity.client.utils.EntityApiConstants;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

public class RestClient {

    /**
     * Methods returns the desired results
     * If getEntity is true, returns the EM Model Entity class
     * Else, checks :
     *         if getLocationHeader is true , return 'location' header value
     *              which is the non-redirect url for existing Entity (entity ID)
     *         if getLocationHeader is false, returns the String json response
     *
     * @param webClient web client
     * @param uriBuilderURIFunction url to be executed
     * @param getEntity true , if Entity class to be retrieved
     * @param getLocationHeader true, if location header value is to be retrieved
     * @param <T> Desired result Type
     * @return
     * @throws TechnicalRuntimeException
     */
    public <T> T getEntityResults(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction,
                                  boolean getEntity, boolean getLocationHeader) throws TechnicalRuntimeException {
        try {
            WebClient.ResponseSpec result = executeGet(webClient, uriBuilderURIFunction);
             if (getEntity) {
                 return (T) result.bodyToMono(Entity.class).block();
             } else {
                 if (getLocationHeader) {
                     return (T) result.toBodilessEntity()
                             .flatMap(voidResponseEntity ->
                                     Mono.justOrEmpty(voidResponseEntity.getHeaders().getFirst(EntityApiConstants.HEADER_LOCATION)))
                             .block();
                 }
                 return (T) result
                         .bodyToMono(String.class)
                         .block();
             }
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

    /**
     * Returns the List of Entities
     * @param webClient
     * @param uriBuilderURIFunction
     * @param jsonBody
     * @return
     */
    public EntityRetrievalResponse postEntityResults(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction, String jsonBody) {
        try {
            WebClient.ResponseSpec result = executePost(webClient, uriBuilderURIFunction, jsonBody);
            return result
                    .bodyToMono(EntityRetrievalResponse.class)
                    .block();
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

    private WebClient.ResponseSpec executeGet(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction) throws TechnicalRuntimeException {
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
    }

    private WebClient.ResponseSpec executePost(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction, String jsonBody) throws TechnicalRuntimeException {
        return webClient
                .post()
                .uri(uriBuilderURIFunction)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve()
                .onStatus(
                        HttpStatus.UNAUTHORIZED::equals,
                        response -> response.bodyToMono(String.class).map(TechnicalRuntimeException::new))
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(String.class).map(EntityNotFoundException::new));

    }
}
