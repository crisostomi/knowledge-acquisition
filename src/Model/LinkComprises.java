package Model;

import DataTypes.PreconditionsException;

public final class LinkComprises {

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
        link.getBiologicalEntity().removeLinkComprises(pass, link);
        link.getModel().removeLinkComprises(pass, link);
    }
}
