package eu.europeana.entity.client.utils;

/**
 * Entity client constants class
 */
public class EntityApiConstants {

    private EntityApiConstants() {
        // hide implicit one
    }

    public static final String ENTITY_API_ENDPOINT_MISSING =  "Entity Api endpoint not provided !!!";
    public static final String EM_API_ENDPOINT_MISSING     =  "Entity Management Api endpoint not provided !!!";

    public static final String BASE_URL = "http://data.europeana.eu";
    public static final String ENTITY_ID_BASE = "base";

    public static final String HEADER_LOCATION = "location";

    public static final String SUGGEST_PATH = "suggest";
    public static final String ENRICH_PATH = "enrich";
    public static final String RESOLVE_PATH = "resolve";
    public static final String MULTIPLE_ENTITY_RETRIEVAL_PATH = "retrieve";
    public static final String PATH_SEPERATOR = "/";

    public static final String TEXT = "text";
    public static final String WSKEY = "wskey";
    public static final String LANG = "lang";
    public static final String LANGUAGE = "language";

    public static final String SCOPE = "scope";
    public static final String ROWS = "rows";
    public static final String ALGORITHM = "algorithm";
    public static final String URI = "uri";

    public static final String TOTAL = "total";
    public static final String ITEMS_FIELD = "items";
    public static final String ID = "id";
    public static final String TYPE = "type";
}
