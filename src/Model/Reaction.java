package Model;

import DataTypes.ModifierType;
import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Set;

public class Reaction extends BiologicalEntity {

    private static final Logger LOGGER = Logger.getLogger( Reaction.class.getName() );

    private RealInterval rate;
    private RealInterval rateInv;
    private boolean reversible = false;

    private Set<LinkTypeReactant> reactants = new HashSet<>();
    private Set<LinkTypeProduct> products = new HashSet<>();
    private Set<LinkTypeModifier> modifiers = new HashSet<>();

    private LinkTypeReactionCompartment linkTypeReactionCompartment;


    public Reaction(String id, Model m) throws PreconditionsException {
        super(id, m);
    }

    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if (this.getId().equals(bioEntity.getId()) && bioEntity instanceof Reaction){
            this.setName(bioEntity.getName());
            this.overrideRate((Reaction)bioEntity);
        }
        else{
            throw new PreconditionsException("The overridden reaction must have the same id.");
        }
    }

    public void overrideRate(Reaction other) throws PreconditionsException{
        if (this.getId().equals(other.getId())) {
            this.setRate(other.rate);
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }

    @Override
    public Reaction cloneIntoModel(Model model) throws PreconditionsException {
        Reaction reaction = new Reaction(this.getId(), model);
        reaction.setName(this.getName());
        reaction.setRate(this.getRate());
        LinkComprises.insertLink(model, reaction);

        BiologicalEntity comp = model.getBioEntityById(reaction.getLinkReactionCompartment().getCompartment().getId());
        if ( comp == null || !(comp instanceof Compartment)) {
            comp = this.getLinkReactionCompartment().getCompartment().cloneIntoModel(model);
        }
        LinkReactionCompartment.insertLink(reaction, (Compartment) comp);

        for ( LinkTypeReactant linkReactant: this.getReactants()){
            Species reactant = linkReactant.getSpecies();
            BiologicalEntity destinationModelSpecies = model.getBioEntityById(this.getId());
            // if not found:
            if ( destinationModelSpecies == null || !(destinationModelSpecies instanceof Reaction) ){
                destinationModelSpecies = reactant.cloneIntoModel(model);
            }
            reaction.addReactant( (Species)destinationModelSpecies, linkReactant.getStoichiometry());
        }

        for ( LinkTypeProduct linkProduct: this.getProducts()){
            Species product = linkProduct.getSpecies();
            BiologicalEntity destinationModelSpecies = model.getBioEntityById(this.getId());
            // if not found:
            if ( destinationModelSpecies == null || !(destinationModelSpecies instanceof Reaction) ){
                destinationModelSpecies = product.cloneIntoModel(model);
            }
            reaction.addProduct( (Species)destinationModelSpecies, linkProduct.getStoichiometry());
        }

        for ( LinkTypeModifier linkModifier: this.getModifiers()){
            Species modifier = linkModifier.getSpecies();
            BiologicalEntity destinationModelSpecies = model.getBioEntityById(this.getId());
            // if not found:
            if ( destinationModelSpecies == null || !(destinationModelSpecies instanceof Reaction) ){
                destinationModelSpecies = modifier.cloneIntoModel(model);
            }
            reaction.addModifier( (Species)destinationModelSpecies, linkModifier.getModifierType());
        }
        return reaction;
    }

// Reactant association, of which Reaction is the only responsible, n to n

    public void addReactant(Species species, int stoichiometry){
        LinkTypeReactant linkTypeReac = new LinkTypeReactant(species, this, stoichiometry);
        this.reactants.add(linkTypeReac);
    }

    public Set<LinkTypeReactant> getReactants() {
        return (Set<LinkTypeReactant>)((HashSet<LinkTypeReactant>)reactants).clone();
    }

// Modifier association, of which Reaction is the only responsible, n to n

    public void addModifier(Species species, ModifierType type){
        LinkTypeModifier linkTypeMod = new LinkTypeModifier(species, this, type);
        this.modifiers.add(linkTypeMod);
    }

    public Set<LinkTypeModifier> getModifiers() {
        return (Set<LinkTypeModifier>)((HashSet<LinkTypeModifier>)modifiers).clone();
    }

// Product association, of which Reaction is the only responsible, n to n

    public void addProduct(Species species, int stoichiometry){
        LinkTypeProduct linkTypeProd = new LinkTypeProduct(species, this, stoichiometry);
        this.products.add(linkTypeProd);
    }

    public Set<LinkTypeProduct> getProducts() {
        return (Set<LinkTypeProduct>)((HashSet<LinkTypeProduct>)products).clone();
    }

// ReactionCompartment association, of which Compartment and Reaction are both responsibles, 0/1 to n

    public void insertLinkReactionCompartment(LinkReactionCompartment pass, LinkTypeReactionCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkReactionCompartment to invoke this method");
        linkTypeReactionCompartment = l;
    }

    public void removeLinkReactionCompartment(LinkReactionCompartment pass)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkReactionCompartment to invoke this method");
        linkTypeReactionCompartment = null;
    }

    public LinkTypeReactionCompartment getLinkReactionCompartment() {
        return linkTypeReactionCompartment;
    }


// getters and setters

    public RealInterval getRate() {
        return rate;
    }

    public void setRate(RealInterval rate) {
        this.rate = rate;
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public void setReversible() {
        this.reversible = true;
    }

    public RealInterval getRateInv() throws PreconditionsException {
        if (this.reversible) return rateInv;
        else throw new PreconditionsException(
                "Reaction " + this.id + " is not reversible"
        );
    }

    public void setRateInv(RealInterval rateInv) throws PreconditionsException{
        if (this.reversible) this.rateInv = rateInv;
        else throw new PreconditionsException(
                "Reaction " + this.getId() + " is not reversible"
        );
    }
}
