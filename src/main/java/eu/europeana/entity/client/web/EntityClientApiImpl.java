package eu.europeana.entity.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.BaseEntityApiClient;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClientApiImpl extends BaseEntityApiClient implements EntityClientApi {

    public EntityClientApiImpl() {
      this( new EntityClientConfiguration());
    }
    

    public EntityClientApiImpl(EntityClientConfiguration configuration) {
        super(configuration);
    }

    @Override
    public List<Entity> getSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws JsonProcessingException, TechnicalRuntimeException {
        List<String> suggestResults = getEntityApiRestClient().retrieveSuggestions(text, language, scope, type, rows, algorithm);
        return getMetadata(suggestResults);
    }

    @Override
    public List<Entity> getEnrichment(String text, String lang, String type, String rows) throws JsonProcessingException {
        List<String> enrichResults = getEntityApiRestClient().retrieveEnrichment(text, lang, type, rows);
        return getMetadata(enrichResults);
    }

    @Override
    public Entity getEntityById(String entityId) {
        return getEntityManagementRestClient().getEntityById(entityId);
    }

    @Override
    public List<Entity> getEntityByUri(String uri) throws TechnicalRuntimeException {
        List<String> resolveResults = getEntityApiRestClient().retrieveEntityByUri(uri);
        return getMetadata(resolveResults);
    }

    private List<Entity> getMetadata(List<String> results) throws TechnicalRuntimeException {
        List<Entity> entities = new ArrayList<>();
        if (!results.isEmpty()) {
            // if only one entity, execute single entity retrieval method. Otherwise, multiple entity retrieval
            if (results.size() == 1) {
                entities.add(getEntityManagementRestClient().getEntityById(StringUtils.remove(results.get(0), "\"")));
            } else {
                entities = getEntityManagementRestClient().getEntityByIds(results);
            }
            // fail-safe check
            if (entities.size() != results.size()) {
                List<String> retrievedEMEntities = entities.stream().map(Entity::getEntityId).collect(Collectors.toList());
                //This should never happen, But just to be sure.
                throw new TechnicalRuntimeException("Mismatch between entity Api v2 entities =" + results + "  and EM entities=" + retrievedEMEntities);
            }
        }
        return entities;
    }
}
