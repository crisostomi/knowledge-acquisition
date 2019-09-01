import DataTypes.PreconditionsException;
import Model.*;
import InputKnowledge.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ConsolidateKB {

    private static final Logger LOGGER = Logger.getLogger( ConsolidateKB.class.getName() );

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
            if (!(ka instanceof CompartmentKA)) { continue; }
            ka.consolidateModelWithAtom(m);
        }

        for (KnowledgeAtom ka : KAtoms) {
            if (!(ka instanceof SpeciesKA)) { continue; }
            ka.consolidateModelWithAtom(m);
        }

        for (KnowledgeAtom ka : KAtoms) {
            if (!(ka instanceof ProteinKA)) { continue; }
            ka.consolidateModelWithAtom(m);
        }

        for (KnowledgeAtom ka : KAtoms) {
            if (!(ka instanceof ReactionKA)) { continue; }
            ka.consolidateModelWithAtom(m);
        }

        return m;
    }
}
