package Model.Link;

import DataTypes.PreconditionsException;
import Model.Compartment;
import Model.LinkType.LinkTypeReactionCompartment;
import Model.Reaction;

import java.io.Serializable;

public final class LinkReactionCompartment implements Serializable {

    private LinkReactionCompartment() {}

    public static void insertLink(Reaction reaction, Compartment compartment) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkReactionCompartment pass = new LinkReactionCompartment();
        // Creo il link
        LinkTypeReactionCompartment link = new LinkTypeReactionCompartment(reaction, compartment);
        // Inserisco il link nei due oggetti esibendo il pass
        reaction.insertLinkReactionCompartment(pass, link);
        compartment.insertLinkReactionCompartment(pass, link);
    }

    public static void removeLink(LinkTypeReactionCompartment link)
            throws PreconditionsException {
        LinkReactionCompartment pass = new LinkReactionCompartment();
        link.getReaction().removeLinkReactionCompartment(pass);
        link.getCompartment().removeLinkReactionCompartment(pass, link);
    }
}
