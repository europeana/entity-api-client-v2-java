package eu.europeana.entity.client.web;

import eu.europeana.api.commons_sb3.auth.apikey.ApikeyBasedAuthentication;
import eu.europeana.entity.client.EntityApiClient;
import eu.europeana.entity.client.config.ClientConnectionConfig;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.EntityClientException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * Instantiation test
 * @author srishti singh
 * @since 14 Jan 2026
 */
public class EntityClientInstantiationTest {


    private Properties properties;

    /**
     * This constructor will create a Entity api client based on values present in properties file.
     * @throws EntityClientException
     */
    @BeforeEach
    void setup() {
        properties = new Properties();
        properties.put("entity.api.url" , "http://localhost:entity.api.url");
        properties.put("entity.management.url" , "http://localhost:entity.management.api.url");
        properties.put("apikey" , "invalid_for_test");
    }

    @Test
    public void testEntityClient_withBasicConfiguration() throws EntityClientException {
        EntityApiClient entityClientApi = new EntityApiClient(new EntityClientConfiguration(properties));
        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }

    @Test
    public void testEntityClient_withConnectionValuesInConfiguration() throws EntityClientException {
        addConnectionConfig();
        EntityApiClient entityClientApi = new EntityApiClient(new EntityClientConfiguration(properties));
        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }

    @Test
    public void testEntityClient_withoutAnyConfig() throws EntityClientException {
        EntityApiClient entityClientApi = new EntityApiClient(
                "http://localhost:entity.api.url",
                "http://localhost:entity.management.api.url"
                , new ApikeyBasedAuthentication("invalid_test"));

        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }


    @Test
    public void testEntityClient_withConnectionConfiguration() throws EntityClientException {
        EntityApiClient entityClientApi = new EntityApiClient(    "http://localhost:entity.api.url",
                "http://localhost:entity.management.api.url"
                , new ApikeyBasedAuthentication("invalid_test"),
                new ClientConnectionConfig("25", "5",
                        "5", "60", "300",
                        "30", "60"));

        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }

    @Test
    public void testEntityClient_withEntityConfiguration() throws EntityClientException {
        EntityApiClient entityClientApi =  new EntityApiClient(new EntityClientConfiguration(properties),null);

        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }

    @Test
    public void testEntityClient_withEntityConfiguration_WithConnectionValues() throws EntityClientException {
        addConnectionConfig();
        EntityApiClient entityClientApi =  new EntityApiClient(new EntityClientConfiguration(properties),null);

        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }


    @Test
    public void testEntityClient_withEntityConfiguration_AndConnectionConfig() throws EntityClientException {
        EntityApiClient entityClientApi =  new EntityApiClient(new EntityClientConfiguration(properties),
                new ClientConnectionConfig("25", "5",
                "5", "60", "300",
                "30", "60"));

        Assertions.assertNotNull(entityClientApi);
        Assertions.assertEquals(ApikeyBasedAuthentication.class, entityClientApi.getEntityClientApiConnection().getAuthenticationHandler().getClass());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityApiConnection());
        Assertions.assertNotNull(entityClientApi.getEntityClientApiConnection().getEntityManagementConnection());
    }

    @AfterEach
    void cleanup() {
        properties.clear();
    }

    private void addConnectionConfig() {
        properties.put("entity.client.total.max.connection", "25");
        properties.put("entity.client.max.connection.per.route", "5");
        properties.put("entity.client.validate.after.inactivity", "5");
        properties.put("entity.client.time.to.live", "5");
        properties.put("entity.client.socket.timeout", "60");
        properties.put("entity.client.connection.request.timeout", "300");
        properties.put("entity.client.response.timeout", "30");
    }

}
