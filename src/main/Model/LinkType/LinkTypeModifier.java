package Model.LinkType;

import DataTypes.ModifierType;
import Model.Species;
import Model.Reaction;
import java.io.Serializable;
import java.util.Objects;

public class LinkTypeModifier implements Serializable {
    private Species species;
    private Reaction reaction;

    private ModifierType modifierType;

    public LinkTypeModifier(Species species, Reaction reaction, ModifierType modifierType) {
        this.species = species;
        this.reaction = reaction;
        this.modifierType = modifierType;
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
        return modifierType;
    }

    public Species getSpecies() {
        return species;
    }

    public Reaction getReaction() {
        return reaction;
    }
}
