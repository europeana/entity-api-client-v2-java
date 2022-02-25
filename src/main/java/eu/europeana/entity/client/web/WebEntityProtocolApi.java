package eu.europeana.entity.client.web;

import eu.europeana.api.commons.error.EuropeanaApiException;
import eu.europeana.entity.client.exception.AuthenticationException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.codehaus.jettison.json.JSONException;

import java.util.List;

public interface WebEntityProtocolApi {

    /**
     * This method returns entity suggestions depending on given text, algorithm, types, scope and language.
     * @param text
     * @param language
     * @param scope
     * @param type
     * @param rows
     * @param algorithm
     */
    public List<Entity> getSuggestions(String text, String language, String scope, String type, String rows, String algorithm) throws JSONException, EuropeanaApiException, UnsupportedEntityTypeException;

    /**
     * Get Entity by EntityId
     * @param entityId
     * @return
     * @throws UnsupportedEntityTypeException
     */
    public Entity getEntityById(String entityId) throws UnsupportedEntityTypeException, AuthenticationException;

    /**
     * Get Entity by Uri
     * @param uri
     * @throws JSONException
     * @throws EuropeanaApiException
     * @throws UnsupportedEntityTypeException
     */
    public List<Entity> getEntityByUri(String uri) throws JSONException, EuropeanaApiException, UnsupportedEntityTypeException;
}
