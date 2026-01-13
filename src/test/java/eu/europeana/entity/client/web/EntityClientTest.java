package eu.europeana.entity.client.web;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import eu.europeana.entity.client.EntityApiClient;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entitymanagement.definitions.model.Organization;
import eu.europeana.entitymanagement.vocabulary.EntityTypes;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entitymanagement.definitions.model.Entity;

/**
 * Test class to test the client
 * @author srishti singh
 * @since 10 march 2025
 */
class EntityClientTest {

  EntityApi apiClient ;

  @Test
  void test() {
    fail("Not yet implemented");
  }


  /**
   * This constructor will create a Entity api client based on values present in properties file.
   * @throws EntityClientException
   */
  @BeforeEach
  void setup() throws EntityClientException {
    apiClient =  new EntityApiClient(new EntityClientConfiguration());
  }

  /**
   * get entity
   * @throws JsonProcessingException
   */
  @Test
  void testGetEntity_1() throws EntityClientException {
    Entity entity = apiClient.getEntity("http://data.europeana.eu/agent/61804");
    assertNotNull(entity);
    assertNotNull(entity.getType());
  }


  /**
   * To EM redirects for old entity Ids
   * @throws JsonProcessingException
   */
  @Test
  void testGetEntity_2() throws EntityClientException {
    Entity entity = apiClient.getEntity("http://data.europeana.eu/organization/1482250000001710507");
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals("4385", StringUtils.substringAfterLast(entity.getEntityId(), "/"));
  }



  /**
   * To test new Organisation Type : Aggregator
   * @throws JsonProcessingException
   */
  @Test
  void testGetEntity_3() throws EntityClientException {
    Organization entity = (Organization) apiClient.getEntity("http://data.europeana.eu/organization/4563");
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals(EntityTypes.Aggregator.getEntityType(), entity.getType());
    assertNotNull(entity.getAggregatesFrom());

  }

  /**
   * To test the hasGeo fields in Organisation
   * @throws JsonProcessingException
   */
  @Test
  void testGetEntity_4() throws EntityClientException {
    Organization entity = (Organization) apiClient.getEntity("http://data.europeana.eu/organization/713");
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals(EntityTypes.Organization.getEntityType(), entity.getType());
    // check address and has geo
    assertNotNull(entity.getAddress());
    assertNotNull(entity.getAddress().getVcardHasGeo());
    assertNotNull(entity.getAddress().getVcardHasGeo().hasMetadataProperties());

  }


  /**
   * resolve entity test
   * @throws JsonProcessingException
   */
  @Test
  void testResolveEntity_1() throws EntityClientException {
    Entity entity = apiClient.resolveEntity("http://openlibrary.org/works/OL59188A").get(0);
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals(EntityTypes.Agent.getEntityType(), entity.getType());
  }

  /**
   * resolve entity test
   * @throws JsonProcessingException
   */
  @Test
  void testResolveEntity_2() throws EntityClientException {
    Entity entity = apiClient.resolveEntity("http://dbpedia.org/resource/Johannes_Vermeer").get(0);
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals(EntityTypes.Agent.getEntityType(), entity.getType());
  }


  @Test
  void testEnrichment_1() throws EntityClientException {
    List<Entity> enrichments = apiClient.enrichEntity("CulturaItalia", null, "organization", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }


  /**
   * To test new Organisation Type : Aggregator
   * @throws EntityClientException
   */
  @Test
  void testEnrichment_2() throws EntityClientException {
    List<Entity> enrichments = apiClient.enrichEntity("Euskariana", null, "organization", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }


  /**
   * Test enrich
   * @throws EntityClientException
   */
  @Test
  void testEnrichment_3() throws EntityClientException {
    List<Entity> enrichments = apiClient.enrichEntity("Paris", null, "agent", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }

  
}
