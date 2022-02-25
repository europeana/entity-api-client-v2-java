package eu.europeana.entity.client;

import eu.europeana.api.commons.error.EuropeanaApiException;
import eu.europeana.entity.client.model.EntityClientRequest;
import eu.europeana.entity.client.web.WebEntityProtocolApi;
import eu.europeana.entity.client.web.WebEntityProtocolApiImpl;
import eu.europeana.entitymanagement.definitions.exceptions.UnsupportedEntityTypeException;
import org.codehaus.jettison.json.JSONException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ONLY for LIVE testing purpose
@SpringBootApplication
public class EntityApplication {

    public static void main(String args[]) throws EuropeanaApiException, JSONException, UnsupportedEntityTypeException {

        WebEntityProtocolApi webEntityProtocolApi = new WebEntityProtocolApiImpl();
         //webEntityProtocolApi.getSuggestions("paris", "nl", null, "agent", null, null);
         //System.out.println(webEntityProtocolApi.getEntityById("http://data.europeana.eu/agent/61804").getPrefLabel());
        //  webEntityProtocolApi.getEntitybyUri("http://dbpedia.org/resource/Johannes_Vermeer");


        System.out.println(new EntityClientRequest("http://dbpedia.org/resource/Johannes_Vermeer", "en", "agent,place", true));

    }
}
