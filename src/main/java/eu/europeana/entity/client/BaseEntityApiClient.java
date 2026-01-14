package eu.europeana.entity.client;

import eu.europeana.api.commons_sb3.auth.AuthenticationBuilder;
import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.entity.client.config.ClientConnectionConfig;
import eu.europeana.entity.client.config.ConnectionConfigBuilder;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.connection.EntityClientApiConnection;
import eu.europeana.entity.client.exception.EntityClientException;

public class BaseEntityApiClient {

    EntityClientApiConnection entityClientApiConnection;

    /**
     * Creates EntityApiClient connection via properties included in the EntityClientConfiguration
     * If client connection properties are present - creates a custom client
     *        else creates client with defaults
     *
     * @param config
     * @throws EntityClientException
     */
    protected BaseEntityApiClient(EntityClientConfiguration config) throws EntityClientException {
        if (config.getEntityApiUrl() == null) {
            throw new EntityClientException(" Entity Api endpoint not provided !!!");
        }
        if (config.getEntityManagementUrl() == null) {
            throw new EntityClientException(" Entity Management Api endpoint not provided !!!");
        }
        this.entityClientApiConnection = new EntityClientApiConnection(config.getEntityApiUrl(),
                config.getEntityManagementUrl(),
                AuthenticationBuilder.newAuthentication(config),
                ConnectionConfigBuilder.buildPoolingConnection(config, null),
                ConnectionConfigBuilder.buildIOReactorConfig(config, null),
                ConnectionConfigBuilder.buildRequestConfig(config, null));

    }

    /**
     * Creates EntityApiClient connection with defaults
     * @param entityApiUrl
     * @param entityManagementUrl
     * @throws EntityClientException
     */
    protected BaseEntityApiClient(String entityApiUrl, String entityManagementUrl, AuthenticationHandler auth) throws EntityClientException {
        if (entityApiUrl == null) {
            throw new EntityClientException(" Entity Api endpoint not provided !!!");
        }
        if (entityManagementUrl == null) {
            throw new EntityClientException(" Entity Management Api endpoint not provided !!!");
        }
        this.entityClientApiConnection = new EntityClientApiConnection(entityApiUrl, entityManagementUrl, auth);
    }

    protected BaseEntityApiClient(EntityClientConfiguration config, ClientConnectionConfig connConfig) throws EntityClientException {
        this.entityClientApiConnection = new EntityClientApiConnection(config.getEntityApiUrl(), config.getEntityManagementUrl(),
                AuthenticationBuilder.newAuthentication(config),
                ConnectionConfigBuilder.buildPoolingConnection(config, connConfig),
                ConnectionConfigBuilder.buildIOReactorConfig(config, connConfig),
                ConnectionConfigBuilder.buildRequestConfig(config, connConfig));
    }

    protected BaseEntityApiClient(String entityApiUrl, String entityManagementUrl, AuthenticationHandler auth, ClientConnectionConfig connConfig) throws EntityClientException {
        if (entityApiUrl == null) {
            throw new EntityClientException(" Entity Api endpoint not provided !!!");
        }
        if (entityManagementUrl == null) {
            throw new EntityClientException(" Entity Management Api endpoint not provided !!!");
        }
        this.entityClientApiConnection = new EntityClientApiConnection(entityApiUrl, entityManagementUrl, auth,
                ConnectionConfigBuilder.buildPoolingConnection(null, connConfig),
                ConnectionConfigBuilder.buildIOReactorConfig(null, connConfig),
                ConnectionConfigBuilder.buildRequestConfig(null, connConfig));
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
