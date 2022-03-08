package eu.europeana.entity.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;

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
    public List<Entity> getSuggestions(String text, String language, String scope, String type, String rows, String algorithm) throws UnsupportedEntityTypeException, JsonProcessingException;

    /**
     * Get Entity by EntityId
     * @param entityId
     * @return
     * @throws UnsupportedEntityTypeException
     */
    public Entity getEntityById(String entityId) throws UnsupportedEntityTypeException;

    /**
     * Get Entity by Uri
     * @param uri
     * @throws UnsupportedEntityTypeException
     */
    public List<Entity> getEntityByUri(String uri) throws TechnicalRuntimeException, UnsupportedEntityTypeException;
}
