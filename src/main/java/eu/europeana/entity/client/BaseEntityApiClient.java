package eu.europeana.entity.client;

import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.config.WebClients;
import eu.europeana.entity.client.service.EntityApiRestClient;
import eu.europeana.entity.client.service.EntityManagementRestClient;

public abstract class BaseEntityApiClient {

    private final EntityClientConfiguration configuration;
    private final WebClients webClients;
    private EntityApiRestClient entityApiRestClient;
    private EntityManagementRestClient entityManagementRestClient;

    protected BaseEntityApiClient(EntityClientConfiguration configuration) {
        this.configuration = configuration;
        this.webClients = new WebClients(this.configuration);
        init();
    }

    protected BaseEntityApiClient() {
        this.configuration = new EntityClientConfiguration();
        this.webClients = new WebClients(this.configuration);
        init();
    }

    private void init() {
        this.entityApiRestClient = new EntityApiRestClient(this.webClients.getEntityApiClient(), this.configuration.getApikey());
        this.entityManagementRestClient = new EntityManagementRestClient(this.webClients.getEntityManagementClient(), this.configuration.getApikey());
    }

    public EntityClientConfiguration getConfiguration() {
        return configuration;
    }

    public WebClients getWebClients() {
        return webClients;
    }

    public EntityApiRestClient getEntityApiRestClient() {
        return entityApiRestClient;
    }

    public EntityManagementRestClient getEntityManagementRestClient() {
        return entityManagementRestClient;
    }
}
