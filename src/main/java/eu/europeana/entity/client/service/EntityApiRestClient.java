package eu.europeana.entity.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.exception.EntityNotFoundException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;;
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

public class EntityApiRestClient extends RestClient {

    private static final Logger LOGGER = LogManager.getLogger(EntityApiRestClient.class);
    private final WebClient webClient;
    private final String wskey;

    public EntityApiRestClient(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    private String getEntityIds(Function<UriBuilder, URI> uriBuilderURIFunction, boolean getLocationHeader) throws TechnicalRuntimeException {
        WebClient.ResponseSpec result = executeGet(webClient, uriBuilderURIFunction);
        if (getLocationHeader) {
            return result.toBodilessEntity()
                    .flatMap(voidResponseEntity ->
                            Mono.justOrEmpty(voidResponseEntity.getHeaders().getFirst(EntityClientUtils.HEADER_LOCATION)))
                    .block();
        }
        return result
                .bodyToMono(String.class)
                .block();
    }

    public List<String> retrieveSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws JsonProcessingException, TechnicalRuntimeException {
        String results = getEntityIds(EntityClientUtils.buildSuggestUrl(text, language, scope, type, rows, algorithm, wskey), false);
        List<String> entities = EntityClientUtils.getSuggestResults(results);
        if (entities.isEmpty()) {
            LOGGER.debug("No entity found for suggest text={}, lang={}, type={}", text, language, type);
        }
        LOGGER.debug("{} entities found for suggest text={}, lang={}, type={}", entities.size(), text, language, type);
        return entities;
    }

    public List<String> retrieveEntityByUri(String uri) throws TechnicalRuntimeException {
        String entityId = getEntityIds(EntityClientUtils.buildEntityResolveUrl(uri, wskey), true);
        if(StringUtils.isNotEmpty(entityId)) {
            LOGGER.debug("{} entity found for uri={} ", entityId, uri);
            return Collections.singletonList(entityId);
        }
        LOGGER.debug("No entity found for resolve uri={}", uri);
        return Collections.emptyList();
    }

}