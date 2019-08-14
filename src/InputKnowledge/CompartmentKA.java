package InputKnowledge;
import DataTypes.PreconditionsException;
import Model.*;

public class CompartmentKA extends KnowledgeAtom {

    private final Double size;

    public CompartmentKA(String id, boolean override, KnowledgeBase knowledgeBase) {
        super(id, override, knowledgeBase);
        this.size = null;
    }

    public CompartmentKA(String id, boolean override, KnowledgeBase knowledgeBase, String name) {
        super(id, override, knowledgeBase, name);
        this.size = null;
    }

    public CompartmentKA(String id, boolean override, KnowledgeBase knowledgeBase, double size) {
        super(id, override, knowledgeBase);
        this.size = size;
    }

    public CompartmentKA(String id, boolean override, KnowledgeBase knowledgeBase, String name, double size) {
        super(id, override, knowledgeBase, name);
        this.size = size;
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
       BiologicalEntity be = m.getBioEntityById(this.id);

       if (be != null && !(be instanceof Compartment)) {
           throw new PreconditionsException(
                   "The model has a BiologicalEntity with same ID that is not a Compartment"
           );
       }

       if (be == null) {
           Compartment c = new Compartment(this.getId());
           // TODO: create new link <m, c> in comprises

           this.handleBioEntityName(c);
           try {
               this.handleCompartmentSize(c);
           } catch (CompartmentKASizeNotFoundException exc) {}
           this.addAdditionalKnowledge(c);
       }
    }

    private void handleCompartmentSize(Compartment c) throws PreconditionsException {
        if (this.size == null) throw new CompartmentKASizeNotFoundException();
        if (this.size != null && c.getSize() != null && Double.compare(this.size, c.getSize()) != 0) {
            throw new SizeMismatchException(
                    "Compartment " + c.getId() + " already has size " + c.getSize() +
                            " which is different from size " + this.size + " provided by the atom"
            );
        }

        c.setSize(this.size);
    }
}
