package InputKnowledge;

import DataTypes.PreconditionsException;
import Model.AdditionalKnowledgeType;

public final class LinkTypeAdditionalKA {

    private final AdditionalKnowledgeType additionalKnowledgeType;
    private final KnowledgeAtom knowledgeAtom;
    private final String value;

    public LinkTypeAdditionalKA(AdditionalKnowledgeType t, KnowledgeAtom ka, String v)
            throws PreconditionsException {

        if (t == null || ka == null) {
            throw new PreconditionsException(
                    "Cannot link null objects"
            );
        }

        this.additionalKnowledgeType = t;
        this.knowledgeAtom = ka;
        this.value = v;
    }

    public AdditionalKnowledgeType getAdditionalKnowledgeType() {
        return additionalKnowledgeType;
    }

    public KnowledgeAtom getKnowledgeAtom() {
        return knowledgeAtom;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeAdditionalKA that = (LinkTypeAdditionalKA) o;
        return this.additionalKnowledgeType == that.additionalKnowledgeType &&
                this.knowledgeAtom == that.knowledgeAtom;
    }

    @Override
    public int hashCode() {
        return additionalKnowledgeType.hashCode() + knowledgeAtom.hashCode();
    }
}
