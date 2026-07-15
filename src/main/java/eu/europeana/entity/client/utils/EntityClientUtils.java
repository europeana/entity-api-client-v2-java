package eu.europeana.entity.client.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europeana.api.commons_sb3.definitions.search.enrich.EnrichQuery;
import eu.europeana.api.commons_sb3.definitions.search.enrich.EnrichRequest;
import eu.europeana.entity.client.exception.EntityClientException;
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
import java.util.Map;

import static eu.europeana.entity.client.utils.EntityApiConstants.*;

/**
 * Entity client utils class
 */
public class EntityClientUtils {

    private static final Logger LOGGER = LogManager.getLogger(EntityClientUtils.class);
    private static final ObjectMapper mapper = buildMapper();

    private EntityClientUtils() {
        // to hide implicit one
    }


    /**
     * Constructs a URI for the suggestion API with the specified query parameters.
     *
     * @param url the base URL for the suggestion API
     * @param text the input text to generate suggestions for
     * @param language the language of the input text; can be null
     * @param scope the scope of the suggestions; can be null
     * @param type the type of suggestions to retrieve; can be null
     * @param rows the maximum number of suggestion rows to retrieve; can be null
     * @param algorithm the algorithm to use for generating suggestions; can be null
     * @return a URI representing the suggestion API request with the specified parameters
     * @throws EntityClientException if there is an error during URI construction
     */
    public static URI buildSuggestUrl(String url, String text, String language, String scope,
                                      String type, String rows, String algorithm) throws EntityClientException {
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
            throw  new EntityClientException("Error creating suggest Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Builds the Entity Enrich API URL using the provided parameters.
     *
     * @param url the base URL for the Entity Enrich API
     * @param text the text to be enriched
     * @param lang the language of the text; can be null
     * @param type the type of enrichment; can be null
     * @param rows the number of rows to retrieve; can be null
     * @return a URI object representing the constructed Enrich API URL
     * @throws EntityClientException if there is an error creating the URI
     */
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
            throw  new EntityClientException("Error creating enrich Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Constructs a JSON string for an enrichment request based on the given parameters.
     *
     * @param type the type of entity for the enrichment request
     * @param textLangMap a map where keys are texts to be enriched and values are their respective language codes
     * @param rows the number of rows to be included in the response
     * @return a JSON string representation of the enrichment request
     * @throws EntityClientException if the input map is null, empty, or if JSON processing fails
     */
    public static String buildEnrichRequest(String type, Map<String, String> textLangMap, int rows) throws EntityClientException {
        if (textLangMap == null || textLangMap.isEmpty()) {
            throw new EntityClientException("No values provided for enrichment request");
        }
        try {
            List<EnrichQuery> query = new ArrayList<>(textLangMap.size());
            for (Map.Entry<String, String> entry : textLangMap.entrySet()) {
                query.add(new EnrichQuery(entry.getKey(), entry.getValue()));
            }
            return mapper.writeValueAsString(new EnrichRequest(type, query, rows));
        } catch (JsonProcessingException e) {
            throw new EntityClientException("Error creating enrich request " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }


    /**
     * Constructs a URI for resolving an entity using the provided base URL and resolve URI parameter.
     *
     * @param enityUrl the base URL for the entity resolve endpoint
     * @param resolveUri the URI parameter to resolve the entity
     * @return a URI object representing the constructed resolve URL
     * @throws EntityClientException if there is an error during URI construction
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
     * Builds a URI for entity retrieval based on the given path.
     *
     * @param path the input string representing the path to be used for building the URI
     * @return a URI object constructed from the given path
     * @throws EntityClientException if there is an error during URI construction, such as an invalid path
     */
    public static URI buildEntityRetrievalUrl(String path) throws EntityClientException {
        try {
            URIBuilder builder = new URIBuilder(path);
            return builder.build();
        } catch (URISyntaxException e) {
            throw  new EntityClientException("Error creating Entity retrieval Urls " +e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Extracts the entity retrieval ID from the given ID by removing the base URL prefix.
     * id : http://data.europeana.eu/<type>/<id>
     * extracts : /<type>/<id>
     * @param id the full identifier string containing the base URL and the entity-specific ID
     * @return the entity retrieval ID, which is the portion of the input ID after the base URL
     */
    public static String getEntityRetrievalId(String id) {
       return StringUtils.substringAfter(id, BASE_URL);
    }

    /**
     * Parses the provided JSON string to extract entity IDs from the API results.
     *
     * @param json the JSON string representing the API response, which contains entity data and metadata
     * @return a list of entity IDs extracted from the JSON response
     * @throws JsonProcessingException if an error occurs while parsing the JSON string
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
                if (total != entities.size() && LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Mismatch while parsing the suggest results. Entities in suggest Results = {}, Entities collected={}",
                            total, entities.size());
                }
            }
        }
        return entities;
    }

    /**
     * Extracts the error message from a JSON response string.
     *
     * @param response the JSON string response, which is expected to contain an error message
     *                 under the key "message"
     * @return the extracted error message as a string; returns an empty string if parsing fails
     *         or if the "message" key is not found
     */
    public static String getErrorMessage(String response) {
        try {
            return mapper.readTree(response).get("message").asText();
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing the response", e);
        }
        return "";
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.findAndRegisterModules();
        return mapper;
    }
}
