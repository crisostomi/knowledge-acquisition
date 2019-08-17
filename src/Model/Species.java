package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

public class Species extends BiologicalEntity {

    private RealInterval initialAmount;
    private LinkTypeSpeciesCompartment linkTypeSpeciesCompartment;

    public Species(String id, Model m) throws PreconditionsException {
        super(id, m);
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if (this.getId().equals(bioEntity.getId()) && bioEntity instanceof Species){
            this.overrideName(bioEntity);
            this.overrideInitialAmount((Species)bioEntity);
        }
        else{
            throw new PreconditionsException("The overridden species must have the same id.");
        }
    }

    public void overrideInitialAmount(Species species) throws PreconditionsException {
        if (this.getId().equals(species.getId())){
            this.initialAmount = species.getInitialAmount();
        }
        else{
            throw new PreconditionsException("The overridden species must have the same id.");
        }
    }

    @Override
    public Species cloneIntoModel(Model model) throws PreconditionsException{
        Species species = new Species(this.getId(), model);
        species.setName(this.getName());
        species.setInitialAmount(this.getInitialAmount());
        LinkComprises.insertLink(model, species);
        BiologicalEntity comp = model.getBioEntityById(species.getLinkSpeciesCompartment().getCompartment().getId());
        if ( comp == null || !(comp instanceof Compartment)) {
            comp = this.getLinkSpeciesCompartment().getCompartment().cloneIntoModel(model);
        }
        LinkSpeciesCompartment.insertLink(species, (Compartment) comp);
        return species;
    }


// SpeciesCompartment association, of which Compartment and Species are both responsibles, 0/1 to n

    public void insertLinkSpeciesCompartment(LinkSpeciesCompartment pass, LinkTypeSpeciesCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeSpeciesCompartment = l;
    }

    public void removeLinkSpeciesCompartment(LinkSpeciesCompartment pass)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeSpeciesCompartment = null;
    }

    public LinkTypeSpeciesCompartment getLinkSpeciesCompartment() {
        return linkTypeSpeciesCompartment;
    }


// getters and setters

    public RealInterval getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(RealInterval initialAmount) {
        this.initialAmount = initialAmount;
    }

    @Override
    public String toString() {
        return "Species{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                "initialAmount=" + initialAmount +
                '}';
    }
}
