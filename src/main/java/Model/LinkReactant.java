package Model;

import DataTypes.PreconditionsException;

import java.io.Serializable;

public class LinkReactant implements Serializable {

    public static void insertLink(Species species, Reaction reaction, int stoichiometry) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkReactant pass = new LinkReactant();
        // Creo il link
        LinkTypeReactant link = new LinkTypeReactant(species, reaction, stoichiometry);
        // Inserisco il link nei due oggetti esibendo il pass
        species.insertLinkReactant(pass, link);
        reaction.insertLinkReactant(pass, link);
    }

    public static void removeLink(LinkTypeReactant link)
            throws PreconditionsException {
        LinkReactant pass = new LinkReactant();
        link.getSpecies().removeLinkReactant(pass, link);
        link.getReaction().removeLinkReactant(pass, link);
    }
}
