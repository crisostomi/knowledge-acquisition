package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public class Model {

    public void overrideModel(Model other){

    }

    public BiologicalEntity getBioEntityById(String id){
        return null;
    }

    public void overrideEntity(BiologicalEntity other){

    }

    private Set<LinkTypeComprises> linkComprisesSet = new HashSet<LinkTypeComprises>();

    public void insertLinkComprises(LinkComprises pass, LinkTypeComprises l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkComprisesSet.add(l);
    }

    public void removeLinkComprises(LinkComprises pass, LinkTypeComprises l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkComprisesSet.remove(l);
    }

    public Set<LinkTypeComprises> getLinkAssoc() {
        return (Set<LinkTypeComprises>)((HashSet<LinkTypeComprises>)linkComprisesSet).clone();
    }
}
