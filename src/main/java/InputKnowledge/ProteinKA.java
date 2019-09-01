package InputKnowledge;

import DataTypes.PreconditionsException;
import Model.Model;
import Model.Species;
import Model.BiologicalEntity;
import Model.Protein;

import java.util.logging.Logger;

public class ProteinKA extends SpeciesKA {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String externalId = null;



    private Double abundance = null;

    public ProteinKA(String id, boolean override, KnowledgeBase knowledgeBase) throws PreconditionsException {
        super(id, override, knowledgeBase);
    }

    public ProteinKA(String id, boolean override, KnowledgeBase knowledgeBase, String name) throws PreconditionsException {
        super(id, override, knowledgeBase, name);
    }

    public void initializeExternalId(String externalId) throws PreconditionsException {
        if (this.externalId != null || externalId == null) throw new PreconditionsException();
            this.externalId = externalId;

        logger.info(
                "ProteinKA:" + this.id + ",\n\tset externalId = " + externalId
        );
    }

    public void initializeAbundance(Double abundance) throws PreconditionsException {
        if (this.abundance != null || abundance == null) throw new PreconditionsException();
        this.abundance = abundance;

        logger.info(
                "ProteinKA:" + this.id + ",\n\tset abundance = " + abundance
        );
    }

    public void handleProteinAbundances(Protein p) throws IdMismatchException {
        logger.info(
                "Handling protein abundances..."
        );
        if (!this.id.equals(p.getId())) {
            throw new IdMismatchException(
                    "Species has different ID ("+p.getId()+") from atom ("+this.id+")"
            );
        }

        logger.info("Species abundance = " + p.getAbundance() +
                ", atom abundance = " + this.abundance);
        if (this.abundance != null) {
            Double newAbundance = this.abundance;
            p.setAbundance(newAbundance);
        }
        logger.info("New value = "+ p.getAbundance());
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

        Protein p;
        if (be == null) {
            logger.info(
                    "Species not found"
            );
            p = new Protein(this.getId(), m);
        } else {
            logger.info(
                    "Species found"
            );
            p = (Protein) be;
        }

        this.handleSpeciesCompartment(p);
        this.handleSpeciesInitialAmount(p);
        this.handleSpeciesBounds(p);

        this.handleBioEntityName(p);
        this.addAdditionalKnowledge(p);
        this.handleProteinAbundances(p);
        p.addExternalId(this.externalId);
    }


}
