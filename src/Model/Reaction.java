package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

import java.util.Set;

public class Reaction extends BiologicalEntity {

    private RealInterval rate;
    private Set<LinkTypeReactant> reactants;
    private Set<LinkTypeProduct> products;
    private Set<LinkTypeModifier> modifiers;


    public Reaction(String id, String name) {
        super(id, name);
    }

    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if (this.getId().equals(bioEntity.getId()) && bioEntity instanceof Reaction){
            this.setName(bioEntity.getName());
        }
        else{
            throw new PreconditionsException("The overridden reaction must have the same id.");
        }
    }

    public void addReactant();

    public void addModifier();

    public void addProduct();

    public void overrideRate();

    public RealInterval getRate() {
        return rate;
    }

    public void setRate(RealInterval rate) {
        this.rate = rate;
    }
}
