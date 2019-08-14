package InputKnowledge;

import DataTypes.PreconditionsException;
import Model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    public Set<LinkTypeAdditionalKA> getLinkAdditionalKA() {
        return (Set<LinkTypeAdditionalKA>)
                ((HashSet<LinkTypeAdditionalKA>)additionalKA).clone();
    }

    public abstract void consolidateModelWithAtom(Model m);

    public void addAdditionalKnowledge(BiologicalEntity be) throws PreconditionsException {

        for (LinkTypeAdditionalKA link1 : this.getLinkAdditionalKA()) {
            AdditionalKnowledgeType t1 = link1.getAdditionalKnowledgeType();

            for (LinkTypeAdditionalKnowledge link2 : be.getLinkAdditionalKnowledge()) {
                AdditionalKnowledgeType t2 = link2.getAddKnowType();

                if (t1 == t2 && !link1.getValue().equals(link2.getValue())) {
                    throw new AdditionalKnowledgeMismatchException(
                            "Biological entity " + be.getId() + " already has value " + link2.getValue() +
                                    " for AdditionalKnowledgeType " + t1.getId() +
                                    " which is different from value " + link1.getValue() + " provided by the atom"
                    );
                }
            }
        }

        for (LinkTypeAdditionalKA link : this.getLinkAdditionalKA()) {
            be.insertLinkAdditionalKnowledge(link.getAdditionalKnowledgeType(), link.getValue());
        }
    }

    public void handleBioEntityName(BiologicalEntity be) throws PreconditionsException {
        if (!this.id.equals(be.getId())) throw new IdMismatchException();
        if (this.getName() == null) throw new KnowledgeAtomNameNotFoundException();
        if (be.getName() != null && !be.getName().equals(this.getName())) throw new NameMismatchException();

        be.setName(this.getName());
    }
}
