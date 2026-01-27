package eu.europeana.entity.client;

import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.entity.client.config.ClientConnectionConfig;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entity.client.web.EntityApi;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity Api Client
 *
 * @author srishti singh
 */
public class EntityApiClient extends BaseEntityApiClient implements EntityApi {

    /**
     * Creates a EntityApiClient from the entity client configuration (via properties)
     * If client connection properties are present - creates a custom client
     *       else creates client with defaults
     *
     * @param configuration entity client configuration
     * @throws EntityClientException
     */
    public EntityApiClient(EntityClientConfiguration configuration) throws EntityClientException {
        super(configuration);
    }

    /**
     * Creates EntityApiClient connection with defaults
     * @param entityApiUri
     * @param entityManagementApiUri
     * @throws EntityClientException
     */
    public EntityApiClient(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth)
            throws EntityClientException {
        super(entityApiUri, entityManagementApiUri, auth);
    }

    /**
     * Creates a EntityApiClient
     *    custom : if ClientConnectionConfig values are present will create a custom client with Request Config (timeout, keep alive etc values),
     *             IOReactorConfig for socket timeouts etc and connection manager to handle the connection
     *    default : if ClientConnectionConfig is null or empty
     * @param entityApiUri entity api url
     * @param entityManagementApiUri  entity management url
     * @param connectionConfig client connection manager for the client
     * @throws EntityClientException
     */
    public EntityApiClient(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth, ClientConnectionConfig connectionConfig)
            throws EntityClientException {
        super(entityApiUri, entityManagementApiUri, auth, connectionConfig);
    }

    /**
     * Creates a EntityApiClient
     *    custom : if configuration has connection propeties OR ClientConnectionConfig values are present will create a custom client
     *    default : if both configuration doesn't have connection properties and ClientConnectionConfig is null or empty
     * @param configuration entity client configuration
     * @param connectionConfig client connection manager for the client
     * @throws EntityClientException
     */
    public EntityApiClient(EntityClientConfiguration configuration, ClientConnectionConfig connectionConfig)
            throws EntityClientException {
        super(configuration, connectionConfig);
    }

    @Override
    public List<Entity> suggestEntity(String text, String language, String scope, String type, String rows, String algorithm)
            throws EntityClientException {
        List<String> suggestResults = getEntityClientApiConnection().retrieveSuggestions(text, language, scope, type, rows, algorithm);
        return getMetadata(suggestResults);
    }

    @Override
    public List<Entity> enrichEntity(String text, String lang, String type, String rows) throws EntityClientException {
        List<String> enrichResults = getEntityClientApiConnection().retrieveEnrichment(text, lang, type, rows);
        return getMetadata(enrichResults);
    }

    @Override
    public Entity getEntity(String entityId) throws EntityClientException {
        return getEntityClientApiConnection().getEntityById(entityId);
    }

    @Override
    public List<Entity> resolveEntity(String uri) throws EntityClientException {
        List<String> resolveResults = getEntityClientApiConnection().resolveEntity(uri);
        return getMetadata(resolveResults);
    }

    /**
     * This methods fetches the Entities from EM for the list of entity ids
     * @param results list of entity ids
     * @return List of Entities
     * @throws EntityClientException throws if there is a mismatch b/w entities
     */
    public List<Entity> getMetadata(List<String> results) throws EntityClientException {
        List<Entity> entities = new ArrayList<>();
        if (!results.isEmpty()) {
            // if only one entity, execute single entity retrieval method. Otherwise, multiple entity retrieval
            if (results.size() == 1) {
                entities.add(getEntityClientApiConnection().getEntityById(StringUtils.remove(results.get(0), "\"")));
            } else {
                entities = getEntityClientApiConnection().getEntityByIds(results);
            }
            // fail-safe check
            if (entities.size() != results.size()) {
                List<String> retrievedEMEntities = entities.stream().map(Entity::getEntityId).toList();
                //This should never happen, But just to be sure.
                throw new EntityClientException("Mismatch between entity Api v2 entities =" + results + "  and EM entities=" + retrievedEMEntities);
            }
        }
        return entities;
    }
}
