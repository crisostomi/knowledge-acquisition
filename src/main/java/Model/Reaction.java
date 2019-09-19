package Model;

import DataTypes.ModifierType;
import DataTypes.PreconditionsException;
import DataTypes.RateParameter;
import DataTypes.RealInterval;
import Model.Link.*;
import Model.LinkType.LinkTypeModifier;
import Model.LinkType.LinkTypeProduct;
import Model.LinkType.LinkTypeReactant;
import Model.LinkType.LinkTypeReactionCompartment;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Set;

public class Reaction extends BiologicalEntity {

    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private Map<RateParameter, RealInterval> rateParameters = new HashMap<>();
    private Map<RateParameter, RealInterval> rateInvParameters = new HashMap<>();
    private boolean reversible = false;

    private Set<LinkTypeModifier> linkTypeModifierSet = new HashSet<>();
    private Set<LinkTypeProduct> linkTypeProductSet = new HashSet<>();
    private Set<LinkTypeReactant> linkTypeReactantSet = new HashSet<>();
    private LinkTypeReactionCompartment linkTypeReactionCompartment;

    public Reaction(String id, Model m) throws PreconditionsException {
        super(id, m);
        for(RateParameter rateParam: RateParameter.values()){
            rateParameters.put(rateParam, new RealInterval(0, Double.MAX_VALUE));
        }
    }

    public Reaction(String id, Model m, boolean reversible) throws PreconditionsException {
        super(id, m);
        for(RateParameter rateParam: RateParameter.values()){
            rateParameters.put(rateParam, new RealInterval(0, Double.MAX_VALUE));
        }
        this.reversible = reversible;
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
            this.setRateParameters(other.getRateParameters());
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }

    @Override
    public Reaction cloneIntoModel(Model model) throws PreconditionsException {
        Reaction reaction = new Reaction(this.getId(), model);
        reaction.setName(this.getName());
        reaction.setRateParameters(this.getRateParameters());
        LinkComprises.insertLink(model, reaction);

        BiologicalEntity comp = model.getBioEntityById(reaction.getLinkReactionCompartment().getCompartment().getId());
        if ( comp == null || !(comp instanceof Compartment)) {
            comp = this.getLinkReactionCompartment().getCompartment().cloneIntoModel(model);
        }
        LinkReactionCompartment.insertLink(reaction, (Compartment) comp);

        for ( LinkTypeReactant linkReactant: this.getLinkReactantSet()){
            Species reactant = linkReactant.getSpecies();
            BiologicalEntity destinationModelSpecies = model.getBioEntityById(this.getId());
            // if not found:
            if ( destinationModelSpecies == null || !(destinationModelSpecies instanceof Reaction) ){
                destinationModelSpecies = reactant.cloneIntoModel(model);
            }
            LinkReactant.insertLink((Species) destinationModelSpecies, this, linkReactant.getStoichiometry());
        }

        for ( LinkTypeProduct linkProduct: this.getLinkProductSet()){
            Species product = linkProduct.getSpecies();
            BiologicalEntity destinationModelSpecies = model.getBioEntityById(this.getId());
            // if not found:
            if ( destinationModelSpecies == null || !(destinationModelSpecies instanceof Reaction) ){
                destinationModelSpecies = product.cloneIntoModel(model);
            }
            LinkProduct.insertLink((Species) destinationModelSpecies, this, linkProduct.getStoichiometry());
        }

        for ( LinkTypeModifier linkModifier: this.getLinkModifierSet()){
            Species modifier = linkModifier.getSpecies();
            BiologicalEntity destinationModelSpecies = model.getBioEntityById(this.getId());
            // if not found:
            if ( destinationModelSpecies == null || !(destinationModelSpecies instanceof Reaction) ){
                destinationModelSpecies = modifier.cloneIntoModel(model);
            }
            LinkModifier.insertLink((Species) destinationModelSpecies, this, linkModifier.getModifierType());
        }
        return reaction;
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

    public Set<Species> getReactants(){
        Set<Species> reactants = new HashSet<>();
        for (LinkTypeReactant linkTypeReactant: this.linkTypeReactantSet){
            reactants.add(linkTypeReactant.getSpecies());
        }
        return reactants;
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

    public Set<Species> getProducts(){
        Set<Species> products = new HashSet<>();
        for (LinkTypeProduct linkTypeProduct: this.linkTypeProductSet){
            products.add(linkTypeProduct.getSpecies());
        }
        return products;
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

    public Set<Species> getModifiers(){
        Set<Species> modifiers = new HashSet<>();
        for (LinkTypeModifier linkTypeModifier: this.linkTypeModifierSet){
            modifiers.add(linkTypeModifier.getSpecies());
        }
        return modifiers;
    }

// getters and setters

    public RealInterval getRate(RateParameter rateParam) {
        return this.rateParameters.get(rateParam);
    }

    public void setRate(RateParameter rateParam, RealInterval rate) {

        this.rateParameters.put(rateParam, rate);
    }

    public int getSpeciesStoich(Species species){
        for (LinkTypeReactant link: this.getLinkReactantSet() ){
            if (link.getSpecies().equals(species)){
                return link.getStoichiometry();
            }
        }
        for (LinkTypeProduct link: this.getLinkProductSet()){
            if (link.getSpecies().equals(species)){
                return link.getStoichiometry();
            }
        }
        return 0;
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public RealInterval getRateInv(RateParameter rateParam) throws PreconditionsException {
        if (this.reversible) return this.rateParameters.get(rateParam);
        else throw new PreconditionsException(
                "Reaction " + this.id + " is not reversible"
        );
    }

    public void setRateInv(RateParameter rateParam, RealInterval rate) throws PreconditionsException{
        if (this.reversible) this.rateParameters.put(rateParam, rate);
        else throw new PreconditionsException(
                "Reaction " + this.getId() + " is not reversible"
        );
    }

    public Map<RateParameter, RealInterval> getRateParameters() {
        return rateParameters;
    }

    public void setRateParameters(Map<RateParameter, RealInterval> rateParameters) {
        this.rateParameters = rateParameters;
    }

    public Set<Species> getInvolvedSpecies(){
        Set<Species> involvedSpecies = new HashSet<>();
        for (LinkTypeReactant linkReactant : this.getLinkReactantSet()) {
            Species s = linkReactant.getSpecies();
            involvedSpecies.add(s);
        }

        for (LinkTypeModifier linkModifier : this.getLinkModifierSet()) {
            Species s = linkModifier.getSpecies();
            involvedSpecies.add(s);
        }

        if (this.isReversible()) {
            for (LinkTypeProduct linkProduct : this.getLinkProductSet()) {
                Species s = linkProduct.getSpecies();
                involvedSpecies.add(s);
            }
        }
        return involvedSpecies;
    }

    public boolean isComplex(){
        return !linkTypeModifierSet.isEmpty();
    }

}
