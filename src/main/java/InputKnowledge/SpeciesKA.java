package InputKnowledge;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;
import Model.*;

import java.util.logging.Logger;

public class SpeciesKA extends KnowledgeAtom {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String compartmentId = null;
    private RealInterval initialAmount = null;

    public SpeciesKA(String id, boolean override, KnowledgeBase knowledgeBase)
            throws PreconditionsException {
        super(id, override, knowledgeBase);
    }

    public SpeciesKA(String id, boolean override, KnowledgeBase knowledgeBase, String name)
            throws PreconditionsException {
        super(id, override, knowledgeBase, name);
    }

    public void initializeCompartmentId(String compartmentId) throws PreconditionsException {
        if (this.compartmentId != null || compartmentId == null) throw new PreconditionsException();
        this.compartmentId = compartmentId;

        logger.info(
                "SpeciesKA:" + this.id + ",\n\tset compartmentId = " + compartmentId
        );
    }

    public void initializeInitialAmount(RealInterval initialAmount) throws PreconditionsException {
        if (this.initialAmount != null || initialAmount == null) throw new PreconditionsException();
        this.initialAmount = initialAmount;

        logger.info(
                "SpeciesKA:" + this.id + ",\n\tset initialAmount = " + initialAmount
        );
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
        logger.info(
                "Consolidating Model with atom..."
        );

        logger.info(
                "Searching the model for Species " + this.id
        );

        BiologicalEntity be = m.getBioEntityById(this.id);

        if (be != null && !(be instanceof Species)) {
            throw new PreconditionsException(
                    "The model comprises a BiologicalEntity with same ID that is not a Species"
            );
        }

        Species s;
        if (be == null) {
            logger.info(
                    "Species not found"
            );
            s = new Species(this.getId(), m);
        } else {
            logger.info(
                    "Species found"
            );
            s = (Species) be;
        }

        this.handleSpeciesCompartment(s);
        this.handleSpeciesInitialAmount(s);

        this.handleBioEntityName(s);
        this.addAdditionalKnowledge(s);
    }

    protected void handleSpeciesCompartment(Species s) throws PreconditionsException {
        logger.info(
                "Handling species compartment..."
        );
        if (!this.id.equals(s.getId())) {
            throw new IdMismatchException(
                    "Species has different ID ("+s.getId()+") from atom ("+this.id+")"
            );
        }

        if (this.compartmentId == null) {
            logger.info("Atom has no information about species compartment");
            return;
        }

        Model m = s.getLinkComprises().getModel();
        LinkTypeSpeciesCompartment l = s.getLinkSpeciesCompartment();
        if (l != null) {
            Compartment c = l.getCompartment();
            logger.info(
                    "Species is already associated to a compartment"
            );
            if (!(c.getId().equals(this.compartmentId))) throw new CompartmentMismatchException();
        } else {
            logger.info(
                    "Species is not associated to a compartment yet"
            );
            BiologicalEntity be = m.getBioEntityById(this.compartmentId);
            if (be != null && !(be instanceof Compartment)) {
                throw new PreconditionsException(
                        "Species " + s.getId() +" compartment id is associated to a BiologicalEntity that is not a Compartment"
                );
            }

            Compartment comp = (Compartment)be;

            if (comp == null) {
                logger.info(
                        "The compartment is not in the model yet"
                );
                comp = new Compartment(this.compartmentId, m);
            }

            LinkSpeciesCompartment.insertLink(s, comp);
        }
    }

    protected void handleSpeciesInitialAmount(Species s) throws PreconditionsException {
        logger.info(
                "Handling initial amount..."
        );
        if (!this.id.equals(s.getId())) {
            throw new IdMismatchException(
                    "Species has different ID ("+s.getId()+") from atom ("+this.id+")"
            );
        }

        logger.info("Species initial amount = " + s.getInitialAmount() +
                ", atom initial amount = " + this.initialAmount);
        if (this.initialAmount != null) {
            RealInterval newInitalAmount = s.getInitialAmount().intersect(this.initialAmount);
            s.setInitialAmount(newInitalAmount);
        }

        logger.info("New value = "+ s.getInitialAmount());
    }

}

