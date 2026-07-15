package eu.europeana.entity.client.web;

import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;
import java.util.Map;

/**
 * Entity client api interface
 */
public interface EntityApi {

    /**
     * Suggests a list of entities based on the provided parameters.
     * This method processes the input text, language, scope, type,
     * number of rows, and algorithm to generate entity suggestions.
     *
     * @param text the input text to analyze for suggesting entities
     * @param language the language of the input text, represented as a language code (e.g., "en", "es")
     * @param scope the contextual scope for entity suggestions (e.g., "global", "local")
     * @param type the type of entities to suggest (e.g., "agent", "organization", "place")
     * @param rows the maximum number of suggested entities to retrieve
     * @param algorithm the algorithm to use for generating entity suggestions
     * @return a list of suggested {@code Entity} objects based on the input parameters
     * @throws EntityClientException if an error occurs during the suggestion process, such as invalid input
     *                               or issues with the remote service
     */
    List<Entity> suggestEntity(String text, String language, String scope, String type, String rows, String algorithm) throws EntityClientException;

    /**
     * Enriches entities based on the provided text, language, type, and the number of rows.
     * This method processes the given inputs to retrieve and enrich a list of entities.
     *
     * @param text the text input to be analyzed and enriched
     * @param lang the language of the input text, represented as a language code (e.g., "en", "fr")
     * @param type the type of entities to enrich (e.g., "agent", "organization", "place")
     * @param rows the maximum number of rows or entities to retrieve and process
     * @return a list of enriched {@code Entity} objects
     * @throws EntityClientException if an error occurs during the enrichment process, such as invalid input
     *                               or issues with the remote service
     */
    List<Entity> enrichEntity(String text, String lang, String type, String rows) throws EntityClientException;

    /**
     * This method enriches entities based on the provided type, a mapping of text to languages,
     * and the number of rows to process. This operation uses the provided parameters to fetch and
     * enrich entity data.
     *
     * @param type the type of entities to be enriched (e.g., "agent", "place", "organization")
     * @param textLangMap a map where the key is the text to be enriched and the value is the associated language
     * @param rows the maximum number of rows/entities to retrieve and process
     * @return a list of enriched {@code Entity} objects based on the provided inputs
     * @throws EntityClientException if an error occurs during the enrichment process, such as invalid input
     *                               or issues with the remote service
     */
     List<Entity> enrichEntity(String type, Map<String, String> textLangMap, int rows) throws EntityClientException;

    /**
     * Retrieves an entity based on the provided entity ID.
     * This method fetches the detailed information for the entity
     * corresponding to the specified identifier.
     *
     * @param entityId the unique identifier of the entity to be retrieved
     * @return the {@code Entity} object containing the details of the requested entity
     * @throws EntityClientException if an error occurs during the fetching process, such as network issues
     *                               or failures in parsing the service response
     */
    public Entity getEntity(String entityId) throws EntityClientException;


    /**
     * Resolves an entity based on the provided URI. This method fetches the entity information
     * corresponding to the given URI and returns a list of matching entities.
     *
     * @param uri the unique resource identifier for the entity to be resolved
     * @return a list of entities matching the provided URI
     * @throws EntityClientException if an error occurs during the resolution process, such as issues
     *                               with the remote service or parsing the response
     */
    List<Entity> resolveEntity(String uri) throws EntityClientException;
}
