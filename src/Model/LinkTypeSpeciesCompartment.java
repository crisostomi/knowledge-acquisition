package Model;

import Util.GsonRepellent;

import java.io.Serializable;

public class LinkTypeSpeciesCompartment implements Serializable {
    private Species species;

    @GsonRepellent
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
