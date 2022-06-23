package eu.europeana.entity.client.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Properties;

public class EntityClientConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(EntityClientConfiguration.class);

    public static final String PROPERTIES_FILE = "/entity-client.properties";
    public static final String API_KEY = "entity.api.key";
    public static final String ENTITY_API_URL = "entity.api.url";
    public static final String ENTITY_MANAGEMENT_URL = "entity.management.url";

    Properties properties;

    public EntityClientConfiguration() {
        loadProperties(PROPERTIES_FILE);
    }

    public EntityClientConfiguration(Properties properties) {
        this.properties = properties;
    }

    private Properties loadProperties(String propertiesFile) {
        try { properties = new Properties();
            properties.load(getClass().getResourceAsStream(propertiesFile));
        } catch (IOException e) {
            LOGGER.error("Error loading the properties file {}", PROPERTIES_FILE);
        }
        return properties;
    }

    public String getApikey(){
        return getProperty(API_KEY);
    }

    public String getEntityApiUrl() {
        return getProperty(ENTITY_API_URL);
    }

    public String getEntityManagementUrl() {
        return getProperty(ENTITY_MANAGEMENT_URL);
    }

    private String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
