package Model;

import java.util.HashMap;
import java.util.Map;
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

    public abstract void override();

//    public Map<AdditionalKnowledgeType, String> getAdditionalKnowledge(){
//        Map<AdditionalKnowledgeType, String> addKnow = new HashMap<AdditionalKnowledgeType, String>();
//
//    }
}
