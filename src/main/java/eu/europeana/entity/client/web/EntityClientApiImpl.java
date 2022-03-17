package eu.europeana.entity.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.BaseEntityApiClient;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityClientApiImpl extends BaseEntityApiClient implements EntityClientApi {

    public EntityClientApiImpl() {}

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
            for (String entityId : results) {
                entities.add(getEntityManagementRestClient().getEntityById(entityId));
            }
            // fail-safe check
            if (entities.size() != results.size()) {
                //This should never happen, But just to be sure.
                throw new TechnicalRuntimeException("Mismatch between entity Api v2 entities =" + results.size() + "  and EM entities=" + entities.size());
            }
        }
        return entities;
    }
}
