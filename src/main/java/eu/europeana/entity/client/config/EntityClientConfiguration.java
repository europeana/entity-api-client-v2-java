package eu.europeana.entity.client.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import eu.europeana.api.commons_sb3.auth.AuthenticationConfig;

/**
 * Entity API client configuration class
 * 
 * @author srishti singh
 */
public class EntityClientConfiguration extends AuthenticationConfig {

  private static final long serialVersionUID = 1;

  private static final Logger LOGGER = LogManager.getLogger(EntityClientConfiguration.class);

  public static final String PROPERTIES_FILE = "entity-client.properties";
  public static final String CONFIG_FOLDER = "/opt/app/config/";
  public static final String ENTITY_API_URL = "entity.api.url";
  public static final String ENTITY_MANAGEMENT_URL = "entity.management.url";

  // client connection configuration
  public static final String TOTAL_MAX_CONNECTION = "entity.client.total.max.connection";
  public static final String MAX_CONNECTION_PER_ROUTE = "entity.client.max.connection.per.route";
  public static final String VALIDATE_AFTER_INACTIVITY = "entity.client.validate.after.inactivity";
  public static final String TIME_TO_LIVE = "entity.client.time.to.live";
  public static final String SOCKET_TIMEOUT = "entity.client.socket.timeout";
  public static final String CONNECTION_REQUEST_TIMEOUT =
      "entity.client.connection.request.timeout";
  public static final String RESPONSE_TIMEOUT = "entity.client.response.timeout";
  public static final String TCP_NO_DELAY = "entity.client.tcp.no.delay";


  /**
   * Constructor
   */
  public EntityClientConfiguration() {
    super();
    loadProperties(PROPERTIES_FILE);
  }

  public EntityClientConfiguration(Properties properties) {
    super(properties);
  }

  private void loadProperties(String propertiesFile) {
    File externalConfigFile = new File(CONFIG_FOLDER, propertiesFile);
    // first check if the
    if (externalConfigFile.exists()) {
      loadFromConfigFile(externalConfigFile);
    } else {
      loadFromClasspathFile(propertiesFile);
    }
  }

  void loadFromClasspathFile(String propertiesFile) {
    try {
      // try loading from classpath
      //ensure /
      String classpathPropsFile = (propertiesFile.startsWith("/") ? propertiesFile : "/" + propertiesFile);
      load(getClass().getResourceAsStream(classpathPropsFile));

    } catch (IOException e) {
      LOGGER.error("Error loading the properties file from classpath: {}", propertiesFile, e);
    }
  }

  void loadFromConfigFile(File externalConfigFile) {
    try (InputStream is = java.nio.file.Files.newInputStream(externalConfigFile.toPath())) {
      load(is);
    } catch (IOException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Error loading the properties config folder: {}", externalConfigFile.getName(), e);
      }
    }
  }


  public String getEntityApiUrl() {
    return getProperty(ENTITY_API_URL);
  }

  public String getEntityManagementUrl() {
    return getProperty(ENTITY_MANAGEMENT_URL);
  }

  public String getTotalMaxConnection() {
    return getProperty(TOTAL_MAX_CONNECTION);
  }

  public String getMaxConnectionPerRoute() {
    return getProperty(MAX_CONNECTION_PER_ROUTE);
  }

  public String getValidateAfterInactivity() {
    return getProperty(VALIDATE_AFTER_INACTIVITY);
  }

  public String getTimeToLive() {
    return getProperty(TIME_TO_LIVE);
  }

  public String getConnectionRequestTimeout() {
    return getProperty(CONNECTION_REQUEST_TIMEOUT);
  }

  public String getSocketTimeout() {
    return getProperty(SOCKET_TIMEOUT);
  }

  public String getResponseTimeout() {
    return getProperty(RESPONSE_TIMEOUT);
  }

  public boolean getTcpNoDelay() {
    return Boolean.parseBoolean(getProperty(TCP_NO_DELAY));
  }


  /**
   * return the Integer value of the property requested
   * @return value
   */
  public int getConnectionConfigValue(String property) {
    String value = getProperty(property);
    if (StringUtils.isNotEmpty(value)) {
      return Integer.parseInt(value);
    }
    return 0;
  }

  /**
   * Checks if pooling connection property values are provided
   * @return true
   */
  public boolean hasPoolingConnMetadata() {
    return StringUtils.isNotBlank(getTotalMaxConnection())
        || StringUtils.isNotBlank(getMaxConnectionPerRoute())
        || StringUtils.isNotBlank(getValidateAfterInactivity())
        || StringUtils.isNotBlank(getTimeToLive());
  }


  /**
   * Checks if IO reactor property values are provided
   * @return true
   */
  public boolean hasIOReactorMetadata() {
    return StringUtils.isNotBlank(getSocketTimeout());
  }

  /**
   * Checks if Request config property values are provided
   * @return true
   */
  public boolean hasRequestConfigMetadata() {
    return StringUtils.isNotBlank(getResponseTimeout())
        || StringUtils.isNotBlank(getConnectionRequestTimeout());
  }

}
