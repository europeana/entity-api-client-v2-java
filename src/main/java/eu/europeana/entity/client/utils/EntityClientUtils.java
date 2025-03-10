package eu.europeana.entity.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.utils.EntityRecordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class EntityClientUtils extends EntityApiConstants {

    private static final Logger LOGGER = LogManager.getLogger(EntityClientUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private EntityClientUtils() {
        // to hide implicit one
    }

    /**
     * Builds the Entity Api suggest url
     * @param text
     * @param language
     * @param scope
     * @param type
     * @param rows
     * @param algorithm
     * @param wskey
     * @return
     */
    public static Function<UriBuilder, URI> buildSuggestUrl(String text, String language, String scope, String type, String rows, String algorithm, String wskey) {
        return uriBuilder -> {
            UriBuilder builder =
                    uriBuilder
                            .path(PATH_SEPERATOR + SUGGEST_PATH)
                            .queryParam(WSKEY, wskey)
                            .queryParam(TEXT, text);
            if (language != null) {
                builder.queryParam(LANGUAGE, language);
            }
            if (scope != null) {
                builder.queryParam(SCOPE, scope);
            }
            if (type != null) {
                builder.queryParam(TYPE, type);
            }
            if (rows != null) {
                builder.queryParam(ROWS, rows);
            }
            if (algorithm != null) {
                builder.queryParam(ALGORITHM, algorithm);
            }
            return builder.build();
        };
    }

    public static Function<UriBuilder, URI> buildEntityEnrichUrl(String text, String lang, String type, String rows, String wskey) {
        return uriBuilder -> {
            UriBuilder builder =
                    uriBuilder
                            .path(PATH_SEPERATOR + ENRICH_PATH)
                            .queryParam(WSKEY, wskey)
                            .queryParam(TEXT, text);
            if (lang != null) {
                builder.queryParam(LANG, lang);
            }
            if (type != null) {
                builder.queryParam(TYPE, type);
            }
            if (rows != null) {
                builder.queryParam(ROWS, rows);
            }
            return builder.build();
        };
    }


    /**
     * Builds the Entity Api resolve url
     * @param uri
     * @param wsKey
     * @return
     */
    public static Function<UriBuilder, URI> buildEntityResolveUrl(String uri, String wsKey) {
        return uriBuilder -> {
            UriBuilder builder =
                    uriBuilder
                            .path(PATH_SEPERATOR + RESOLVE_PATH)
                            .queryParam(URI, uri)
                            .queryParam(WSKEY, wsKey);
            return builder.build();
        };
    }

    /**
     * Builds the Entity management entity retrieval url
     * @param id
     * @param wsKey
     * @return
     */
    public static Function<UriBuilder, URI> buildEntityRetrievalUrl(String id, String wsKey) {
        return uriBuilder -> {
            UriBuilder builder =
                    uriBuilder
                            .path(id)
                            .queryParam(WSKEY, wsKey);
            return builder.build();
        };
    }

    /**
     * Builds the Entity management multiple entity retrieval url
     * @param wsKey
     * @return
     */
    public static Function<UriBuilder, URI> buildMultipleEntityRetrievalUrl(String wsKey) {
        return uriBuilder -> {
            UriBuilder builder =
                    uriBuilder
                            .path(PATH_SEPERATOR + MULTIPLE_ENTITY_RETRIEVAL_PATH)
                            .queryParam(WSKEY, wsKey);
            return builder.build();
        };
    }

    /**
     * Builds the Entity retrieval url from entityId
     * id :  http://data.europeana.eu/<type>/<id>
     * builds :/<type>/<id>
     * @param id
     * @return
     */
    public static String getEntityRetrievalId(String id) {
        return  "/" + EntityRecordUtils.getEntityRequestPath(id);
    }

    /**
     * Returns List of ids from suggest results
     * @param json
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEntityTypeException
     */
    public static List<String> getEntityApiResults(String json) throws JsonProcessingException {
        List<String> entities = new ArrayList<>();
        if (!StringUtils.isEmpty(json)) {
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            int total = node.has(TOTAL) ? Integer.parseInt(String.valueOf(node.get(TOTAL))) : 0;
            if (total > 0) {
                if (node.has(ITEMS_FIELD)) {
                    Iterator<JsonNode> iterator = node.get(ITEMS_FIELD).iterator();
                    while (iterator.hasNext()) {
                        entities.add(String.valueOf(iterator.next().get(ID)));
                    }
                }
                // fail-safe check
                if (total != entities.size()) {
                    LOGGER.debug("Mismatch while parsing the suggest results. Entities in suggest Results = {}, Entities collected={}",
                            total, entities.size());
                }
            }
        }
        return entities;
    }
}
