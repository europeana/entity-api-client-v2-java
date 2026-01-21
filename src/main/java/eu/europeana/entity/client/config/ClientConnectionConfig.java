package eu.europeana.entity.client.config;

import org.apache.commons.lang3.StringUtils;

/**
 * ClientConnectionConfig class
 *
 * @author srishti singh
 * @since 12 Jan 2026
 */
public class ClientConnectionConfig {

    private String totalMaxConnection;

    private String maxConnectionPerRoute;

    private String validateAfterInactivity;

    private String timeToLive;

    private String socketTimeout;

    private String responseTimeout;

    private String connectionRequestTimeout;

    public ClientConnectionConfig() {
    }

    /**
     * Constructor to instantiate ClientConnectionConfig
     * @param totalMaxConnection totalMaxConnection value
     * @param maxConnectionPerRoute maxConnectionPerRoute value
     * @param validateAfterInactivity validateAfterInactivity value in seconds
     * @param timeToLive timeToLive value in seconds
     * @param socketTimeout socketTimeout value in seconds
     * @param responseTimeout responseTimeout value in seconds
     * @param connectionRequestTimeout connectionRequestTimeout value in seconds
     */
    public ClientConnectionConfig(String totalMaxConnection, String maxConnectionPerRoute, String validateAfterInactivity,
                                  String timeToLive, String socketTimeout, String responseTimeout, String connectionRequestTimeout) {
        this.totalMaxConnection = totalMaxConnection;
        this.maxConnectionPerRoute = maxConnectionPerRoute;
        this.validateAfterInactivity = validateAfterInactivity;
        this.timeToLive = timeToLive;
        this.socketTimeout = socketTimeout;
        this.responseTimeout = responseTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getTotalMaxConnection() {
        return Integer.parseInt(totalMaxConnection);
    }

    public void setTotalMaxConnection(String totalMaxConnection) {
        this.totalMaxConnection = totalMaxConnection;
    }

    public int getMaxConnectionPerRoute() {
        return Integer.parseInt(maxConnectionPerRoute);
    }

    public void setMaxConnectionPerRoute(String maxConnectionPerRoute) {
        this.maxConnectionPerRoute = maxConnectionPerRoute;
    }

    public int getValidateAfterInactivity() {
        return Integer.parseInt(validateAfterInactivity);
    }

    public void setValidateAfterInactivity(String validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
    }

    public int getTimeToLive() {
        return Integer.parseInt(timeToLive);
    }

    public void setTimeToLive(String timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getSocketTimeout() {
        return Integer.parseInt(socketTimeout);
    }

    public void setSocketTimeout(String socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getResponseTimeout() {
        return Integer.parseInt(responseTimeout);
    }

    public void setResponseTimeout(String responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public int getConnectionRequestTimeout() {
        return Integer.parseInt(connectionRequestTimeout);
    }

    public void setConnectionRequestTimeout(String connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    /**
     * Checks if pooling connection property values are provided
     * @return true
     */
    public boolean hasPoolingConnMetadata() {
        return StringUtils.isNotBlank(this.totalMaxConnection) ||
                StringUtils.isNotBlank(this.maxConnectionPerRoute) ||
                StringUtils.isNotBlank(this.validateAfterInactivity) ||
                StringUtils.isNotBlank(this.timeToLive) ;
    }

    /**
     * Checks if IO reactor property values are provided
     * @return true
     */
    public boolean hasIOReactorMetadata() {
        return StringUtils.isNotBlank(this.socketTimeout);
    }

    /**
     * Checks if Request config property values are provided
     * @return true
     */
    public boolean hasRequestConfigMetadata() {
        return StringUtils.isNotBlank(this.responseTimeout) ||
                StringUtils.isNotBlank(this.connectionRequestTimeout);
    }

}
