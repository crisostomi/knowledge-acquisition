package InputKnowledge;

import Model.Model;

public abstract class KnowledgeAtom {

    private final String id;
    private final String name;
    private final boolean override;

    /**
     * Constructor for creating a KA without the name of the entity
     * @param id the id of the entity the KA describes
     * @param override if the KA is of 'override' type
     */
    public KnowledgeAtom(String id, boolean override) {
        this.id = id;
        this.name = null;
        this.override = override;
    }

    /**
     * Constructor for creating a KA with the name of the entity
     * @param id the id of the entity the KA describes
     * @param name the name of the entity the KA describes
     * @param override if the KA is of 'override' type
     */
    public KnowledgeAtom(String id, String name, boolean override) {
        this.id = id;
        this.name = name;
        this.override = override;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOverride() {
        return override;
    }

    public abstract void consolidateModelWithAtom(Model m);

}
