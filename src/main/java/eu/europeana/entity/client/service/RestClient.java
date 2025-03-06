package eu.europeana.entity.client.service;

import static eu.europeana.entity.client.utils.EntityApiConstants.WSKEY;

import eu.europeana.entity.client.exception.MultipleEntityFoundException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.model.EntityRetrievalResponse;
import eu.europeana.entity.client.utils.EntityApiConstants;
import eu.europeana.entitymanagement.definitions.model.Entity;
import java.net.URI;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

public class RestClient {

  private static final Logger LOGGER = LogManager.getLogger(RestClient.class);

  /**
   * Methods returns the desired results. if getLocationHeader is true , return 'location' header value which is the non-redirect
   * url for existing Entity (entity ID) if getLocationHeader is false, returns the String json response
   *
   * @param webClient web client
   * @param uriBuilderURIFunction url to be executed
   * @param getLocationHeader true, if location header value is to be retrieved
   * @param <T> Desired result Type
   * @return
   * @throws TechnicalRuntimeException
   */
  public <T> T getEntityId(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction, boolean getLocationHeader)
      throws TechnicalRuntimeException {
    try {

      if (getLocationHeader) {
        return (T) executeGet(webClient, uriBuilderURIFunction, ResponseEntity.class)
            .flatMap(voidResponseEntity ->
                Mono.justOrEmpty(voidResponseEntity.getHeaders().getFirst(EntityApiConstants.HEADER_LOCATION)))
            .block();
      }
      return (T) executeGet(webClient, uriBuilderURIFunction, String.class).block();
    } catch (Exception e) {
      /*
       * Spring WebFlux wraps exceptions in ReactiveError (see Exceptions.propagate())
       * So we need to unwrap the underlying exception, for it to be handled by callers of this method
       **/
      Throwable t = Exceptions.unwrap(e);
      if (t instanceof TechnicalRuntimeException) {
        throw new TechnicalRuntimeException("User is not authorised to perform this action");
      }
      if (t instanceof MultipleEntityFoundException) {
        LOGGER.debug("Multiple Entity found - {} ", e.getMessage());
        return null;
      }
      // all other exception should be logged and null response should be returned
      LOGGER.debug("Entity API Client call failed - {}", e.getMessage());
      return null;
    }
  }

  /**
   * Returns the Single Entity class or List of Entities
   *
   * @param webClient
   * @param uriBuilderURIFunction
   * @param jsonBody if null, executes Get EM retrieval method. If present executes the multiple Entity Retrieval method.
   * @return
   */
  public <T> T getEntities(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction, String jsonBody) {
    try {
      if (jsonBody == null) {
        return (T) executeGet(webClient, uriBuilderURIFunction, Entity.class).block();
      } else {
        WebClient.ResponseSpec result = executePost(webClient, uriBuilderURIFunction, jsonBody);
        return (T) result
            .bodyToMono(EntityRetrievalResponse.class)
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
      // all other exception should be logged and null response should be returned
      LOGGER.debug("Entity Management Client call failed. {}", e.getMessage());
      return null;
    }
  }

  private <T> Mono<T> executeGet(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction, Class<T> entityClass)
      throws TechnicalRuntimeException {

    return webClient
        .get()
        .uri(uriBuilderURIFunction)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono( response -> {
          if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
            return response.bodyToMono(String.class).flatMap( errorBody -> Mono.error(new TechnicalRuntimeException("")));
          } else if (response.statusCode() == HttpStatus.MULTIPLE_CHOICES) {
            return response.bodyToMono(String.class).flatMap( errorBody -> Mono.error(new MultipleEntityFoundException("")));
          } else if (response.statusCode() == HttpStatus.MOVED_PERMANENTLY) {
            return getResponseFromLocation(webClient, response, entityClass);
          }  else  {
            return response.bodyToMono(entityClass);
          }
        });
  }

  private <T> Mono<T> getResponseFromLocation(WebClient webClient, ClientResponse response, Class<T> entityClass) {

    URI location = response.headers().asHttpHeaders().getLocation();
    String query = response.request().getURI().getQuery();
    final MultiValueMap<String, String> queryParams = UriComponentsBuilder.newInstance().
                                                                query(query).build().getQueryParams();
    String wsKey = queryParams.getFirst(WSKEY);
    LOGGER.debug("Redirecting to location: {}", location);

    if (location != null && wsKey != null) {
      return executeGet(webClient,
          uriBuilder -> uriBuilder.host(response.request().getURI().getAuthority())
                    .path(location.getPath().replace("/entity",""))
              .query(location.getQuery())
                    .build(), entityClass);
    } else {
      LOGGER.debug("Redirecting to location empty");
      return Mono.empty();
    }
  }

  private WebClient.ResponseSpec executePost(WebClient webClient, Function<UriBuilder, URI> uriBuilderURIFunction,
      String jsonBody) throws TechnicalRuntimeException {
    return webClient
        .post()
        .uri(uriBuilderURIFunction)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(jsonBody))
        .retrieve()
        .onStatus(
            HttpStatus.UNAUTHORIZED::equals,
            response -> response.bodyToMono(String.class).map(TechnicalRuntimeException::new));

  }
}
