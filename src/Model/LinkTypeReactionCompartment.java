package Model;

import Util.GsonRepellent;

public class LinkTypeReactionCompartment {
    @GsonRepellent
    private Reaction reaction;
    private Compartment compartment;

    public LinkTypeReactionCompartment(Reaction reaction, Compartment compartment) {
        this.reaction = reaction;
        this.compartment = compartment;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public Compartment getCompartment() {
        return compartment;
    }

    @Override
    public String toString() {
        return "LinkTypeReactionCompartment{" +
                "reaction=" + reaction +
                '}';
    }
}
