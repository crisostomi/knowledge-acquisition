package InputKnowledge;
import DataTypes.PreconditionsException;
import Model.*;

public class CompartmentKA extends KnowledgeAtom {

    private Double size = null;

    public CompartmentKA(String id, boolean override, KnowledgeBase knowledgeBase)
            throws PreconditionsException {
        super(id, override, knowledgeBase);
    }

    public CompartmentKA(String id, boolean override, KnowledgeBase knowledgeBase, String name)
            throws PreconditionsException {
        super(id, override, knowledgeBase, name);
    }

    public void initializeSize(Double size)
            throws PreconditionsException {
        if (this.size != null || size == null) throw new PreconditionsException();

        this.size = size;
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
       BiologicalEntity be = m.getBioEntityById(this.id);

       if (be != null && !(be instanceof Compartment)) {
           throw new PreconditionsException(
                   "The model comprises a BiologicalEntity with same ID that is not a Compartment"
           );
       }

       Compartment c;
       if (be == null) {
           c = new Compartment(this.getId(), m);
       } else {
           c = (Compartment) be;
       }

        this.handleCompartmentSize(c);

        this.handleBioEntityName(c);
        this.addAdditionalKnowledge(c);
    }

    private void handleCompartmentSize(Compartment c) throws PreconditionsException {
        if (this.size != null && c.getSize() != null && Double.compare(this.size, c.getSize()) != 0) {
            throw new SizeMismatchException(
                    "Compartment " + c.getId() + " already has size " + c.getSize() +
                            " which is different from size " + this.size + " provided by the atom"
            );
        }

        if (this.size != null) c.setSize(this.size);
    }
}
