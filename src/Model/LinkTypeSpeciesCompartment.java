package Model;

public class LinkTypeSpeciesCompartment {
    private Species species;
    private Compartment compartment;


    public LinkTypeSpeciesCompartment(Species species, Compartment compartment) {
        this.species = species;
        this.compartment = compartment;
    }

    public Species getSpecies() {
        return species;
    }

    public Compartment getCompartment() {
        return compartment;
    }
}
