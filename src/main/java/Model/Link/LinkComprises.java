package Model.Link;

import DataTypes.PreconditionsException;
import Model.LinkType.LinkTypeComprises;
import Model.Model;
import Model.BiologicalEntity;

import java.io.Serializable;

public final class LinkComprises implements Serializable {

    private LinkComprises(){}

    public static void insertLink(Model model, BiologicalEntity bioEntity) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkComprises pass = new LinkComprises();
        // Creo il link
        LinkTypeComprises link = new LinkTypeComprises(model, bioEntity);
        // Inserisco il link nei due oggetti esibendo il pass
        model.insertLinkComprises(pass, link);
        bioEntity.insertLinkComprises(pass, link);
    }

    public static void removeLink(LinkTypeComprises link)
            throws PreconditionsException {
        LinkComprises pass = new LinkComprises();
        link.getBiologicalEntity().removeLinkComprises(pass);
        link.getModel().removeLinkComprises(pass, link);
    }
}
