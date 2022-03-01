package eu.europeana.entity.client;

import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.config.WebClients;
import eu.europeana.entity.client.service.EntityApiRestService;
import eu.europeana.entity.client.service.EntityManagementRestService;

public abstract class BaseEntityApiClient {

    private final EntityClientConfiguration configuration;
    private final WebClients webClients;
    private EntityApiRestService entityApiRestService;
    private EntityManagementRestService entityManagementRestService;

    public BaseEntityApiClient(EntityClientConfiguration configuration) {
        this.configuration = configuration;
        this.webClients = new WebClients(this.configuration);
        init();
    }

    public BaseEntityApiClient() {
        this.configuration = new EntityClientConfiguration();
        this.webClients = new WebClients(this.configuration);
        init();
    }

    private void init() {
        this.entityApiRestService = new EntityApiRestService(this.webClients.getEntityApiClient(), this.configuration.getApikey());
        this.entityManagementRestService = new EntityManagementRestService(this.webClients.getEntityManagementClient(), this.configuration.getApikey());
    }

    public EntityClientConfiguration getConfiguration() {
        return configuration;
    }

    public WebClients getWebClients() {
        return webClients;
    }

    public EntityApiRestService getEntityApiRestService() {
        return entityApiRestService;
    }

    public EntityManagementRestService getEntityManagementRestService() {
        return entityManagementRestService;
    }
}
