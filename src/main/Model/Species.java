package Model;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;
import Model.Link.*;
import Model.LinkType.LinkTypeModifier;
import Model.LinkType.LinkTypeProduct;
import Model.LinkType.LinkTypeReactant;
import Model.LinkType.LinkTypeSpeciesCompartment;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Species extends BiologicalEntity implements Serializable {

    private RealInterval initialAmount;
    private Set<LinkTypeProduct> linkTypeProductSet = new HashSet<>();
    private Set<LinkTypeReactant> linkTypeReactantSet = new HashSet<>();
    private Set<LinkTypeModifier> linkTypeModifierSet = new HashSet<>();
    private LinkTypeSpeciesCompartment linkTypeSpeciesCompartment;

    public Species(String id, Model m) throws PreconditionsException {
        super(id, m);
        this.initialAmount = new RealInterval(0, Double.MAX_VALUE);
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if (this.getId().equals(bioEntity.getId()) && bioEntity instanceof Species){
            this.overrideName(bioEntity);
            Species other = (Species)bioEntity;
            this.overrideInitialAmount(other);
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

// Reactant association, of which Reaction and Species are both responsibles

    public void insertLinkReactant(LinkReactant pass, LinkTypeReactant l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeReactantSet.add(l);
    }

    public void removeLinkReactant(LinkReactant pass, LinkTypeReactant l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeReactantSet.remove(l);
    }

    public Set<LinkTypeReactant> getLinkReactantSet() {
        return (Set<LinkTypeReactant>)((HashSet<LinkTypeReactant>)linkTypeReactantSet).clone();
    }

// Product association, of which Reaction and Species are both responsibles

    public void insertLinkProduct(LinkProduct pass, LinkTypeProduct l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeProductSet.add(l);
    }

    public void removeLinkProduct(LinkProduct pass, LinkTypeProduct l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeProductSet.remove(l);
    }

    public Set<LinkTypeProduct> getLinkProductSet() {
        return (Set<LinkTypeProduct>)((HashSet<LinkTypeProduct>)linkTypeProductSet).clone();
    }

// Modifier association, of which Reaction and Species are both responsibles

    public void removeLinkModifier(LinkModifier pass, LinkTypeModifier l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeModifierSet.remove(l);
    }
    public void insertLinkModifier(LinkModifier pass, LinkTypeModifier l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkTypeModifierSet.add(l);
    }

    public Set<LinkTypeModifier> getLinkModifierSet() {
        return (Set<LinkTypeModifier>)((HashSet<LinkTypeModifier>)linkTypeModifierSet).clone();
    }

// getters and setters

    public RealInterval getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(RealInterval initialAmount) {
        this.initialAmount = initialAmount;
    }

}
