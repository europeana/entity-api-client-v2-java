package eu.europeana.entity.client.model;

import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;

public class EntityRetrievalResponse {

    private List<Entity> entities;

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}
