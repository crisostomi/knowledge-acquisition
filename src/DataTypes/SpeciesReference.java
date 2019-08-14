package DataTypes;

import java.util.Objects;

public class SpeciesReference {
    private String speciesId;
    private int stoichiometry;

    SpeciesReference(String speciesId, int stoichiometry) throws Exception{
        if (stoichiometry > 0){
            this.speciesId = speciesId;
            this.stoichiometry = stoichiometry;
        }
        else{
            throw new Exception();
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
