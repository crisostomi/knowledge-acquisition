package Model;

import DataTypes.PreconditionsException;
import Util.GsonRepellent;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Compartment extends BiologicalEntity implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private Double size;

    private Set<LinkTypeSpeciesCompartment> linkSpeciesCompartmentSet = new HashSet<>();
    private Set<LinkTypeReactionCompartment> linkReactionCompartmentSet = new HashSet<>();


    public Compartment(String id, Model m) throws PreconditionsException {
        super(id, m);
        this.size = null;
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {
        if (this.getId().equals(bioEntity.getId()) && bioEntity instanceof Compartment){
            this.overrideName(bioEntity);
            this.overrideSize((Compartment) bioEntity);
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids or classes.");
        }
    }

    public void overrideSize(Compartment comp) throws PreconditionsException{
        if (this.getId().equals(comp.getId())){
            this.size = comp.getSize();
        }
        else{
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }

    @Override
    public Compartment cloneIntoModel(Model model) throws PreconditionsException {
        Compartment compartment = new Compartment(this.getId(), model);
        if (this.getName() != null){
            compartment.setName(this.getName());
        }
        if (this.getSize() != null){
            compartment.setSize(this.getSize());
        }
        LinkComprises.insertLink(model, compartment);
        return compartment;
    }


// SpeciesCompartment association, of which Compartment and Species are both responsibles, 0/1 to n

    public void insertLinkSpeciesCompartment(LinkSpeciesCompartment pass, LinkTypeSpeciesCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkSpeciesCompartmentSet.add(l);
    }

    public void removeLinkSpeciesCompartment(LinkSpeciesCompartment pass, LinkTypeSpeciesCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkSpeciesCompartment to invoke this method");
        linkSpeciesCompartmentSet.remove(l);
    }

    public Set<LinkTypeSpeciesCompartment> getLinkSpeciesCompartmentSet() {
        return (Set<LinkTypeSpeciesCompartment>)((HashSet<LinkTypeSpeciesCompartment>)linkSpeciesCompartmentSet).clone();
    }

// ReactionCompartment association, of which Compartment and Reaction are both responsibles, 0/1 to n

    public void insertLinkReactionCompartment(LinkReactionCompartment pass, LinkTypeReactionCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkReactionSpeciesCompartment to invoke this method");
        linkReactionCompartmentSet.add(l);
    }

    public void removeLinkReactionCompartment(LinkReactionCompartment pass, LinkTypeReactionCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkReactionSpeciesCompartment to invoke this method");
        linkReactionCompartmentSet.remove(l);
    }

    public Set<LinkTypeReactionCompartment> getLinkReactionCompartmentSet() {
        return (Set<LinkTypeReactionCompartment>)((HashSet<LinkTypeReactionCompartment>)linkReactionCompartmentSet).clone();
    }


// getters and setters

    public Double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
