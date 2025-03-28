package eu.europeana.entity.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;
// get rename -  getentity , resolveentity, suggest entity, enrichEntity
public interface EntityClientApi {

    /**
     * This method returns entity suggestions depending on given text, algorithm, types, scope and language.
     * @param text
     * @param language
     * @param scope
     * @param type
     * @param rows
     * @param algorithm
     */
    public List<Entity> suggestEntity(String text, String language, String scope, String type, String rows, String algorithm) throws JsonProcessingException;

    /**
     * This method returns entity enrichment depending on given text, types and language.
     * @param text
     * @param lang
     * @param type
     * @param rows
     */
    public List<Entity> enrichEntity(String text, String lang, String type, String rows) throws JsonProcessingException;

    /**
     * Get Entity by EntityId
     * @param entityId
     * @return
     * @throws UnsupportedEntityTypeException
     */
    public Entity getEntity(String entityId);

    /**
     * Get Entity by Uri
     * @param uri
     * @throws UnsupportedEntityTypeException
     */
    public List<Entity> resolveEntity(String uri) throws TechnicalRuntimeException;
}
