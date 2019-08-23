package Model;

import java.io.Serializable;
import java.util.Objects;

public class LinkTypeProduct implements Serializable {
    private Species species;
    private Reaction reaction;

    private int stoichiometry;

    public LinkTypeProduct(Species species, Reaction reaction, int stoichiometry) {
        this.species = species;
        this.reaction = reaction;
        this.stoichiometry = stoichiometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeProduct that = (LinkTypeProduct) o;
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
