package eu.europeana.entity.client.connection;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.api.commons_sb3.http.HttpConnection;
import eu.europeana.entity.client.utils.EntityApiConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseApiConnection extends EntityApiConstants {

    protected static final Logger LOGGER = LogManager.getLogger(EntityClientApiConnection.class);

    protected static final String ERROR_MESSAGE = "Entity API Client call failed - ";

    protected final HttpConnection entityApiConnection = new HttpConnection();
    protected final HttpConnection entityManagementConnection = new HttpConnection(true);

    protected final ObjectMapper mapper = new ObjectMapper();

    protected String                entityApiUri;
    protected String                entityManagementApiUri;

    protected AuthenticationHandler auth;

    /**
     * BaseApiConnection constructor
     * @param entityApiUri entity api service url
     * @param entityManagementApiUri Entity Management Service url
     * @param auth Authentication Handler for the client
     */
    public BaseApiConnection(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth) {
        this.entityApiUri = entityApiUri;
        this.entityManagementApiUri = entityManagementApiUri;
        this.auth = auth;

        // set object mapper
        SimpleModule module = new SimpleModule();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(module);
        mapper.findAndRegisterModules();
    }


    public AuthenticationHandler getAuthenticationHandler() {
        return this.auth;
    }

    public void setAuthenticationHandler(AuthenticationHandler auth) {
        this.auth = auth;
    }


}
