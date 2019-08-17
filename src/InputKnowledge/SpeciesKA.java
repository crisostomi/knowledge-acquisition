package InputKnowledge;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;
import Model.*;

public class SpeciesKA extends KnowledgeAtom {

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
    }

    public void initializeInitialAmount(RealInterval initialAmount) throws PreconditionsException {
        if (this.initialAmount != null || initialAmount == null) throw new PreconditionsException();
        this.initialAmount = initialAmount;
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
        BiologicalEntity be = m.getBioEntityById(this.id);

        if (be != null && !(be instanceof Species)) {
            throw new PreconditionsException(
                    "The model comprises a BiologicalEntity with same ID that is not a Species"
            );
        }

        Species s;
        if (be == null) {
            s = new Species(this.getId(), m);
        } else {
            s = (Species) be;
        }

        this.handleSpeciesInitialAmount(s);
        this.handleSpeciesCompartment(s);

        this.handleBioEntityName(s);
        this.addAdditionalKnowledge(s);
    }

    private void handleSpeciesCompartment(Species s) throws PreconditionsException {
        if (!this.id.equals(s.getId())) {
            throw new IdMismatchException(
                    "Species has different ID ("+s.getId()+") from atom ("+this.id+")"
            );
        }

        if (this.compartmentId == null) return;

        Model m = s.getLinkComprises().getModel();
        LinkTypeSpeciesCompartment l = s.getLinkSpeciesCompartment();
        if (l != null) {
            Compartment c = l.getCompartment();
            if (!(c.getId().equals(this.compartmentId))) throw new CompartmentMismatchException();
        } else {

            BiologicalEntity be = m.getBioEntityById(this.compartmentId);
            if (be != null && !(be instanceof Compartment)) {
                throw new PreconditionsException(
                        "Species " + s.getId() +" compartment id is associated to a BiologicalEntity that is not a Compartment"
                );
            }

            Compartment comp = (Compartment)be;

            if (comp == null) {
                comp = new Compartment(this.compartmentId, m);
            }

            LinkSpeciesCompartment.insertLink(s, comp);
        }
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
