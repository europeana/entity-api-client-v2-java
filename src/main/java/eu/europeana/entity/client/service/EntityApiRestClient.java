package eu.europeana.entity.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

public class EntityApiRestClient extends RestClient {

    private static final Logger LOGGER = LogManager.getLogger(EntityApiRestClient.class);
    private final WebClient webClient;
    private final String wskey;

    public EntityApiRestClient(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    public List<String> retrieveSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws JsonProcessingException, TechnicalRuntimeException {
        String results = getEntityId(webClient,
                EntityClientUtils.buildSuggestUrl(text, language, scope, type, rows, algorithm, wskey), false);
        List<String> entities = EntityClientUtils.getEntityApiResults(results);
        if (entities.isEmpty()) {
            LOGGER.debug("No entity found for suggest text={}, lang={}, type={}", text, language, type);
        }
        LOGGER.debug("{} entities found for suggest text={}, lang={}, type={}", entities.size(), text, language, type);
        return entities;
    }

    /**
     * Returns the value present in location response header,
     * if the endpoint returns 301 (Moved Permanently).
     * Otherwise, if endpoint returns,
     *   300 (Multiple Choices) for a uri ie; more than one entity for one uri,
     *   empty list will be returned
     *
     * @param uri
     * @return
     * @throws TechnicalRuntimeException
     */
    public List<String> retrieveEntityByUri(String uri) throws TechnicalRuntimeException {
        String entityId = getEntityId(webClient,
                EntityClientUtils.buildEntityResolveUrl(uri, wskey), true);
        if(StringUtils.isNotEmpty(entityId)) {
            LOGGER.debug("{} entity found for uri={} ", entityId, uri);
            return Collections.singletonList("\"" + entityId + "\"");
        }
        LOGGER.debug("No entity found for resolve uri={}", uri);
        return Collections.emptyList();
    }

    public List<String> retrieveEnrichment(String text, String language, String type, String rows)
            throws JsonProcessingException, TechnicalRuntimeException {
        String results = getEntityId(webClient,
                EntityClientUtils.buildEntityEnrichUrl(text, language, type, rows, wskey), false);
        List<String> entities = EntityClientUtils.getEntityApiResults(results);
        if (entities.isEmpty()) {
            LOGGER.debug("No entity found for enrich text={}, lang={}, type={}", text, language, type);
        }
        LOGGER.debug("{} entities found for enrich text={}, lang={}, type={}", entities.size(), text, language, type);
        return entities;
    }

}
