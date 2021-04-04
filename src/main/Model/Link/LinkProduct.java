package Model.Link;

import DataTypes.PreconditionsException;
import Model.LinkType.LinkTypeProduct;
import Model.Reaction;
import Model.Species;

import java.io.Serializable;

public class LinkProduct implements Serializable {

    public static void insertLink(Species species, Reaction reaction, int stoichiometry) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkProduct pass = new LinkProduct();
        // Creo il link
        LinkTypeProduct link = new LinkTypeProduct(species, reaction, stoichiometry);
        // Inserisco il link nei due oggetti esibendo il pass
        species.insertLinkProduct(pass, link);
        reaction.insertLinkProduct(pass, link);
    }

    public static void removeLink(LinkTypeProduct link)
            throws PreconditionsException {
        LinkProduct pass = new LinkProduct();
        link.getSpecies().removeLinkProduct(pass, link);
        link.getReaction().removeLinkProduct(pass, link);
    }
}
