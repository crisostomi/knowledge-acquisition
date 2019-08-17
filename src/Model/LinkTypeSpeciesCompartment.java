package Model;

import Util.GsonRepellent;

public class LinkTypeSpeciesCompartment {
    @GsonRepellent
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

    @Override
    public String toString() {
        return "LinkTypeSpeciesCompartment{" +
                "species=" + species +
                '}';
    }
}
