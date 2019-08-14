package DataTypes;

import java.util.Objects;


public class SpeciesReference {
    private String speciesId;
    private int stoichiometry;

    /**
     * Class that represents a reference to a species in a reaction
     * @param speciesId id of the species
     * @param stoichiometry stoichiometry of the species in the reaction
     * @throws PreconditionsException
     */

    SpeciesReference(String speciesId, int stoichiometry) throws PreconditionsException{
        if (stoichiometry > 0){
            this.speciesId = speciesId;
            this.stoichiometry = stoichiometry;
        }
        else{
            throw new PreconditionsException("Stoichiometry must be greater than zero.");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(speciesId, stoichiometry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeciesReference that = (SpeciesReference) o;
        return stoichiometry == that.stoichiometry &&
                Objects.equals(speciesId, that.speciesId);
    }
}
