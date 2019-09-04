package Model.LinkType;

import Model.BiologicalEntity;
import Model.AdditionalKnowledgeType;

import java.io.Serializable;
import java.util.Objects;

public class LinkTypeAdditionalKnowledge implements Serializable {

    private BiologicalEntity biologicalEntity;
    private AdditionalKnowledgeType addKnowType;
    private String value;

    public LinkTypeAdditionalKnowledge(BiologicalEntity biologicalEntity, AdditionalKnowledgeType addKnowType, String value) {
        this.biologicalEntity = biologicalEntity;
        this.addKnowType = addKnowType;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeAdditionalKnowledge that = (LinkTypeAdditionalKnowledge) o;
        return biologicalEntity == that.biologicalEntity &&
                addKnowType == that.addKnowType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(biologicalEntity, addKnowType);
    }

    public BiologicalEntity getBiologicalEntity() {
        return biologicalEntity;
    }

    public AdditionalKnowledgeType getAddKnowType() {
        return addKnowType;
    }

    public String getValue() {
        return value;
    }
}
