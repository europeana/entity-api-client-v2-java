package eu.europeana.entity.client;

import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entity.client.web.EntityApi;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity Api Client
 *
 * @author srishti singh
 */
public class EntityApiClient extends BaseEntityApiClient implements EntityApi {

    public EntityApiClient(EntityClientConfiguration configuration) throws EntityClientException {
        super(configuration);
    }

    public EntityApiClient(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth)
            throws EntityClientException {
        super(entityApiUri, entityManagementApiUri, auth);
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
