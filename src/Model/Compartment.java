package Model;

import DataTypes.PreconditionsException;

public class Compartment extends BiologicalEntity {

    private double size;

    public Compartment(String id, String name) {
        super(id, name);
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {

    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
