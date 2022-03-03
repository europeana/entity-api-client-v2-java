package eu.europeana.entity.client.utils;

import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.*;
import eu.europeana.entitymanagement.utils.EntityRecordUtils;
import eu.europeana.entitymanagement.vocabulary.EntityTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityClientUtils {

    private static final Logger LOGGER = LogManager.getLogger(EntityClientUtils.class);

    public static final String BASE_URL = "http://data.europeana.eu";
    public static final String ENTITY_ID_BASE = "base";

    public static final String HEADER_LOCATION = "location";

    public static final String SUGGEST_PATH = "suggest";
    public static final String RESOLVE_PATH = "resolve";
    public static final String PATH_SEPERATOR = "/";

    public static final String TEXT = "text";
    public static final String WSKEY = "wskey";
    public static final String LANGUAGE = "language";
    public static final String SCOPE = "scope";
    public static final String ROWS = "rows";
    public static final String ALGORITHM = "algorithm";
    public static final String URI = "uri";

    public static final String TOTAL = "total";
    public static final String ITEMS_FIELD = "items";
    public static final String ID = "id";
    public static final String TYPE = "type";

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
     * @param jsonObject
     * @return
     * @throws JSONException
     * @throws UnsupportedEntityTypeException
     */
    public static List<String> getSuggestResults(JSONObject jsonObject) throws JSONException{
        List<String> entities = new ArrayList<>();
        JSONArray items = jsonObject.getJSONArray(ITEMS_FIELD);
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = (JSONObject) items.get(i);
            if (item.has(TYPE) && item.has(ID)) {
                entities.add(String.valueOf(item.get(ID)));
            }
        }
        // fail-safe check
        if(getTotalValue(jsonObject) != entities.size()) {
         LOGGER.error("Mismatch while parsing the suggest results. Entities in suggest Results = {}, Entities collected={}",
                 getTotalValue(jsonObject), entities.size());
        }
        return entities;
    }

    /**
     *  Returns the total value from the jsonObject
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static int getTotalValue(JSONObject jsonObject) throws JSONException {
        if(jsonObject.has(TOTAL)) {
            return Integer.parseInt(String.valueOf(jsonObject.get(TOTAL)));
        }
        return 0;
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
