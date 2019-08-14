package InputKnowledge;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;
import Model.*;

public class SpeciesKA extends KnowledgeAtom {

    private String compartmentId = null;
    private RealInterval initialAmount = null;

    public SpeciesKA(String id, boolean override, KnowledgeBase knowledgeBase) {
        super(id, override, knowledgeBase);
    }

    public SpeciesKA(String id, boolean override, KnowledgeBase knowledgeBase, String name) {
        super(id, override, knowledgeBase, name);
    }

    public void initializeCompartmentId(String compartmentId) throws PreconditionsException {
        if (this.compartmentId != null) throw new PreconditionsException();
        this.compartmentId = compartmentId;
    }

    public void initializeInitialAmount(RealInterval initialAmount) throws PreconditionsException {
        if (this.initialAmount != null) throw new PreconditionsException();
        this.initialAmount = initialAmount;
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
        BiologicalEntity be = m.getBioEntityById(this.id);

        if (be != null && !(be instanceof Species)) {
            throw new PreconditionsException(
                    "The model has a BiologicalEntity with same ID that is not a Species"
            );
        }

        if (be == null) {
            Species s = new Species(this.getId());
            // TODO: create new link <m, c> in comprises

            this.handleSpeciesInitialAmount(s);
            this.handleSpeciesCompartment(s);

            this.handleBioEntityName(s);
            this.addAdditionalKnowledge(s);
        }
    }

    private void handleSpeciesCompartment(Species s) throws PreconditionsException {
        if (!this.id.equals(s.getId())) {
            throw new IdMismatchException(
                    "Species has different ID ("+s.getId()+") from atom ("+this.id+")"
            );
        }

        // TODO: rest of method (need link in Compartment)

    }

    private void handleSpeciesInitialAmount(Species s) throws PreconditionsException {
        if (!this.id.equals(s.getId())) {
            throw new IdMismatchException(
                    "Species has different ID ("+s.getId()+") from atom ("+this.id+")"
            );
        }

        if (this.initialAmount != null) {
            if (s.getInitialAmount() != null) {
                RealInterval newInitalAmount = s.getInitialAmount().intersect(this.initialAmount);
                s.setInitialAmount(newInitalAmount);
            } else {
                s.setInitialAmount(this.initialAmount);
            }
        } else if (s.getInitialAmount() == null) {
            RealInterval newInitalAmount = new RealInterval(0, Double.MAX_VALUE);
            s.setInitialAmount(newInitalAmount);
        }
    }
}