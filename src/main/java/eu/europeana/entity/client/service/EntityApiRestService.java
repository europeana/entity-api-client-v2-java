package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.AuthenticationException;
import eu.europeana.entity.client.exception.EntityNotFoundException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class EntityApiRestService {

    private static final Logger LOGGER = LogManager.getLogger(EntityApiRestService.class);
    private final WebClient webClient;
    private final String wskey;

    public EntityApiRestService(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    /**
     * @param uriBuilderURIFunction url for the request
     * @param getLocationHeader  if we want to fetch only the response header 'location' value
     *                        This is done to avoid the redirect issues and for directly fetching the entity Id
     * @return
     * @throws AuthenticationException
     */
    private String executeGet(Function<UriBuilder, URI> uriBuilderURIFunction, boolean getLocationHeader) throws AuthenticationException {
        try {
            WebClient.ResponseSpec result = webClient
                    .get()
                    .uri(uriBuilderURIFunction)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                            HttpStatus.UNAUTHORIZED::equals,
                            response -> response.bodyToMono(String.class).map(AuthenticationException::new))
                    .onStatus(HttpStatus.NOT_FOUND :: equals,
                            response -> response.bodyToMono(String.class).map(EntityNotFoundException::new));
            if (getLocationHeader) {
                return result.toBodilessEntity()
                        .flatMap(voidResponseEntity ->
                                Mono.justOrEmpty(voidResponseEntity.getHeaders().getFirst(EntityClientUtils.HEADER_LOCATION)))
                        .block();
            }
            return result
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            /*
             * Spring WebFlux wraps exceptions in ReactiveError (see Exceptions.propagate())
             * So we need to unwrap the underlying exception, for it to be handled by callers of this method
             **/
            Throwable t = Exceptions.unwrap(e);
            if (t instanceof AuthenticationException) {
                throw new AuthenticationException("User is not authorised to perform this action");
            }
            if (t instanceof EntityNotFoundException) {
                return null;
            }
            // all other exception should be propagated
            throw e;
        }
    }

    public List<Entity> retrieveSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws JSONException, UnsupportedEntityTypeException, AuthenticationException {
        String results = executeGet(EntityClientUtils.buildSuggestUrl(text, language, scope, type, rows, algorithm, wskey), false);
        JSONObject jsonObject = new JSONObject(results);
        // process only if results are present
        if (EntityClientUtils.getTotalValue(jsonObject) > 0) {
            return EntityClientUtils.getSuggestResults(jsonObject);
        }
        LOGGER.error("No entity found for suggest text={}, lang={}, type={}", text, language, type);
        return Collections.emptyList();
    }

    public List<Entity> retrieveEntityByUri(String uri) throws UnsupportedEntityTypeException, AuthenticationException {
        String entityId = executeGet(EntityClientUtils.buildEntityResolveUrl(uri, wskey), true);
        if(StringUtils.isNotEmpty(entityId)) {
            return EntityClientUtils.getResolveResults(entityId);
        }
        LOGGER.error("No entity found for resolve uri={}", uri);
        return Collections.emptyList();
    }

}
