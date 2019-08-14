package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public class Compartment extends BiologicalEntity {

    private Double size;
    private Set<LinkTypeSpeciesCompartment> linkSpeciesCompartmentSet;

    public Compartment(String id, Model m) throws PreconditionsException {
        super(id, m);
        this.size = null;
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {

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

    public Set<LinkTypeComprises> getLinkSpeciesCompartmentSet() {
        return (Set<LinkTypeComprises>)((HashSet<LinkTypeSpeciesCompartment>)linkSpeciesCompartmentSet).clone();
    }
}
