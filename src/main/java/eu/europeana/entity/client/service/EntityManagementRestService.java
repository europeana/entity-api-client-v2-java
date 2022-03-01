package eu.europeana.entity.client.service;

import eu.europeana.entity.client.exception.AuthenticationException;
import eu.europeana.entity.client.exception.EntityNotFoundException;
import eu.europeana.entity.client.utils.EntityClientUtils;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.Exceptions;

import java.net.URI;
import java.util.function.Function;

public class EntityManagementRestService {

    private final WebClient webClient;
    private final String wskey;

    public EntityManagementRestService(WebClient webClient, String wskey) {
        this.webClient = webClient;
        this.wskey = wskey;
    }

    private Entity executeGet(Function<UriBuilder, URI> uriBuilderURIFunction, Class<? extends Entity> clazz) throws AuthenticationException {
        try {
            return webClient
                    .get()
                    .uri(uriBuilderURIFunction)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                            HttpStatus.UNAUTHORIZED::equals,
                            response -> response.bodyToMono(String.class).map(AuthenticationException::new))
                    .onStatus(HttpStatus.NOT_FOUND :: equals,
                            response -> response.bodyToMono(String.class).map(EntityNotFoundException::new))
                    .bodyToMono(clazz)
                    .block();
        } catch (Exception e) {
            /*
             * Spring WebFlux wraps exceptions in ReactiveError (see Exceptions.propagate())
             * So we need to unwrap the underlying exception, for it to be handled by callers of this method
             **/
            Throwable t = Exceptions.unwrap(e);
            if(t instanceof AuthenticationException) {
                throw new AuthenticationException("User is not authorised to perform this action");
            }
            if (t instanceof EntityNotFoundException) {
                return null;
            }
            // all other exception should be propagated
            throw e;
        }
    }

   public Entity getEntity(Entity entity) throws AuthenticationException {
        return executeGet(
                EntityClientUtils.buildEntityRetrievalUrl(EntityClientUtils.getEntityRetrievalId(entity.getEntityId()), wskey)
                ,entity.getClass());
   }

   public Entity getEntityById(String entityId) throws UnsupportedEntityTypeException, AuthenticationException {
       return executeGet(
              EntityClientUtils.buildEntityRetrievalUrl(EntityClientUtils.getEntityRetrievalId(entityId), wskey)
              ,EntityClientUtils.getEntityClassById(entityId).getClass());
    }
}
