package Model.LinkType;

import Model.Model;
import Model.Reaction;
import Model.Compartment;

import java.io.Serializable;

public class LinkTypeReactionCompartment implements Serializable {
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
}
