package Model;

import Util.GsonRepellent;

import java.io.Serializable;
import java.util.Objects;

public class LinkTypeComprises implements Serializable {

    private Model model;
    private BiologicalEntity bioEntity;

    public Model getModel() {
        return model;
    }

    public BiologicalEntity getBiologicalEntity() {
        return bioEntity;
    }

    public LinkTypeComprises(Model model, BiologicalEntity bioEntity) {
        this.model = model;
        this.bioEntity = bioEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkTypeComprises that = (LinkTypeComprises) o;
        return model == that.model && bioEntity == that.bioEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, bioEntity);
    }
}
