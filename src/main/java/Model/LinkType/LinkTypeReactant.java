package Model.LinkType;

import java.io.Serializable;
import java.util.Objects;
import Model.Species;
import Model.Reaction;

public class LinkTypeReactant implements Serializable {
    private Species species;
    private Reaction reaction;

    private int stoichiometry;

    public LinkTypeReactant(Species species, Reaction reaction, int stoichiometry) {
        this.species = species;
        this.reaction = reaction;
        this.stoichiometry = stoichiometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeReactant that = (LinkTypeReactant) o;
        return species == that.species &&
                reaction == that.reaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(species, reaction);
    }


    public int getStoichiometry() {
        return stoichiometry;
    }

    public Species getSpecies() {
        return species;
    }

    public Reaction getReaction() {
        return reaction;
    }
}
