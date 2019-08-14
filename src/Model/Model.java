package Model;

import DataTypes.PreconditionsException;
import java.util.HashSet;
import java.util.Set;

public class Model {

    private Set<LinkTypeComprises> linkComprisesSet = new HashSet<>();

   public void overrideModel(Model other){
//        for (LinkTypeComprises linkComprises:this.getLinkComprisesSet()){
//            this.overrideEntity(linkComprises.getBiologicalEntity());
//        }
    }

   public BiologicalEntity getBioEntityById(String id){
        return null;
   }
//
//    public void overrideEntity(BiologicalEntity other){
//        Boolean exists = false;
//        for (LinkTypeComprises <mod_be,model>:this.comprises) do {
//            if (mod_be.id == be.id) then {
//                mod_be.override(be)
//                exists = true
//            }
//        }
//
//        if (not exists) then
//                new_model_be = be.clone()
//        create new link <this, new_model_be> in comprises
//    }


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

    public Set<LinkTypeComprises> getLinkComprisesSet() {
        return (Set<LinkTypeComprises>)((HashSet<LinkTypeComprises>)linkComprisesSet).clone();
    }
}
