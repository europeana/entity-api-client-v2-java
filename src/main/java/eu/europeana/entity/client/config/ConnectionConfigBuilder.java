package eu.europeana.entity.client.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.util.concurrent.TimeUnit;

import static eu.europeana.entity.client.config.EntityClientConfiguration.*;

/**
 * ClientConnectionConfig Builder class
 *
 * @author srishti singh
 * @since 12 Jan 2026
 */
public class ConnectionConfigBuilder {

    private ConnectionConfigBuilder () {
        // hide implicit one
    }

    /**
     * Builds PoolingAsyncClientConnectionManager
     * @param config entity client configuration
     * @param connConfig Client connection config
     * @return PoolingAsyncClientConnectionManager
     */
    public static PoolingAsyncClientConnectionManager buildPoolingConnection(EntityClientConfiguration config, ClientConnectionConfig connConfig) {
        if (config != null && config.hasPoolingConnMetadata()) {
            return PoolingAsyncClientConnectionManagerBuilder.create()
                    .setMaxConnTotal(config.getConnectionConfigValue(TOTAL_MAX_CONNECTION))
                    .setMaxConnPerRoute(config.getConnectionConfigValue(MAX_CONNECTION_PER_ROUTE))
                    .setDefaultConnectionConfig(
                            ConnectionConfig.custom()
                                    .setValidateAfterInactivity(TimeValue.ofSeconds(config.getConnectionConfigValue(VALIDATE_AFTER_INACTIVITY)))
                                    .setTimeToLive(TimeValue.ofSeconds(config.getConnectionConfigValue(TIME_TO_LIVE)))
                                    .build())
                    .build();
        }
        if (connConfig != null && connConfig.hasPoolingConnMetadata()) {
            return PoolingAsyncClientConnectionManagerBuilder.create()
                    .setMaxConnTotal(connConfig.getTotalMaxConnection())
                    .setMaxConnPerRoute(connConfig.getMaxConnectionPerRoute())
                    .setDefaultConnectionConfig(
                            ConnectionConfig.custom()
                                    .setValidateAfterInactivity(TimeValue.ofSeconds(connConfig.getValidateAfterInactivity()))
                                    .setTimeToLive(TimeValue.ofSeconds(connConfig.getTimeToLive()))
                                    .build())
                    .build();
        }
        return new PoolingAsyncClientConnectionManager(); // default
    }

    /**
     * Builds IOReactorConfig
     * @param config entity client configuration
     * @param connConfig Client connection config
     * @return IOReactorConfig
     */
    public static IOReactorConfig buildIOReactorConfig(EntityClientConfiguration config, ClientConnectionConfig connConfig) {
        if (config != null && config.hasIOReactorMetadata()) {
            return IOReactorConfig.custom()
                    .setSoTimeout(Timeout.of(config.getConnectionConfigValue(SOCKET_TIMEOUT), TimeUnit.SECONDS))
                    .build();
        }
        if (connConfig != null && connConfig.hasIOReactorMetadata()) {
            return IOReactorConfig.custom()
                    .setSoTimeout(Timeout.of(connConfig.getSocketTimeout(), TimeUnit.SECONDS))
                    .build();
        }
        return IOReactorConfig.custom().build(); // default
    }

    /**
     * Builds RequestConfig
     * @param config entity client configuration
     * @param connConfig Client connection config
     * @return RequestConfig
     */
    public static RequestConfig buildRequestConfig(EntityClientConfiguration config, ClientConnectionConfig connConfig){
        if (config != null && config.hasRequestConfigMetadata()) {
            return RequestConfig.custom()
                    .setConnectionRequestTimeout(Timeout.of(config.getConnectionConfigValue(CONNECTION_REQUEST_TIMEOUT), TimeUnit.SECONDS))
                    .setResponseTimeout(Timeout.of(config.getConnectionConfigValue(RESPONSE_TIMEOUT), TimeUnit.SECONDS))
                    .build();
        }
        if (connConfig != null && connConfig.hasRequestConfigMetadata()) {
            return RequestConfig.custom()
                    .setConnectionRequestTimeout(Timeout.of(connConfig.getConnectionRequestTimeout(), TimeUnit.SECONDS))
                    .setResponseTimeout(Timeout.of(connConfig.getResponseTimeout(), TimeUnit.SECONDS))
                    .build();
        }
        return  RequestConfig.custom().build(); // default
    }
}
