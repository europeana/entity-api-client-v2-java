package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.TechnicalRuntimeException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
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

    private Entity getEntity(Function<UriBuilder, URI> uriBuilderURIFunction, Class<? extends Entity> clazz) throws TechnicalRuntimeException {
        return executeGet(webClient, uriBuilderURIFunction).bodyToMono(clazz).block();
    }

   public Entity getEntityById(String entityId) throws UnsupportedEntityTypeException, TechnicalRuntimeException {
       return getEntity(
              EntityClientUtils.buildEntityRetrievalUrl(EntityClientUtils.getEntityRetrievalId(entityId), wskey)
              ,EntityClientUtils.getEntityClassById(entityId).getClass());
    }
}
