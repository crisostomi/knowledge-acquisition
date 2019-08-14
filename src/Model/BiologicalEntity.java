package Model;

public abstract class BiologicalEntity {
    private final String id; //unique
    private String name;

    public BiologicalEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract void override();

    public Map<AdditionalKnowledgeType, String> getAdditionalKnowledge(){

    }
}
