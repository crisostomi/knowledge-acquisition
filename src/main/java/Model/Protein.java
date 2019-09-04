package Model;

import DataTypes.PreconditionsException;
import Model.Link.LinkComprises;
import Model.LinkType.LinkTypeComprises;

import java.util.HashSet;
import java.util.Set;

public class Protein extends Species {
    private Double abundance;
    private Set<String> externalIds = new HashSet<>();

    public Protein(String id, Model m) throws PreconditionsException {
        super(id, m);
    }

    public Double getAbundance() {
        return abundance;
    }

    public void setAbundance(Double abundance) {
        this.abundance = abundance;
    }

    public void addExternalId(String id){
        this.externalIds.add(id);
    }

    public Set<String> getExternalIds(){
        return (Set<String>)((HashSet<String>)externalIds).clone() ;
    }

    public void merge(Protein other) throws PreconditionsException {
        assert ( other.getLinkReactantSet().isEmpty() && other.getLinkProductSet().isEmpty() &&
                other.getLinkModifierSet().isEmpty() ) ^ ( this.getLinkReactantSet().isEmpty() && this.getLinkProductSet().isEmpty() &&
                this.getLinkModifierSet().isEmpty());
        if ( other.getLinkReactantSet().isEmpty() && other.getLinkProductSet().isEmpty() &&
                other.getLinkModifierSet().isEmpty() ) {
            this.setAbundance(other.getAbundance());
            Model model = other.getLinkComprises().getModel();
            LinkTypeComprises linkTypeComprises = new LinkTypeComprises(model, other);
            LinkComprises.removeLink(linkTypeComprises);
        }
        else{
            other.merge(this);
        }
    }

}
