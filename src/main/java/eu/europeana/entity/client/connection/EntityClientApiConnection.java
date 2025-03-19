package eu.europeana.entity.client.connection;

import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.api.commons_sb3.http.HttpResponseHandler;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entity.client.model.EntityRetrievalResponse;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;


import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;


public class EntityClientApiConnection extends BaseApiConnection {

   public EntityClientApiConnection(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth) {
       super(entityApiUri, entityManagementApiUri, auth);
   }

    public List<String> retrieveSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws EntityClientException {
        try {
            URI suggestUrl = EntityClientUtils.buildSuggestUrl(entityApiUri + PATH_SEPERATOR + SUGGEST_PATH, text, language, scope, type, rows, algorithm);
            HttpResponseHandler response = entityApiConnection.get(suggestUrl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getStatus() == HttpStatus.SC_OK) {
                List<String> entities = EntityClientUtils.getEntityApiResults(response.getResponse());
                if (entities != null) {
                    LOGGER.debug("{} entities found for suggest text={}, lang={}, type={}", entities.size(), text, language, type);
                    return entities;
                }
            }
        } catch (IOException e) {
           throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        LOGGER.debug("No entity found for suggest text={}, lang={}, type={}", text, language, type);
        return Collections.emptyList();
    }

    /**
     * Returns the value present in location response header,
     * if the endpoint returns 301 (Moved Permanently).
     * Otherwise, if endpoint returns,
     *   300 (Multiple Choices) for a uri ie; more than one entity for one uri,
     *   empty list will be returned
     *
     * @param uri
     * @return
     * @throws EntityClientException
     */
    public List<String> resolveEntity(String uri) throws EntityClientException {
        try {
            java.net.URI resolveurl = EntityClientUtils.buildEntityResolveUrl(entityApiUri + PATH_SEPERATOR + RESOLVE_PATH, uri);
            HttpResponseHandler response = entityApiConnection.get(resolveurl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            // check if the status is 301 and body is empty , fetch the location header
            if (response.getStatus() == HttpStatus.SC_MOVED_PERMANENTLY && StringUtils.isEmpty(response.getResponse())) {
                String entityId = response.getLocationHeader();
                if (StringUtils.isNotEmpty(entityId)) {
                    LOGGER.debug("entity :  {} , found for uri={} ", entityId, uri);
                    return Collections.singletonList("\"" + entityId + "\"");
                }
            }
        } catch (IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        LOGGER.debug("No entity found for resolve uri={}", uri);
        return Collections.emptyList();
    }


    public List<String> retrieveEnrichment(String text, String language, String type, String rows) throws EntityClientException{
        try {
            java.net.URI enrichUrl = EntityClientUtils.buildEntityEnrichUrl(entityApiUri + PATH_SEPERATOR + ENRICH_PATH, text, language, type, rows);
            HttpResponseHandler response = entityApiConnection.get(enrichUrl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getStatus() == HttpStatus.SC_OK) {
                List<String> entities = EntityClientUtils.getEntityApiResults(response.getResponse());
                if (entities != null) {
                    LOGGER.debug("{} entities found for enrich text={}, lang={}, type={}", entities.size(), text, language, type);
                    return entities;
                }
            }
        } catch (IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        LOGGER.debug("No entity found for enrich text={}, lang={}, type={}", text, language, type);
        return Collections.emptyList();
    }

    /**
     * Returns the Entity matching the entity id
     * This method executes the entity Retrieval method of EM.
     * @param entityId
     * @return
     * @throws EntityClientException
     */
    public Entity getEntityById(String entityId) throws  EntityClientException {
        try {
            java.net.URI entityUrl = EntityClientUtils.buildEntityRetrievalUrl(entityManagementApiUri  + EntityClientUtils.getEntityRetrievalId(entityId));
            HttpResponseHandler response = entityManagementConnection.get(entityUrl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getStatus() == HttpStatus.SC_OK) {
                return mapper.readValue(response.getResponse(), Entity.class);
            }
        } catch (IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        return null;
    }

    /**
     * Returns the Entities matching the entity ids.
     * This method executes the Multiple entity Retrieval method of EM.
     * @param entityIds
     * @return
     * @throws EntityClientException
     */
    public List<Entity> getEntityByIds(List<String> entityIds) throws EntityClientException {
        try {
            java.net.URI entityUrl = EntityClientUtils.buildMultipleEntityRetrievalUrl(entityManagementApiUri + PATH_SEPERATOR + MULTIPLE_ENTITY_RETRIEVAL_PATH);
            HttpResponseHandler response = entityManagementConnection.post(entityUrl.toString(), entityIds.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getStatus() == HttpStatus.SC_OK) {
                return mapper.readValue(response.getResponse(), EntityRetrievalResponse.class).getItems();
            }
        } catch (IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        return null;
    }
}
