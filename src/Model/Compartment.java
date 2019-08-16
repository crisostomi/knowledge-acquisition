package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public class Compartment extends BiologicalEntity {

    private Double size;
    private Set<LinkTypeSpeciesCompartment> linkSpeciesCompartmentSet = new HashSet<>();
    private Set<LinkTypeReactionCompartment> linkReactionCompartmentSet = new HashSet<>();


    public Compartment(String id, Model m) throws PreconditionsException {
        super(id, m);
        this.size = null;
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {

    }

    @Override
    public void cloneIntoModel(Model model) {

    }

    public Double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void insertLinkSpeciesCompartment(LinkSpeciesCompartment pass, LinkTypeSpeciesCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkSpeciesCompartmentSet.add(l);
    }

    public void removeLinkSpeciesCompartment(LinkSpeciesCompartment pass, LinkTypeSpeciesCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkSpeciesCompartmentSet.remove(l);
    }

    public Set<LinkTypeSpeciesCompartment> getLinkSpeciesCompartmentSet() {
        return (Set<LinkTypeSpeciesCompartment>)((HashSet<LinkTypeSpeciesCompartment>)linkSpeciesCompartmentSet).clone();
    }

    public void insertLinkReactionCompartment(LinkReactionCompartment pass, LinkTypeReactionCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkReactionCompartmentSet.add(l);
    }

    public void removeLinkReactionCompartment(LinkReactionCompartment pass, LinkTypeReactionCompartment l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkReactionCompartmentSet.remove(l);
    }

    public Set<LinkTypeReactionCompartment> getLinkReactionCompartmentSet() {
        return (Set<LinkTypeReactionCompartment>)((HashSet<LinkTypeReactionCompartment>)linkReactionCompartmentSet).clone();
    }

}
