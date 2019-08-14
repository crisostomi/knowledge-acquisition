package Model;

public class LinkTypeReactionCompartment {
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
