package eu.europeana.entity.client.config;

import eu.europeana.api.commons_sb3.auth.AuthenticationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Properties;

public class EntityClientConfiguration extends AuthenticationConfig {

    private static final Logger LOGGER = LogManager.getLogger(EntityClientConfiguration.class);

    public static final String PROPERTIES_FILE = "/entity-client.properties";
    public static final String ENTITY_API_URL = "entity.api.url";
    public static final String ENTITY_MANAGEMENT_URL = "entity.management.url";


    public EntityClientConfiguration() {
        super();
        loadProperties(PROPERTIES_FILE);
    }

    public EntityClientConfiguration(Properties properties) {
        super(properties);
    }

    private void loadProperties(String propertiesFile) {
        try {
            load(getClass().getResourceAsStream(propertiesFile));
        }
        catch (IOException e) {
            LOGGER.error("Error loading the properties file {}", propertiesFile);
        }
    }


    public String getEntityApiUrl() {
        return getProperty(ENTITY_API_URL);
    }

    public String getEntityManagementUrl() {
        return getProperty(ENTITY_MANAGEMENT_URL);
    }

}
