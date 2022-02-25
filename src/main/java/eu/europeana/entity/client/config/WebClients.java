package eu.europeana.entity.client.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClients {

    private static final Logger LOG = LogManager.getLogger(WebClients.class);

    private EntityClientConfiguration config;
    private int maxInMemSizeMb = 10;

    public WebClients(EntityClientConfiguration config) {
        this.config = config;
    }

    public WebClient getEntityApiClient() {
        return getApiClient(config.getEntityApiUrl());
    }

    public WebClient getEntityManagementClient() {
        return getApiClient(config.getEntityManagementUrl());
    }

    private WebClient getApiClient(String apiEndpoint) {
        return WebClient.builder()
                .baseUrl(apiEndpoint)
                .filter(logRequest())
                .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(maxInMemSizeMb * 1024 * 1024))
                        .build())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            LOG.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            return next.exchange(clientRequest);
        };
    }
}
