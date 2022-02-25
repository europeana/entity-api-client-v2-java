package eu.europeana.entity.client.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import eu.europeana.entitymanagement.definitions.model.Entity;
import eu.europeana.entitymanagement.vocabulary.EntityTypes;

import java.util.List;
import java.util.Map;

// TODO - Remove this class once entity management has provided a concrete solution for deserializing Agent class
// Class with Agent and ConsolidatedAgent Fields. This is to make sure webClient parses the Class properly
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"@context", "id", "type", "depiction", "isShownBy", "prefLabel", "altLabel", "hiddenLabel", "name", "begin", "dateOfBirth", "dateOfEstablishment", "end", "dateOfDeath", "dateOfTermination", "date", "placeOfBirth", "placeOfDeath", "gender", "professionOrOccupation", "biographicalInformation", "note", "hasPart", "isPartOf", "hasMet", "isRelatedTo", "wasPresentAt", "identifier", "sameAs"})
public class Agent
        extends Entity
{
    private String type = EntityTypes.Agent.name();
    private List<String> date;
    private String begin;
    private String end;
    private String dateOfBirth;
    private String dateOfDeath;
    private List<String> wasPresentAt;
    private List<String> hasMet;
    private Map<String, String> name;
    private Map<String, List<String>> biographicalInformation;
    private List<String> professionOrOccupation;
    private String placeOfBirth;
    private String placeOfDeath;
    private String dateOfEstablishment;
    private String dateOfTermination;
    private String gender;
    private List<String> sameAs;

    public Agent() {
        //Empty constructor
    }

    @JsonGetter("wasPresentAt")
    public List<String> getWasPresentAt() {
        return this.wasPresentAt;
    }

    @JsonSetter("wasPresentAt")
    public void setWasPresentAt(List<String> wasPresentAt) {
        this.wasPresentAt = wasPresentAt;
    }

    @JsonGetter("date")
    public List<String> getDate() {
        return this.date;
    }

    @JsonSetter("date")
    public void setDate(List<String> date) {
        this.date = date;
    }

    @JsonGetter("begin")
    public String getBegin() {
        return this.begin;
    }

    @JsonSetter("begin")
    public void setBegin(String begin) {
        this.begin = begin;
    }

    @JsonGetter("end")
    public String getEnd() {
        return this.end;
    }

    @JsonSetter("end")
    public void setEnd(String end) {
        this.end = end;
    }

    @JsonGetter("hasMet")
    public List<String> getHasMet() {
        return this.hasMet;
    }

    @JsonSetter("hasMet")
    public void setHasMet(List<String> hasMet) {
        this.hasMet = hasMet;
    }

    @JsonGetter("name")
    public Map<String, String> getName() {
        return this.name;
    }

    @JsonSetter("name")
    public void setName(Map<String, String> name) {
        this.name = name;
    }

    @JsonGetter("biographicalInformation")
    public Map<String, List<String>> getBiographicalInformation() {
        return this.biographicalInformation;
    }

    @JsonSetter("biographicalInformation")
    public void setBiographicalInformation(Map<String, List<String>> biographicalInformation) {
        this.biographicalInformation = biographicalInformation;
    }

    @JsonGetter("dateOfBirth")
    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    @JsonSetter("dateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonGetter("dateOfDeath")
    public String getDateOfDeath() {
        return this.dateOfDeath;
    }

    @JsonSetter("dateOfDeath")
    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    @JsonGetter("placeOfBirth")
    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    @JsonSetter("placeOfBirth")
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @JsonGetter("placeOfDeath")
    public String getPlaceOfDeath() {
        return this.placeOfDeath;
    }

    @JsonSetter("placeOfDeath")
    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    @JsonGetter("dateOfEstablishment")
    public String getDateOfEstablishment() {
        return this.dateOfEstablishment;
    }

    @JsonSetter("dateOfEstablishment")
    public void setDateOfEstablishment(String dateOfEstablishment) {
        this.dateOfEstablishment = dateOfEstablishment;
    }

    @JsonGetter("dateOfTermination")
    public String getDateOfTermination() {
        return this.dateOfTermination;
    }

    @JsonSetter("dateOfTermination")
    public void setDateOfTermination(String dateOfTermination) {
        this.dateOfTermination = dateOfTermination;
    }

    @JsonGetter("gender")
    public String getGender() {
        return this.gender;
    }

    @JsonSetter("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonGetter("professionOrOccupation")
    public List<String> getProfessionOrOccupation() {
        return this.professionOrOccupation;
    }

    @JsonSetter("professionOrOccupation")
    public void setProfessionOrOccupation(List<String> professionOrOccupation) {
        this.professionOrOccupation = professionOrOccupation;
    }

    public String getType() {
        return this.type;
    }

    @JsonSetter("sameAs")
    public void setSameReferenceLinks(List<String> uris) {
        this.sameAs = uris;
    }


    @JsonGetter("sameAs")
    public List<String> getSameReferenceLinks() {
        return this.sameAs;
    }
}
