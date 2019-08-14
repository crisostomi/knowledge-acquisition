package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

public class ReversibleReaction extends Reaction {
    RealInterval revRate;


    public ReversibleReaction(String id) throws PreconditionsException {
        super(id);
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        super.override(bioEntity);
        this.overrideRevRate((ReversibleReaction) bioEntity);
    }

    public void overrideRevRate(ReversibleReaction revReaction) throws PreconditionsException{
        if(this.getId().equals(revReaction.getId())){
            this.revRate = revReaction.revRate;
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }
}
