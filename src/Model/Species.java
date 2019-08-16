package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

public class Species extends BiologicalEntity {
    private RealInterval initialAmount;
    private LinkTypeSpeciesCompartment linkTypeSpeciesCompartment;

    public Species(String id, Model m) throws PreconditionsException {
        super(id, m);
    }

    public Species(String id, Model m, String name) throws PreconditionsException {
        super(id, m);
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

    @Override
    public void cloneIntoModel(Model model) {

    }

    public RealInterval getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(RealInterval initialAmount) {
        this.initialAmount = initialAmount;
    }

    public void insertLinkSpeciesCompartment(LinkSpeciesCompartment pass, LinkTypeSpeciesCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkTypeSpeciesCompartment = l;
    }

    public void removeLinkSpeciesCompartment(LinkSpeciesCompartment pass)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkTypeSpeciesCompartment = null;
    }

    public LinkTypeSpeciesCompartment getLinkSpeciesCompartment() {
        return linkTypeSpeciesCompartment;
    }
}
