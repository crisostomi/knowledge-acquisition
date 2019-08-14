package InputKnowledge;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;
import Model.*;

public class RevReactionKA extends ReactionKA {

    private RealInterval rateInv = null;

    public RevReactionKA(String id, boolean override, KnowledgeBase knowledgeBase) {
        super(id, override, knowledgeBase);
    }

    public RevReactionKA(String id, boolean override, KnowledgeBase knowledgeBase, String name) {
        super(id, override, knowledgeBase, name);
    }

    @Override
    public void consolidateModelWithAtom(Model m) {

    }

    private void handleRevReactionRate(ReversibleReaction r) throws PreconditionsException {
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "ReversibleReaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        if (this.rateInv != null) {
            if (r.getRateInv() != null) {
                RealInterval newRateInv = r.getRateInv().intersect(this.rateInv);
                r.setRate(newRateInv);
            } else {
                r.setRate(this.rate);
            }
        } else if (r.getRateInv() == null) {
            RealInterval newRate = new RealInterval(0, Double.MAX_VALUE);
            r.setRateInv(newRate);
        }
    }
}
