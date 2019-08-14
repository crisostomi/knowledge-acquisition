package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

public class Species extends BiologicalEntity {
    private RealInterval initialAmount;

    public Species(String id) throws PreconditionsException {
        super(id);
    }

    public Species(String id, String name) throws PreconditionsException {
        super(id);
        this.setName(name);
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if (this.getId().equals(bioEntity.getId()) && bioEntity instanceof Species){
            this.setName(bioEntity.getName());
        }
        else{
            throw new PreconditionsException("The overridden species must have the same id.");
        }
    }

    public RealInterval getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(RealInterval initialAmount) {
        this.initialAmount = initialAmount;
    }
}
