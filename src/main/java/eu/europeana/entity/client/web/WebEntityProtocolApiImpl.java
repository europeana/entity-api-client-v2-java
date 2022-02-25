package eu.europeana.entity.client.web;

import eu.europeana.api.commons.error.EuropeanaApiException;
import eu.europeana.entity.client.BaseEntityApiClient;
import eu.europeana.entity.client.exception.AuthenticationException;
import eu.europeana.entity.client.exception.EntityMismatchException;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.codehaus.jettison.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class WebEntityProtocolApiImpl extends BaseEntityApiClient implements WebEntityProtocolApi {

    @Override
    public List<Entity> getSuggestions(String text, String language, String scope, String type, String rows, String algorithm)
            throws JSONException, EuropeanaApiException, UnsupportedEntityTypeException {
        List<Entity> suggestResults = getEntityApiRestService().retrieveSuggestions(text, language, scope, type, rows, algorithm);
        return getMetadata(suggestResults);
    }

    @Override
    public Entity getEntityById(String entityId) throws UnsupportedEntityTypeException, AuthenticationException {
        return getEntityManagementRestService().getEntityById(entityId);
    }

    @Override
    public List<Entity> getEntityByUri(String uri) throws JSONException, EuropeanaApiException, UnsupportedEntityTypeException {
        List<Entity> resolveResults = getEntityApiRestService().retrieveEntityByUri(uri);
        return getMetadata(resolveResults);
    }

    private List<Entity> getMetadata(List<Entity> results) throws AuthenticationException, EntityMismatchException {
        List<Entity> entities = new ArrayList<>();
        if (!results.isEmpty()) {
            for (Entity entity : results) {
                entities.add(getEntityManagementRestService().getEntity(entity));
            }
            // fail-safe check
            if (entities.size() != results.size()) {
                //This should never happen, But just to be sure
                throw new EntityMismatchException("Mismatch between entity Api v2 entities =" + results.size() + "  and EM entities=" + entities.size());
            }
        }
        return entities;
    }
}
