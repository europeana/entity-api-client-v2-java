package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.model.EntityRetrievalResponse;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class EntityManagementRestClient extends RestClient {

    private final WebClient webClient;
    private final String wskey;

    public EntityManagementRestClient(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    /**
     * Returns the Entity matching the entity id
     * This method executes the entity Retrieval method of EM.
     * @param entityId
     * @return
     * @throws TechnicalRuntimeException
     */
   public Entity getEntityById(String entityId) throws TechnicalRuntimeException {
      return getEntities(webClient,
              EntityClientUtils.buildEntityRetrievalUrl(EntityClientUtils.getEntityRetrievalId(entityId), wskey),null);
    }

    /**
     * Returns the Entities matching the entity ids.
     * This method executes the Multiple entity Retrieval method of EM.
     * @param entityIds
     * @return
     * @throws TechnicalRuntimeException
     */
    public List<Entity> getEntityByIds(List<String> entityIds) throws TechnicalRuntimeException {
        EntityRetrievalResponse result = getEntities(webClient,
                EntityClientUtils.buildMultipleEntityRetrievalUrl(wskey), entityIds.toString());
        return result.getItems();
    }
}
