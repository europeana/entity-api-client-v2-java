package eu.europeana.entity.client;

import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.config.WebClients;
import eu.europeana.entity.client.service.EntityApiRestService;
import eu.europeana.entity.client.service.EntityManagementRestService;

public abstract class BaseEntityApiClient {

    private final EntityClientConfiguration configuration;
    private final WebClients webClients;
    private final EntityApiRestService entityApiRestService;
    private final EntityManagementRestService entityManagementRestService;

    public BaseEntityApiClient() {
        this.configuration = new EntityClientConfiguration();
        this.webClients = new WebClients(this.configuration);
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
