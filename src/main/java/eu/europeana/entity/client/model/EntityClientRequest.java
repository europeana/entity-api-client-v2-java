package eu.europeana.entity.client.model;

import eu.europeana.entitymanagement.definitions.model.Entity;

import java.util.List;

// Class for external users to send Request and receive results
public class EntityClientRequest {

    private String value;
    private String language;
    private String type;
    private boolean isReference;
    List<Entity> entities;

    public EntityClientRequest(String value, String language, String type, boolean isReference) {
        this.value = value;
        this.language = language;
        this.type = type;
        this.isReference = isReference;
    }

    public EntityClientRequest(String value, String type, boolean isReference) {
        this.value = value;
        this.type = type;
        this.isReference = isReference;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        this.isReference = reference;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "EntityClientRequest{value=" + value + ", language='" + language + "', type=" + type + '}';
    }
}
