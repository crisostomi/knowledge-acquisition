package InputKnowledge;
import DataTypes.PreconditionsException;
import Model.*;

import java.util.logging.Logger;

public class CompartmentKA extends KnowledgeAtom {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
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

        logger.info(
                "CompartmentKA:" + this.id + ",\n\tset size = " + size
        );
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
        logger.info(
                "Consolidating Model with atom..."
        );

        logger.info(
                "Searching the model for Compartment " + this.id
        );
       BiologicalEntity be = m.getBioEntityById(this.id);


       if (be != null && !(be instanceof Compartment)) {
           throw new PreconditionsException(
                   "The model comprises a BiologicalEntity with same ID that is not a Compartment"
           );
       }

       Compartment c;
       if (be == null) {
           logger.info(
                   "Compartment not found"
           );
           c = new Compartment(this.getId(), m);
       } else {
           logger.info(
                   "Compartment found"
           );
           c = (Compartment) be;
       }

       logger.info(
               "Consolidating atom information..."
       );
        this.handleCompartmentSize(c);

        this.handleBioEntityName(c);
        this.addAdditionalKnowledge(c);
    }

    private void handleCompartmentSize(Compartment c) throws PreconditionsException {
        logger.info(
                "Handling Compartment " + this.id + " size..."
        );

        if (this.size != null && c.getSize() != null && Double.compare(this.size, c.getSize()) != 0) {
            throw new SizeMismatchException(
                    "Compartment " + c.getId() + " already has size " + c.getSize() +
                            " which is different from size " + this.size + " provided by the atom"
            );
        }

        if (this.size != null) {
            c.setSize(this.size);
            logger.info(
                    "Compartment size set"
            );
        } else {
            logger.info(
                    "Atom has no information about size"
            );
        }
    }
}
