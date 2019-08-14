package InputKnowledge;

import DataTypes.PreconditionsException;
import Model.*;

import java.util.HashSet;
import java.util.Set;

public abstract class KnowledgeAtom {

    private final String id;
    private final String name;
    private final boolean override;

    private KnowledgeBase knowledgeBase;
    private Set<LinkTypeAdditionalKA> additionalKA = new HashSet<>();

    /**
     * Constructor for creating a KA without the name of the entity
     * @param id the id of the entity the KA describes
     * @param override if the KA is of 'override' type
     */
    public KnowledgeAtom(String id, boolean override, KnowledgeBase knowledgeBase) {
        this.id = id;
        this.name = null;
        this.override = override;
        this.knowledgeBase = knowledgeBase;
    }

    /**
     * Constructor for creating a KA with the name of the entity
     * @param id the id of the entity the KA describes
     * @param name the name of the entity the KA describes
     * @param override if the KA is of 'override' type
     */
    public KnowledgeAtom(String id, String name, boolean override, KnowledgeBase knowledgeBase) {
        this.id = id;
        this.name = name;
        this.override = override;
        this.knowledgeBase = knowledgeBase;
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

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public void insertLinkAdditionalKA(AdditionalKnowledgeType t, String v) throws PreconditionsException {
        LinkTypeAdditionalKA link = new LinkTypeAdditionalKA(t, this, v);
        additionalKA.add(link);
    }

    public Set<LinkTypeAdditionalKA> getLinkadditionalKA() {
        return (Set<LinkTypeAdditionalKA>)
                ((HashSet<LinkTypeAdditionalKA>)additionalKA).clone();
    }

    public abstract void consolidateModelWithAtom(Model m);

    public void addAdditionalKnowledge(BiologicalEntity be) {
        boolean preconditions = true;

    }
}
