package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public abstract class BiologicalEntity {

    private final String id; //unique
    private String name;
    private Set<LinkTypeAdditionalKnowledge> additionalKnowledge;

    public BiologicalEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void insertLinkAdditionalKnowledge(AdditionalKnowledgeType addKnowType, String value){
        LinkTypeAdditionalKnowledge link = new LinkTypeAdditionalKnowledge(this, addKnowType, value);
        this.additionalKnowledge.add(link);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public abstract void override(BiologicalEntity bioEntity) throws PreconditionsException;

    public Set<LinkTypeAdditionalKnowledge> getLinkAdditionalKnowledge() {
        return (Set<LinkTypeAdditionalKnowledge>)
                ((HashSet<LinkTypeAdditionalKnowledge>)additionalKnowledge).clone();
    }

    public void overrideName(BiologicalEntity bioEntity) throws PreconditionsException{
        if (this.getId().equals(bioEntity.getId())){
            this.name = bioEntity.name;
        }
        else {
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }
}
