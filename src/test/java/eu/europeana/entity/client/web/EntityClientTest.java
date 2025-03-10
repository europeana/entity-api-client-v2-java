package eu.europeana.entity.client.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entitymanagement.definitions.model.Entity;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityClientTest {

  private EntityClientApi apiClient;

  @BeforeEach
  void setUp() {
    final Properties properties = new Properties();
    properties.put("entity.management.url", "");
    properties.put("entity.api.url", "");
    properties.put("entity.api.key", "");
    final EntityClientConfiguration clientConfiguration = new EntityClientConfiguration(properties);
    apiClient = new EntityClientApiImpl(clientConfiguration);
  }

  @Test
  void testEnrichment() throws JsonProcessingException {
    List<Entity> enrichments = apiClient.getEnrichment("CulturaItalia", null, "organization", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }

  @Test
  void testEntityRetrievalWithRedirectAreTheSameEntity_expectSuccess() {
    Entity entityA = apiClient.getEntityById("http://data.europeana.eu/organization/4385");
    Entity entityB = apiClient.getEntityById("http://data.europeana.eu/organization/1482250000001710507");
    assertNotNull(entityA);
    assertNotNull(entityB);
    assertEquals(entityA.getType(), entityB.getType());
    assertEquals(entityA.getEntityId(), entityB.getEntityId());
    assertEquals(entityA.getAbout(), entityB.getAbout());
    assertEquals(entityA.getContext(), entityB.getContext());
    assertEquals(entityA.getSameReferenceLinks(), entityB.getSameReferenceLinks());
  }

}
