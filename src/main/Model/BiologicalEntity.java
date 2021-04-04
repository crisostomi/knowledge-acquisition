package Model;

import DataTypes.PreconditionsException;
import Model.Exceptions.BiologicalEntityNotUniqueException;
import Model.Exceptions.LinkMultiplicityException;
import Model.Link.LinkComprises;
import Model.LinkType.LinkTypeAdditionalKnowledge;
import Model.LinkType.LinkTypeComprises;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public abstract class BiologicalEntity implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    protected final String id;
    protected String name;
    private Set<LinkTypeAdditionalKnowledge> additionalKnowledge = new HashSet<LinkTypeAdditionalKnowledge>();
    private LinkTypeComprises linkComprises;

    public BiologicalEntity(String id, Model m) throws PreconditionsException {
        for (LinkTypeComprises link : m.getLinkComprisesSet()) {
            if (link.getBiologicalEntity().getId().equals(id)) {
                throw new BiologicalEntityNotUniqueException(
                        "Biological entity " + id + " already exists in Model"
                );
            }
        }
        this.id = id;
        this.name = null;
        LinkComprises.insertLink(m, this);
        LOGGER.info("Created BioEntity "+id);
    }

    public abstract BiologicalEntity cloneIntoModel(Model model) throws PreconditionsException;

    public abstract void override(BiologicalEntity bioEntity) throws PreconditionsException;

    public void overrideName(BiologicalEntity bioEntity) throws PreconditionsException{
        if (this.getId().equals(bioEntity.getId())){
            this.name = bioEntity.name;
        }
        else {
            throw new PreconditionsException("The overrider and the overridee have different ids.");
        }
    }

// AdditionalKnowledge association (with attributes), of which BiologicalEntity is the only responsible. (n to n)

    public void insertLinkAdditionalKnowledge(AdditionalKnowledgeType addKnowType, String value){
        LinkTypeAdditionalKnowledge link = new LinkTypeAdditionalKnowledge(this, addKnowType, value);
        this.additionalKnowledge.add(link);
    }

    public void removeLinkAdditionalKnowledge(AdditionalKnowledgeType addKnowType, String value){
        LinkTypeAdditionalKnowledge link = new LinkTypeAdditionalKnowledge(this, addKnowType, value);
        this.additionalKnowledge.remove(link);
    }

    public Set<LinkTypeAdditionalKnowledge> getLinkAdditionalKnowledge() {
        return (Set<LinkTypeAdditionalKnowledge>)
                ((HashSet<LinkTypeAdditionalKnowledge>)additionalKnowledge).clone();
    }

// Comprises association (without attributes), of which BiologicalEntity and Model are both responsible. (n to 1)

    public void insertLinkComprises(LinkComprises pass, LinkTypeComprises l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkComprises to invoke this method");
        if (linkComprises != null) throw new LinkMultiplicityException();
        linkComprises = l;
    }

    public void removeLinkComprises(LinkComprises pass)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkComprises to invoke this method");
        linkComprises = null;
    }

    public LinkTypeComprises getLinkComprises() throws PreconditionsException{
        if (linkComprises == null) {
            throw new LinkMultiplicityException();
        }
        return linkComprises;
    }

// getters and setters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
