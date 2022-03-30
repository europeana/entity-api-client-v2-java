package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.springframework.web.reactive.function.client.WebClient;

public class EntityManagementRestClient extends RestClient {

    private final WebClient webClient;
    private final String wskey;

    public EntityManagementRestClient(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

   public Entity getEntityById(String entityId) throws TechnicalRuntimeException {
      return getResults(webClient,
              EntityClientUtils.buildEntityRetrievalUrl(EntityClientUtils.getEntityRetrievalId(entityId), wskey), true, false);
    }
}
