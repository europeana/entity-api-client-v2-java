package eu.europeana.entity.client.web;

import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;

public interface EntityApi {

    /**
     * This method returns entity suggestions depending on given text, algorithm, types, scope and language.
     * @param text
     * @param language
     * @param scope
     * @param type
     * @param rows
     * @param algorithm
     */
    public List<Entity> suggestEntity(String text, String language, String scope, String type, String rows, String algorithm) throws EntityClientException;

    /**
     * This method returns entity enrichment depending on given text, types and language.
     * @param text
     * @param lang
     * @param type
     * @param rows
     */
    public List<Entity> enrichEntity(String text, String lang, String type, String rows) throws EntityClientException;

    /**
     * Get Entity by EntityId
     * @param entityId
     * @return
     * @throws UnsupportedEntityTypeException
     */
    public Entity getEntity(String entityId) throws EntityClientException;

    /**
     * Get Entity by Uri
     * @param uri
     * @throws UnsupportedEntityTypeException
     */
    public List<Entity> resolveEntity(String uri) throws EntityClientException;
}
