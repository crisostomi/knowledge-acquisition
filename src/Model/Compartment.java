package Model;

import DataTypes.PreconditionsException;

public class Compartment extends BiologicalEntity {

    private Double size;

    public Compartment(String id, Model m) throws PreconditionsException {
        super(id, m);
        this.size = null;
    }

    @Override
    public void override(BiologicalEntity bioEntity) throws PreconditionsException {

    }

    public Double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
