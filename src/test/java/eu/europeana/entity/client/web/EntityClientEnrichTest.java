package eu.europeana.entity.client.web;

import eu.europeana.entity.client.EntityApiClient;
import eu.europeana.entity.client.config.EntityClientConfiguration;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entitymanagement.definitions.model.Entity;
import eu.europeana.entitymanagement.vocabulary.EntityTypes;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntityClientEnrichTest {

    EntityApi apiClient ;

    /**
     * This constructor will create a Entity api client based on values present in properties file.
     * @throws EntityClientException
     */
    @BeforeAll
    void setup() throws EntityClientException {
        apiClient =  new EntityApiClient(new EntityClientConfiguration());
    }

    /**
     * Tests the behavior - when called with null values for the text
     * and language map parameters.
     *
     * Expected behavior:
     * - An {@link EntityClientException} is thrown.
     * - The exception message is "No values provided for enrichment request".
     */
    @Test
    public void testEnrichment_1() {
        EntityClientException exception = assertThrows(EntityClientException.class, () ->
                apiClient.enrichEntity("place", null, 0));

        assertEquals("No values provided for enrichment request", exception.getMessage());
    }

    /**
     * Test scenario - rows invalid value for enrichment
     * @throws EntityClientException
     */
    @Test
    public void testEnrichment_2() throws EntityClientException {
        List<Entity> enrichments = apiClient.enrichEntity("place", Collections.singletonMap("paris", "en"), 0);
        assertTrue(enrichments.isEmpty());

        enrichments = apiClient.enrichEntity("place", Collections.singletonMap("paris", "en"), 60);
        assertTrue(enrichments.isEmpty());
    }

    /**
     * Test scenario - empty rows or zero rows
     *                Enrichments results should be present as the default value of rows is 10
     * @throws EntityClientException
     */
    @Test
    public void testEnrichment_3() throws EntityClientException {
        List<Entity> enrichments = apiClient.enrichEntity("place", Collections.singletonMap("paris", "en"), 0);
        assertNotNull(enrichments);
        assertEquals(EntityTypes.Place.getEntityType(), enrichments.get(0).getType());
    }

    /**
     * Test scenario - empty lang in the map
     * @throws EntityClientException
     */
    @Test
    public void testEnrichment_4() throws EntityClientException {
        List<Entity> enrichments = apiClient.enrichEntity("place", Collections.singletonMap("paris", null), 0);
        assertNotNull(enrichments);
        assertEquals(EntityTypes.Place.getEntityType(), enrichments.get(0).getType());
        assertTrue(StringUtils.contains(enrichments.get(0).getEntityId(), "place/41488"));

        enrichments = apiClient.enrichEntity("agent", Collections.singletonMap("paris", ""), 0);
        assertNotNull(enrichments);
        assertEquals(EntityTypes.Agent.getEntityType(), enrichments.get(0).getType());
        assertTrue(StringUtils.contains(enrichments.get(0).getEntityId(), "agent/52295"));
    }

    /**
     * Test scenario - empty type
     * @throws EntityClientException
     */
    @Test
    public void testEnrichment_5() throws EntityClientException {
        List<Entity> enrichments = apiClient.enrichEntity("", Collections.singletonMap("paris", ""), 0);
        assertNotNull(enrichments);
        assertEquals(2, enrichments.size());

        assertEquals(EntityTypes.Place.getEntityType(), enrichments.get(0).getType());
        assertTrue(StringUtils.contains(enrichments.get(0).getEntityId(), "place/41488"));

        assertEquals(EntityTypes.Agent.getEntityType(), enrichments.get(1).getType());
        assertTrue(StringUtils.contains(enrichments.get(1).getEntityId(), "agent/52295"));
    }

    /**
     * Test scenario - multiple values
     * @throws EntityClientException
     */
    @Test
    public void testEnrichment_6() throws EntityClientException {
        Map<String, String> map = new HashMap<>();
        map.put("paris", "en");
        map.put("India", null);
        map.put("France", "fr");
        List<Entity> enrichments = apiClient.enrichEntity("", map, 0);
        assertNotNull(enrichments);
        assertEquals(7, enrichments.size());
    }

}
