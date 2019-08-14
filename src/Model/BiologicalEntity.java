package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public abstract class BiologicalEntity {

    private final String id; //unique
    private String name;
    private Set<LinkTypeAdditionalKnowledge> additionalKnowledge = new HashSet<LinkTypeAdditionalKnowledge>();
    private LinkTypeComprises linkComprises;

    private static Set<String> idsInUse = new HashSet<>();


    public BiologicalEntity(String id, Model m) throws PreconditionsException {
        if (idsInUse.contains(id)) {
            throw new BiologicalEntityNotUniqueException(
                    "Biological entity " + id + " already exists"
            );
        }
        this.id = id;
        this.name = null;

        LinkComprises.insertLink(m, this);
    }

    public void insertLinkAdditionalKnowledge(AdditionalKnowledgeType addKnowType, String value){
        LinkTypeAdditionalKnowledge link = new LinkTypeAdditionalKnowledge(this, addKnowType, value);
        this.additionalKnowledge.add(link);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public abstract void override(BiologicalEntity bioEntity) throws PreconditionsException;

    public Set<LinkTypeAdditionalKnowledge> getLinkAdditionalKnowledge() {
        return (Set<LinkTypeAdditionalKnowledge>)
                ((HashSet<LinkTypeAdditionalKnowledge>)additionalKnowledge).clone();
    }

    public void overrideName(BiologicalEntity bioEntity) throws PreconditionsException{
        if (this.getId().equals(bioEntity.getId())){
            this.name = bioEntity.name;
        }
        else {
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }


    public void insertLinkComprises(LinkComprises pass, LinkTypeComprises l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        if (linkComprises != null) throw new LinkMultiplicityException();
        linkComprises = l;
    }

    public void removeLinkComprises(LinkComprises pass)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkComprises = null;
    }

    public LinkTypeComprises getLinkComprises() throws PreconditionsException{
        if (linkComprises == null) throw new LinkMultiplicityException();
        return linkComprises;
    }
}
