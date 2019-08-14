package DataTypes;

import java.util.Objects;

public class ModifierReference {
    private String speciesId;
    private ModifierType type;

    public ModifierReference(String speciesId, ModifierType type){
        this.speciesId = speciesId;
        this.type = type;
    }

    public String getSpeciesId() {
        return speciesId;
    }

    public ModifierType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifierReference that = (ModifierReference) o;
        return speciesId.equals(that.speciesId) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speciesId, type);
    }
}
