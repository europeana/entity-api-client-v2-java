package eu.europeana.entity.client;

import eu.europeana.api.commons_sb3.auth.AuthenticationBuilder;
import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.connection.EntityClientApiConnection;
import eu.europeana.entity.client.exception.EntityClientException;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.reactor.IOReactorConfig;

public class BaseEntityApiClient {

    EntityClientApiConnection entityClientApiConnection;

    protected BaseEntityApiClient(EntityClientConfiguration config) throws EntityClientException {
        this(config.getEntityApiUrl(), config.getEntityManagementUrl(), AuthenticationBuilder.newAuthentication(config));
    }

    protected BaseEntityApiClient(String entityApiUrl, String entityManagementUrl, AuthenticationHandler auth) throws EntityClientException {
        if (entityApiUrl == null) {
            throw new EntityClientException(" Entity Api endpoint not provided !!!");
        }
        if (entityManagementUrl == null) {
            throw new EntityClientException(" Entity Management Api endpoint not provided !!!");
        }
        this.entityClientApiConnection = new EntityClientApiConnection(entityApiUrl, entityManagementUrl, auth);
    }

    protected BaseEntityApiClient(String entityApiUrl, String entityManagementUrl, AuthenticationHandler auth,
                                  PoolingAsyncClientConnectionManager connPool,
                                  IOReactorConfig reactorConfig,
                                  RequestConfig requestConfig) throws EntityClientException {
        if (entityApiUrl == null) {
            throw new EntityClientException(" Entity Api endpoint not provided !!!");
        }
        if (entityManagementUrl == null) {
            throw new EntityClientException(" Entity Management Api endpoint not provided !!!");
        }
        this.entityClientApiConnection = new EntityClientApiConnection(entityApiUrl, entityManagementUrl, auth, connPool, reactorConfig, requestConfig);
    }

    public AuthenticationHandler getAuthenticationHandler() {
        return entityClientApiConnection.getAuthenticationHandler();
    }

    public void setAuthenticationHandler(AuthenticationHandler auth) {
        entityClientApiConnection.setAuthenticationHandler(auth);
    }

    public EntityClientApiConnection getEntityClientApiConnection() {
        return entityClientApiConnection;
    }

    public void close() throws EntityClientException {
        this.entityClientApiConnection.close();
    }

}
