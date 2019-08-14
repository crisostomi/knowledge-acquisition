package InputKnowledge;

import Model.*;

public class RevReactionKA extends ReactionKA {

    public RevReactionKA(String id, boolean override, KnowledgeBase knowledgeBase) {
        super(id, override, knowledgeBase);
    }

    public RevReactionKA(String id, boolean override, KnowledgeBase knowledgeBase, String name) {
        super(id, override, knowledgeBase, name);
    }

    @Override
    public void consolidateModelWithAtom(Model m) {

    }
}
