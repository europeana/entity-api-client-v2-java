package eu.europeana.entity.client.exception;

/**
 * Exception for the Entity client api
 */
public class EntityClientException extends Exception {

    private static final long serialVersionUID = 8281933808897246375L;
    private final int remoteStatusCode;

    /**
     * Constructor for exception to indicate that an error occurred during invocation of the remote
     * service or parsing of service response
     *
     * @param msg the error message
     * @param remoteStatusCode the status code from the remote service
     * @param t eventual exception thrown when extracting information from the remote service response
     */
    public EntityClientException(String msg, int remoteStatusCode, Throwable t) {
        super(msg, t);
        this.remoteStatusCode = remoteStatusCode;
    }

    /**
     * Constructor for exception to indicate that an error occurred during invocation of the remote
     * service or parsing of service response
     *
     * @param msg the error message
     * @param remoteStatusCode the status code from the remote service
     */
    public EntityClientException(String msg, int remoteStatusCode) {
        super(msg);
        this.remoteStatusCode = remoteStatusCode;
    }

    /**
     * Constructor for exception to indicate that an error occurred during invocation of the remote
     * service, to be used when no response from the remote service has been received
     *
     * @param msg the error message
     */
    public EntityClientException(String msg) {
        this(msg, -1);
    }

    public int getRemoteStatusCode() {
        return remoteStatusCode;
    }
}
