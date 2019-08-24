package Model;

import DataTypes.ModifierType;

import java.io.Serializable;
import java.util.Objects;

public class LinkTypeModifier implements Serializable {
    private Species species;
    private Reaction reaction;
    private ModifierType type;

    public LinkTypeModifier(Species species, Reaction reaction, ModifierType type) {
        this.species = species;
        this.reaction = reaction;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeModifier that = (LinkTypeModifier) o;
        return species == that.species &&
                reaction == that.reaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(species, reaction);
    }


    public ModifierType getModifierType() {
        return type;
    }

    public Species getSpecies() {
        return species;
    }

    public Reaction getReaction() {
        return reaction;
    }
}
