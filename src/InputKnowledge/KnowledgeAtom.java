package InputKnowledge;

import DataTypes.PreconditionsException;
import Model.*;

import java.util.HashSet;
import java.util.Set;

public abstract class KnowledgeAtom {

    protected final String id;
    protected final String name;
    private final boolean override;

    private Set<LinkTypeAdditionalKA> additionalKA = new HashSet<>();
    private LinkTypeContains linkContains;

    /**
     * Constructor for creating a KA without the name of the entity
     * @param id the id of the entity the KA describes
     * @param override if the KA is of 'override' type
     */
    public KnowledgeAtom(String id, boolean override, KnowledgeBase knowledgeBase)
                        throws PreconditionsException {
        this.id = id;
        this.name = null;
        this.override = override;

        LinkContains.insertLink(knowledgeBase, this);
    }

    /**
     * Constructor for creating a KA with the name of the entity
     * @param id the id of the entity the KA describes
     * @param override if the KA is of 'override' type
     * @param name the name of the entity the KA describes
     */
    public KnowledgeAtom(String id, boolean override, KnowledgeBase knowledgeBase, String name)
            throws PreconditionsException {
        this.id = id;
        this.override = override;
        this.name = name;

        LinkContains.insertLink(knowledgeBase, this);
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

    public void insertLinkAdditionalKA(AdditionalKnowledgeType t, String v) throws PreconditionsException {
        LinkTypeAdditionalKA link = new LinkTypeAdditionalKA(t, this, v);
        additionalKA.add(link);
    }

    public Set<LinkTypeAdditionalKA> getLinkAdditionalKA() {
        return (Set<LinkTypeAdditionalKA>)
                ((HashSet<LinkTypeAdditionalKA>)additionalKA).clone();
    }

    public abstract void consolidateModelWithAtom(Model m) throws PreconditionsException ;

    protected void addAdditionalKnowledge(BiologicalEntity be) throws PreconditionsException {

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

    protected void handleBioEntityName(BiologicalEntity be) throws PreconditionsException {
        if (!this.id.equals(be.getId())) throw new IdMismatchException();
        if (be.getName() != null && !be.getName().equals(this.getName())) throw new NameMismatchException();

        if (this.getName() != null) be.setName(this.getName());
    }

    public void insertLinkContains(LinkContains pass, LinkTypeContains l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        if (linkContains != null) throw new LinkMultiplicityException();
        linkContains = l;
    }

    public LinkTypeContains getLinkContains() {
        return linkContains;
    }
}
