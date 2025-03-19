package eu.europeana.entity.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
     * @return
     */
    public static URI buildSuggestUrl(String url, String text, String language, String scope, String type, String rows, String algorithm) throws EntityClientException {
        try {
            URIBuilder builder = new URIBuilder(url)
                    .addParameter(TEXT, text);
            if (language != null) {
                builder.addParameter(LANGUAGE, language);
            }
            if (scope != null) {
                builder.addParameter(SCOPE, scope);
            }
            if (type != null) {
                builder.addParameter(TYPE, type);
            }
            if (rows != null) {
                builder.addParameter(ROWS, rows);
            }
            if (algorithm != null) {
                builder.addParameter(ALGORITHM, algorithm);
            }
            return builder.build();
        } catch (URISyntaxException e) {
            throw  new EntityClientException("Error creating resolve Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    public static URI buildEntityEnrichUrl(String url, String text, String lang, String type, String rows) throws EntityClientException {
        try {
            URIBuilder builder = new URIBuilder(url)
                    .addParameter(TEXT, text);
            if (lang != null) {
                builder.addParameter(LANG, lang);
            }
            if (type != null) {
                builder.addParameter(TYPE, type);
            }
            if (rows != null) {
                builder.addParameter(ROWS, rows);
            }
            return builder.build();
        } catch (URISyntaxException e) {
            throw  new EntityClientException("Error creating resolve Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }


    /**
     * Builds the Entity Api resolve url
     * @param enityUrl
     * @param resolveUri
     * @return
     */
    public static URI buildEntityResolveUrl(String enityUrl, String resolveUri) throws EntityClientException {
        try {
            URIBuilder builder = new URIBuilder(enityUrl)
                    .addParameter(URI, resolveUri);
            return builder.build();
        } catch (URISyntaxException e) {
            throw  new EntityClientException("Error creating resolve Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Builds the Entity management entity retrieval url
     * @param path
     */
    public static URI buildEntityRetrievalUrl(String path) throws EntityClientException {
        try {
            URIBuilder builder = new URIBuilder(path);
            return builder.build();
        } catch (URISyntaxException e) {
            throw  new EntityClientException("Error creating resolve Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Builds the Entity management multiple entity retrieval url
     * @param path
     * @return
     */
    public static URI buildMultipleEntityRetrievalUrl(String path) throws EntityClientException {
        try {
            URIBuilder builder = new URIBuilder(path);
            return builder.build();
        } catch (URISyntaxException e) {
            throw  new EntityClientException("Error creating resolve Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Builds the Entity retrieval url from entityId
     * id : http://data.europeana.eu/<type>/<id>
     * extracts : /<type>/<id>
     * @param id
     * @return
     */
    public static String getEntityRetrievalId(String id) {
       return StringUtils.substringAfter(id, BASE_URL);
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
