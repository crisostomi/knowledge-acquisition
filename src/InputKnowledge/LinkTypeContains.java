package InputKnowledge;

import java.util.Objects;

public class LinkTypeContains {

    private KnowledgeBase knowledgeBase;
    private KnowledgeAtom knowledgeAtom;

    public LinkTypeContains(KnowledgeBase knowledgeBase, KnowledgeAtom knowledgeAtom) {
        this.knowledgeBase = knowledgeBase;
        this.knowledgeAtom = knowledgeAtom;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public KnowledgeAtom getKnowledgeAtom() {
        return knowledgeAtom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeContains that = (LinkTypeContains) o;
        return knowledgeBase == that.knowledgeBase && knowledgeAtom == that.knowledgeAtom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(knowledgeBase, knowledgeAtom);
    }
}
