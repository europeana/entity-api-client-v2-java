package eu.europeana.entity.client;

import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.service.EntityApiRestClient;
import eu.europeana.entity.client.service.EntityManagementRestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class BaseEntityApiClient {

    private static final Logger LOG = LogManager.getLogger(BaseEntityApiClient.class);
    private static final int MAX_IN_MEM_SIZE_MB = 10;

    private final EntityClientConfiguration configuration;
    private EntityApiRestClient entityApiRestClient;
    private EntityManagementRestClient entityManagementRestClient;

    protected BaseEntityApiClient(EntityClientConfiguration configuration) {
        this.configuration = configuration;
        this.entityApiRestClient = new EntityApiRestClient(buildApiClient(this.configuration.getEntityApiUrl(), false), this.configuration.getApikey());
        this.entityManagementRestClient = new EntityManagementRestClient(buildApiClient(this.configuration.getEntityManagementUrl(), true), this.configuration.getApikey());    }

    protected BaseEntityApiClient() {
        this(new EntityClientConfiguration());
    }

    private WebClient buildApiClient(String apiEndpoint, boolean followRedirects) {
        return WebClient.builder()
                .baseUrl(apiEndpoint)
                .filter(logRequest())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .followRedirect(followRedirects)))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(MAX_IN_MEM_SIZE_MB * 1024 * 1024))
                        .build())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            LOG.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

    public EntityClientConfiguration getConfiguration() {
        return configuration;
    }

    public EntityApiRestClient getEntityApiRestClient() {
        return entityApiRestClient;
    }

    public EntityManagementRestClient getEntityManagementRestClient() {
        return entityManagementRestClient;
    }
}
