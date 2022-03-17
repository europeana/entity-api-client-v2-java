package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.function.Function;

public class EntityManagementRestClient extends RestClient {

    private final WebClient webClient;
    private final String wskey;

    public EntityManagementRestClient(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    private Entity getEntity(Function<UriBuilder, URI> uriBuilderURIFunction) throws TechnicalRuntimeException {
        return executeGet(webClient, uriBuilderURIFunction).bodyToMono(Entity.class).block();
    }

   public Entity getEntityById(String entityId) throws TechnicalRuntimeException {
       return getEntity(
              EntityClientUtils.buildEntityRetrievalUrl(EntityClientUtils.getEntityRetrievalId(entityId), wskey));
    }
}
