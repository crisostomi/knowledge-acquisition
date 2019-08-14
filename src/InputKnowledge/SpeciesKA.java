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

            this.handleBioEntityName(s);
            try {
                this.handleSpeciesInitialAmount(s);
            } catch (SpeciesKAInitAmountNotFoundException exc) {}

            try {
                this.handleSpeciesCompartment(s);
            } catch (SpeciesKACompartmentNotFoundException exc) {}
            this.addAdditionalKnowledge(s);
        }
    }

    private void handleSpeciesCompartment(Species s) {}

    private void handleSpeciesInitialAmount(Species s) {}
}
