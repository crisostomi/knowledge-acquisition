package Model;

public class LinkTypeComprises {
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


}
