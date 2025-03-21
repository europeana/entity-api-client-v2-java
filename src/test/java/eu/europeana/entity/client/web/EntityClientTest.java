package eu.europeana.entity.client.web;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import eu.europeana.entitymanagement.definitions.model.Organization;
import eu.europeana.entitymanagement.vocabulary.EntityTypes;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entitymanagement.definitions.model.Entity;

/**
 * Test class to test the client
 * @author srishti singh
 * @since 10 march 2025
 */
class EntityClientTest {

  @Test
  void test() {
    fail("Not yet implemented");
  }


  /**
   * get entity
   * @throws JsonProcessingException
   */
  @Test
  void testGetEntity_1() {
    EntityClientApi apiClient = new EntityClientApiImpl();
    Entity entity = apiClient.getEntity("http://data.europeana.eu/agent/61804");
    assertNotNull(entity);
    assertNotNull(entity.getType());
  }


  /**
   * To EM redirects for old entity Ids
   * @throws JsonProcessingException
   */
  @Test
  void testGetEntity_2() {
    EntityClientApi apiClient = new EntityClientApiImpl();
    Entity entity = apiClient.getEntity("http://data.europeana.eu/organization/1482250000001710507");
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals("4385", StringUtils.substringAfterLast(entity.getEntityId(), "/"));
  }



//  /**
//   * To test new Organisation Type : Aggregator
//   * @throws JsonProcessingException
//   */
//  @Test
//  void testGetEntity_3() {
//    EntityClientApi apiClient = new EntityClientApiImpl();
//    Organization entity = (Organization) apiClient.getEntity("http://data.europeana.eu/organization/4563");
//    assertNotNull(entity);
//    assertNotNull(entity.getType());
//    assertEquals(EntityTypes.Aggregator.getEntityType(), entity.getType());
//    assertNotNull(entity.getAggregatesFrom());
//
//  }


  /**
   * resolve entity test
   * @throws JsonProcessingException
   */
  @Test
  void testResolveEntity_1() {
    EntityClientApi apiClient = new EntityClientApiImpl();
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
  void testResolveEntity_2() {
    EntityClientApi apiClient = new EntityClientApiImpl();
    Entity entity = apiClient.resolveEntity("http://dbpedia.org/resource/Johannes_Vermeer").get(0);
    assertNotNull(entity);
    assertNotNull(entity.getType());
    assertEquals(EntityTypes.Agent.getEntityType(), entity.getType());
  }


  @Test
  void testEnrichment_1() throws JsonProcessingException {
    EntityClientApi apiClient = new EntityClientApiImpl();
    List<Entity> enrichments = apiClient.enrichEntity("CulturaItalia", null, "organization", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }


  /**
   * To test new Organisation Type : Aggregator
   * @throws JsonProcessingException
   */
  @Test
  void testEnrichment_2() throws JsonProcessingException {
    EntityClientApi apiClient = new EntityClientApiImpl();
    List<Entity> enrichments = apiClient.enrichEntity("Euskariana", null, "organization", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }


  /**
   * Test enrich
   * @throws JsonProcessingException
   */
  @Test
  void testEnrichment_3() throws JsonProcessingException {
    EntityClientApi apiClient = new EntityClientApiImpl();
    List<Entity> enrichments = apiClient.enrichEntity("Paris", null, "agent", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }

  
}
