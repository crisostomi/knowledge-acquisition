package Model;

import DataTypes.PreconditionsException;
import Model.Exceptions.LinkMultiplicityException;
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
        // una delle due non partecipa a reazioni
        assert ( other.getLinkReactantSet().isEmpty() && other.getLinkProductSet().isEmpty() &&
                other.getLinkModifierSet().isEmpty() ) ^ ( this.getLinkReactantSet().isEmpty() && this.getLinkProductSet().isEmpty() &&
                this.getLinkModifierSet().isEmpty());

        // se è l'altra a non partecipare a reazioni
        if ( other.getLinkReactantSet().isEmpty() && other.getLinkProductSet().isEmpty() &&
                other.getLinkModifierSet().isEmpty() ) {
            Double oldAbundance = this.abundance;
            try {
                this.setAbundance(other.getAbundance());
                LinkComprises.removeLink(other.getLinkComprises());
            } catch (LinkMultiplicityException exc) {
                this.abundance = oldAbundance;
            }
        }
        else{
            other.merge(this);
        }
    }

    public boolean isQuantitative(){
        return this.getId().contains("ENSP");
    }

}
