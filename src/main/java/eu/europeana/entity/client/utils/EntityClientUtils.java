package eu.europeana.entity.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.*;
import eu.europeana.entitymanagement.utils.EntityRecordUtils;
import eu.europeana.entitymanagement.vocabulary.EntityTypes;
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
     * Builds the Entity management retrieval url
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
     * Builds the Entity retrieval url from entityId
     * id : http://data.europeana.eu/<type>/<id>
     * builds : /<type>/base/<id>
     * @param id
     * @return
     */
    public static String getEntityRetrievalId(String id) {
        if(StringUtils.contains(id, ENTITY_ID_BASE)) {
            return StringUtils.substringAfter(id, BASE_URL);
        } else {
            return  "/" + EntityRecordUtils.getEntityRequestPathWithBase(id);
        }
    }

    /**
     * Returns List of entities with id and type fields from the suggest results
     * @param json
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEntityTypeException
     */
    public static List<String> getSuggestResults(String json) throws JsonProcessingException {
        List<String> entities = new ArrayList<>();
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        int total = node.has(TOTAL) ? Integer.parseInt(String.valueOf(node.get(TOTAL))) : 0;
        if (total > 0) {
            if (node.has(ITEMS_FIELD)) {
                Iterator<JsonNode> iterator = node.get(ITEMS_FIELD).iterator();
                while (iterator.hasNext()) {
                    entities.add(StringUtils.remove(String.valueOf(iterator.next().get(ID)), '\"'));
                }
            }
            // fail-safe check
            if (total != entities.size()) {
                LOGGER.debug("Mismatch while parsing the suggest results. Entities in suggest Results = {}, Entities collected={}",
                        total, entities.size());
            }
        }
        return entities;
    }


    /**
     * Returns the Entity class from an entityId
     * @param entityId
     * @return
     * @throws UnsupportedEntityTypeException
     */
    public static Entity getEntityClassById(String entityId) throws UnsupportedEntityTypeException {
       return EntityClientUtils.getEntityClass(EntityTypes.getByEntityId(entityId));
    }

    /**
     * Returns Entity class from an EntityTypes
     * @param type
     * @return
     */
    private static Entity getEntityClass(EntityTypes type) {
        Entity entity;
        switch (type) {
            case Agent:
                entity = new Agent();
            break;
            case Place:
                entity = new Place();
            break;
            case Concept:
                entity = new Concept();
            break;
            case TimeSpan:
                entity = new TimeSpan();
            break;
            case Organization:
                entity = new Organization();
            break;
            default:
                entity = null;
            break;
        }
        return entity;
    }
}
