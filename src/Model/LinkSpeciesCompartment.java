package Model;

import DataTypes.PreconditionsException;

import java.io.Serializable;

public final class LinkSpeciesCompartment implements Serializable {

    private LinkSpeciesCompartment() {}

    public static void insertLink(Species species, Compartment compartment) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkSpeciesCompartment pass = new LinkSpeciesCompartment();
        // Creo il link
        LinkTypeSpeciesCompartment link = new LinkTypeSpeciesCompartment(species, compartment);
        // Inserisco il link nei due oggetti esibendo il pass
        species.insertLinkSpeciesCompartment(pass, link);
        compartment.insertLinkSpeciesCompartment(pass, link);
    }

    public static void removeLink(LinkTypeSpeciesCompartment link)
            throws PreconditionsException {
        LinkSpeciesCompartment pass = new LinkSpeciesCompartment();
        link.getSpecies().removeLinkSpeciesCompartment(pass);
        link.getCompartment().removeLinkSpeciesCompartment(pass, link);
    }
}
