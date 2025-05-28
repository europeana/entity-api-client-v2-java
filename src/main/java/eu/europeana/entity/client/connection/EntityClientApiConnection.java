package eu.europeana.entity.client.connection;

import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entity.client.model.EntityRetrievalResponse;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ProtocolException;


import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Entity Client Api Connection class
 * @author srishti singh
 */
public class EntityClientApiConnection extends BaseApiConnection {

   public EntityClientApiConnection(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth) {
       super(entityApiUri, entityManagementApiUri, auth);
   }

    /**
     * Retrieve suggestion (list of entity ids) for the text provided
     * @param text text for entity suggest
     * @param language language
     * @param scope scope of the suggest
     * @param type type of entity
     * @param rows number of rows
     * @param algorithm algorithm for entity suggest
     * @return list of entity ids found
     * @throws EntityClientException throws if there is an error in post request or reading the response
     */
    public List<String> retrieveSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws EntityClientException {
        try {
            URI suggestUrl = EntityClientUtils.buildSuggestUrl(entityApiUri + PATH_SEPERATOR + SUGGEST_PATH, text, language, scope, type, rows, algorithm);
            SimpleHttpResponse response = entityApiConnection.get(suggestUrl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getCode() == HttpStatus.SC_OK) {
                List<String> entities = EntityClientUtils.getEntityApiResults(response.getBodyText());
                if (entities != null) {
                    LOGGER.debug("{} entities found for suggest text={}, lang={}, type={}", entities.size(), text, language, type);
                    return entities;
                }
            }
        } catch (ExecutionException | InterruptedException | IOException e) {
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
     * @param uri uri to be resolved
     * @return list of entity ids found
     * @throws EntityClientException throws if there is an error in post request or reading the response
     */
    public List<String> resolveEntity(String uri) throws EntityClientException {
        try {
            URI resolveurl = EntityClientUtils.buildEntityResolveUrl(entityApiUri + PATH_SEPERATOR + RESOLVE_PATH, uri);
            SimpleHttpResponse response = entityApiConnection.get(resolveurl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            // check if the status is 301 and body is empty , fetch the location header
            if (response.getCode() == HttpStatus.SC_MOVED_PERMANENTLY && StringUtils.isEmpty(response.getBodyText())) {
                String entityId = response.getHeader(HttpHeaders.LOCATION).getValue();
                if (StringUtils.isNotEmpty(entityId)) {
                    LOGGER.debug("entity :  {} , found for uri={} ", entityId, uri);
                    return Collections.singletonList("\"" + entityId + "\"");
                }
            }
        } catch (ExecutionException | InterruptedException | ProtocolException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        LOGGER.debug("No entity found for resolve uri={}", uri);
        return Collections.emptyList();
    }


    /**
     * Entity Enrichment retrieval
     * @param text text for entity enrich
     * @param language language for entity enrich
     * @param type type of entity
     * @param rows rows
     * @return lis of entity ids
     * @throws EntityClientException throws if there is an error in post request or reading the response
     */
    public List<String> retrieveEnrichment(String text, String language, String type, String rows) throws EntityClientException{
        try {
            URI enrichUrl = EntityClientUtils.buildEntityEnrichUrl(entityApiUri + PATH_SEPERATOR + ENRICH_PATH, text, language, type, rows);
            SimpleHttpResponse response = entityApiConnection.get(enrichUrl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getCode() == HttpStatus.SC_OK) {
                List<String> entities = EntityClientUtils.getEntityApiResults(response.getBodyText());
                if (entities != null) {
                    LOGGER.debug("{} entities found for enrich text={}, lang={}, type={}", entities.size(), text, language, type);
                    return entities;
                }
            }
        } catch (ExecutionException | InterruptedException | IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        LOGGER.debug("No entity found for enrich text={}, lang={}, type={}", text, language, type);
        return Collections.emptyList();
    }

    /**
     * Returns the Entity matching the entity id
     * This method executes the entity Retrieval method of EM.
     * @param entityId entity id to be fetched
     * @return Entity for the given id
     * @throws EntityClientException throws if there is an error in post request or reading the response
     */
    public Entity getEntityById(String entityId) throws  EntityClientException {
        try {
            URI entityUrl = EntityClientUtils.buildEntityRetrievalUrl(entityManagementApiUri  + EntityClientUtils.getEntityRetrievalId(entityId));
            SimpleHttpResponse response = entityManagementConnection.get(entityUrl.toString(), ContentType.APPLICATION_JSON.getMimeType(), this.auth);
            if (response.getCode() == HttpStatus.SC_OK) {
                return mapper.readValue(response.getBodyText(), Entity.class);
            }
        } catch (ExecutionException | InterruptedException | IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        return null;
    }

    /**
     * Returns the Entities matching the entity ids.
     * This method executes the Multiple entity Retrieval method of EM.
     * @param entityIds lis of entity ids to be fetched
     * @return list of entities
     * @throws EntityClientException throws if there is an error in post request or reading the response
     */
    public List<Entity> getEntityByIds(List<String> entityIds) throws EntityClientException {
        try {
            URI entityUrl = EntityClientUtils.buildEntityRetrievalUrl(
                    entityManagementApiUri + PATH_SEPERATOR + MULTIPLE_ENTITY_RETRIEVAL_PATH);
            SimpleHttpResponse response = entityManagementConnection.post(entityUrl.toString(),
                    entityIds.toString(),
                    ContentType.APPLICATION_JSON.getMimeType(),
                    this.auth);
            if (response.getCode() == HttpStatus.SC_OK) {
                return mapper.readValue(response.getBodyText(), EntityRetrievalResponse.class).getItems();
            }
        } catch (ExecutionException | InterruptedException | IOException e) {
            throw new EntityClientException(ERROR_MESSAGE + e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }
        return Collections.emptyList();
    }
}
