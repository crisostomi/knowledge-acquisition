package DataTypes;

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

}
