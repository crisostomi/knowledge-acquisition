package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

public class ReversibleReaction extends Reaction {

    private RealInterval rateInv;


    public ReversibleReaction(String id, Model m) throws PreconditionsException {
        super(id, m);
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if(this.getId().equals(bioEntity.getId()) && bioEntity instanceof ReversibleReaction){
            super.override(bioEntity);
            this.overrideRevRate((ReversibleReaction) bioEntity);
        }
        else{
            throw new PreconditionsException("The overrider and the overridee must have same ID and same class.");
        }
    }

    public void overrideRevRate(ReversibleReaction revReaction) throws PreconditionsException{
        if(this.getId().equals(revReaction.getId())){
            this.rateInv = revReaction.rateInv;
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }

    public RealInterval getRateInv() {
        return rateInv;
    }

    public void setRateInv(RealInterval rateInv) {
        this.rateInv = rateInv;
    }
}
