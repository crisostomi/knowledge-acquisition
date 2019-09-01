package Model;

import DataTypes.ModifierType;
import DataTypes.PreconditionsException;

import java.io.Serializable;

public class LinkModifier implements Serializable {

    public static void insertLink(Species species, Reaction reaction, ModifierType modifierType) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkModifier pass = new LinkModifier();
        // Creo il link
        LinkTypeModifier link = new LinkTypeModifier(species, reaction, modifierType);
        // Inserisco il link nei due oggetti esibendo il pass
        species.insertLinkModifier(pass, link);
        reaction.insertLinkModifier(pass, link);
    }

    public static void removeLink(LinkTypeModifier link)
            throws PreconditionsException {
        LinkModifier pass = new LinkModifier();
        link.getSpecies().removeLinkModifier(pass, link);
        link.getReaction().removeLinkModifier(pass, link);
    }
}
