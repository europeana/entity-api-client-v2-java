package eu.europeana.entity.client.web;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import eu.europeana.entitymanagement.definitions.model.Entity;

class EntityClientTest {

  @Test
  void test() {
    fail("Not yet implemented");
  }

  @Test
  void testEnrichment() throws JsonProcessingException {
    EntityClientApi apiClient = new EntityClientApiImpl();
    List<Entity> enrichments = apiClient.getEnrichment("CulturaItalia", null, "organization", null);
    assertNotNull(enrichments);
    assertFalse(enrichments.isEmpty());
  }
  
  
}
