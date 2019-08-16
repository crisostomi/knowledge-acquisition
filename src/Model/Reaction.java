package Model;

import DataTypes.ModifierType;
import DataTypes.PreconditionsException;
import DataTypes.RealInterval;

import java.util.HashSet;
import java.util.Set;

public class Reaction extends BiologicalEntity {

    private RealInterval rate;
    private Set<LinkTypeReactant> reactants;
    private Set<LinkTypeProduct> products;
    private Set<LinkTypeModifier> modifiers;

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

    @Override
    public void cloneIntoModel(Model model) {

    }

    public void addReactant(Species species, int stoichiometry){
        LinkTypeReactant linkTypeReac = new LinkTypeReactant(species, this, stoichiometry);
        this.reactants.add(linkTypeReac);
    }

    public void addModifier(Species species, ModifierType type){
        LinkTypeModifier linkTypeMod = new LinkTypeModifier(species, this, type);
        this.modifiers.add(linkTypeMod);
    }

    public void addProduct(Species species, int stoichiometry){
        LinkTypeProduct linkTypeProd = new LinkTypeProduct(species, this, stoichiometry);
        this.products.add(linkTypeProd);
    }

    public void overrideRate(Reaction other) throws PreconditionsException{
        if (this.getId().equals(other.getId())) {
            this.setRate(other.rate);
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }

    public Set<LinkTypeReactant> getReactants() {
        return (Set<LinkTypeReactant>)((HashSet<LinkTypeReactant>)reactants).clone();
    }

    public Set<LinkTypeProduct> getProducts() {
        return (Set<LinkTypeProduct>)((HashSet<LinkTypeProduct>)products).clone();
    }

    public Set<LinkTypeModifier> getModifiers() {
        return (Set<LinkTypeModifier>)((HashSet<LinkTypeModifier>)modifiers).clone();
    }

    public void insertLinkReactionCompartment(LinkReactionCompartment pass, LinkTypeReactionCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkTypeReactionCompartment = l;
    }

    public void removeLinkReactionCompartment(LinkReactionCompartment pass)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkTypeReactionCompartment = null;
    }

    public LinkTypeReactionCompartment getLinkReactionCompartment() {
        return linkTypeReactionCompartment;
    }

    public RealInterval getRate() {
        return rate;
    }

    public void setRate(RealInterval rate) {
        this.rate = rate;
    }
}
