package eu.europeana.entity.client.model;

import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;

/**
 * Entity retrieval Response class
 * @author Srishti singh
 */
public class EntityRetrievalResponse {

    private String context;
    private String type;
    private long total;

    private List<Entity> items;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Entity> getItems() {
        return items;
    }

    public void setItems(List<Entity> items) {
        this.items = items;
    }
}
