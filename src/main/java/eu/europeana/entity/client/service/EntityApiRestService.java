package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.AuthenticationException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.util.UriBuilder;
import reactor.core.Exceptions;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class EntityApiRestService {

    private final WebClient webClient;
    private final String wskey;

    public EntityApiRestService(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    private String executeGet(Function<UriBuilder, URI> uriBuilderURIFunction) throws AuthenticationException {
        try {
            return webClient
                    .get()
                    .uri(uriBuilderURIFunction)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                            HttpStatus.UNAUTHORIZED::equals,
                            response -> response.bodyToMono(String.class).map(AuthenticationException::new))
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            /*
             * Spring WebFlux wraps exceptions in ReactiveError (see Exceptions.propagate())
             * So we need to unwrap the underlying exception, for it to be handled by callers of this method
             **/
            Throwable t = Exceptions.unwrap(e);
            if(t instanceof AuthenticationException) {
                throw new AuthenticationException("User is not authorised to perform this action");
            }
            // all other exception should be propagated
            throw e;
        }
    }

    public List<Entity> retrieveSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws JSONException, UnsupportedEntityTypeException, AuthenticationException {
        String results = executeGet(EntityClientUtils.buildSuggestUrl(text, language, scope, type, rows, algorithm, wskey));
        JSONObject jsonObject = new JSONObject(results);
        // process only if results are present
        if (EntityClientUtils.getTotalValue(jsonObject) > 0) {
            return EntityClientUtils.getSuggestResults(jsonObject);
        }
        return Collections.emptyList();
    }

    public List<Entity> retrieveEntityByUri(String uri) throws JSONException, UnsupportedEntityTypeException, AuthenticationException {
        String results = executeGet(EntityClientUtils.buildEntityResolveUrl(uri, wskey));
        JSONObject jsonObject = new JSONObject(results);
        return EntityClientUtils.getResolveResults(jsonObject);
    }

}
