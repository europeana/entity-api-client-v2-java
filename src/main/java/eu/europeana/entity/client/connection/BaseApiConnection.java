package eu.europeana.entity.client.connection;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import eu.europeana.api.commons_sb3.http.AsyncHttpConnection;
import eu.europeana.entity.client.exception.EntityClientException;
import eu.europeana.entity.client.utils.EntityApiConstants;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Base class for creating connection to the apis - entity api and entity management
 * @author srishti singh
 */
public class BaseApiConnection extends EntityApiConstants {

    protected static final Logger LOGGER = LogManager.getLogger(BaseApiConnection.class);

    protected static final String ERROR_MESSAGE = "Entity API Client call failed - ";

    protected final AsyncHttpConnection entityApiConnection;
    protected final AsyncHttpConnection entityManagementConnection;

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
       initialize(entityApiUri, entityManagementApiUri, auth);

       // create basic CloseableHttpAsyncClient with default Connection pool
        this.entityApiConnection        = new AsyncHttpConnection();
        this.entityManagementConnection =  new AsyncHttpConnection(true);
        // start the async client
        entityApiConnection.start();
        entityManagementConnection.start();
    }

    public BaseApiConnection(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth,
                             PoolingAsyncClientConnectionManager connPool,
                             IOReactorConfig reactorConfig,
                             RequestConfig requestConfig) {
        initialize(entityApiUri, entityManagementApiUri, auth);

        // create CloseableHttpAsyncClient with custom connection pool , reactor config, and request config
        this.entityApiConnection        = new AsyncHttpConnection(connPool, requestConfig, reactorConfig, false);
        this.entityManagementConnection =  new AsyncHttpConnection(connPool, requestConfig, reactorConfig, true);
        // start the async client
        entityApiConnection.start();
        entityManagementConnection.start();
    }

    private void initialize(String entityApiUri, String entityManagementApiUri, AuthenticationHandler auth) {
        this.entityApiUri               = entityApiUri;
        this.entityManagementApiUri     = entityManagementApiUri;
        this.auth                       = auth;

        // set object mapper
        SimpleModule module             = new SimpleModule();
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


    public AsyncHttpConnection getEntityApiConnection() {
        return entityApiConnection;
    }

    public AsyncHttpConnection getEntityManagementConnection() {
        return entityManagementConnection;
    }

    public void close() throws EntityClientException {
        try {
            entityApiConnection.close();
            entityManagementConnection.close();
        } catch (IOException e) {
            throw new EntityClientException("Error while closing the client connections... ");
        }
    }
}
