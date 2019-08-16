import DataTypes.PreconditionsException;
import Model.*;
import InputKnowledge.*;

import java.util.HashSet;
import java.util.Set;

public class ConsolidateKB {

    public static Model consolidateKB(Set<KnowledgeBase> KBs) throws PreconditionsException {

        Set<KnowledgeAtom> override = new HashSet<>();
        Set<KnowledgeAtom> nonOverride = new HashSet<>();

        for (KnowledgeBase kb : KBs) {
            for (LinkTypeContains link : kb.getLinkContainsSet()) {
                KnowledgeAtom ka = link.getKnowledgeAtom();
                if (ka.isOverride()) {
                    override.add(ka);
                } else {
                    nonOverride.add(ka);
                }
            }
        }

        return overrideModel(KB2Model(nonOverride), KB2Model(override));
    }

    private static Model overrideModel(Model overridee, Model overrider) throws PreconditionsException{
        overridee.overrideModel(overrider);

        return overridee;
    }

    private static Model KB2Model(Set<KnowledgeAtom> KAtoms)throws PreconditionsException {
        Model m = new Model();
        for (KnowledgeAtom ka : KAtoms) {
            ka.consolidateModelWithAtom(m);
        }

        return m;
    }
}
