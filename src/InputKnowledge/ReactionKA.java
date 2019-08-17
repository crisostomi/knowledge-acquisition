package InputKnowledge;

import DataTypes.*;
import Model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ReactionKA extends KnowledgeAtom {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private RealInterval rate = null;
    private boolean reversible = false;
    private RealInterval rateInv = null;
    private String compartmentId = null;
    private Set<SpeciesReference> reactants = new HashSet<>();
    private Set<SpeciesReference> products = new HashSet<>();
    private Set<ModifierReference> modifiers = new HashSet<>();


    public ReactionKA(String id, boolean override, KnowledgeBase knowledgeBase)
            throws PreconditionsException {
        super(id, override, knowledgeBase);
    }

    public ReactionKA(String id, boolean override, KnowledgeBase knowledgeBase, String name)
            throws PreconditionsException {
        super(id, override, knowledgeBase, name);
    }

    public void initializeRate(RealInterval rate) throws PreconditionsException {
        if (this.rate != null || rate == null) throw new PreconditionsException();
        this.rate = rate;

        logger.info(
                "ReactionKA:" + this.id + "set rate = " + this.rate
        );
    }

    public void setReversible() {
        this.reversible = true;
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public void initializeRateInv(RealInterval rateInv) throws PreconditionsException {
        if (this.rateInv != null || rateInv == null) throw new PreconditionsException();
        this.rateInv = rateInv;
        this.reversible = true;

        logger.info(
                "ReactionKA:" + this.id + "set rateInv = " + this.rateInv + ", set reversible = " + reversible
        );
    }

    public void initializeCompartmentId(String compartmentId) throws PreconditionsException {
        if (this.compartmentId != null || compartmentId == null) throw new PreconditionsException();
        this.compartmentId = compartmentId;

        logger.info(
                "ReactionKA:" + this.id + "set compartmentId = " + this.compartmentId
        );
    }

    public void initializeReactants(Set<SpeciesReference> reactants) throws PreconditionsException {
        if (!this.reactants.isEmpty() || reactants == null) throw new PreconditionsException();

        this.reactants.addAll(reactants);

        logger.info(
                "ReactionKA:" + this.id + "set reactants = " + this.reactants
        );
    }

    public void initializeProducts(Set<SpeciesReference> products) throws PreconditionsException {
        if (!this.products.isEmpty() || products == null) throw new PreconditionsException();

        this.products.addAll(products);

        logger.info(
                "ReactionKA:" + this.id + "set products = " + this.products
        );
    }

    public void initializeModifiers(Set<ModifierReference> modifiers) throws PreconditionsException {
        if (!this.modifiers.isEmpty() || modifiers == null) throw new PreconditionsException();

        this.modifiers.addAll(modifiers);

        logger.info(
                "ReactionKA:" + this.id + "set modifiers = " + this.modifiers
        );
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
        logger.info(
                "Consolidating model with atom..."
        );
        logger.info(
                "Searching model for Reaction " + this.id + "..."
        );
        BiologicalEntity be = m.getBioEntityById(this.id);
        if (be != null && !(be instanceof Reaction)) {
            throw new PreconditionsException(
                    "The model comprises a BiologicalEntity with same ID that is not a Reaction"
            );
        }

        Reaction r;
        if (be == null) {
            logger.info(
                    "Reaction not found"
            );
            r = new Reaction(this.id, m);
        } else {
            logger.info(
                    "Reaction found"
            );
            r = (Reaction) be;
        }

        this.handleReactionCompartment(r);
        this.handleReactionRate(r);

        if (reversible) {
            logger.info(
                    "The reaction is reversible"
            );
            r.setReversible();
            this.handleRevReactionRate(r);
        }

        this.handleReactionReactants(r);
        this.handleReactionProducts(r);
        this.handleReactionModifiers(r);

        this.handleBioEntityName(r);
        this.addAdditionalKnowledge(r);
    }

    private void handleReactionCompartment(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction compartment..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        if (this.compartmentId == null) {
            logger.info(
                    "Atom has no information about reaction compartment"
            );
            return;
        }

        Model m = r.getLinkComprises().getModel();
        LinkTypeReactionCompartment l = r.getLinkReactionCompartment();
        if (l != null) {
            logger.info(
                    "Reaction is already associated to a compartment"
            );
            Compartment c = l.getCompartment();
            if (!(c.getId().equals(this.compartmentId))) throw new CompartmentMismatchException();
        } else {

            BiologicalEntity be = m.getBioEntityById(this.compartmentId);
            if (be != null && !(be instanceof Compartment)) {
                throw new PreconditionsException(
                        "Reaction " + r.getId() +" compartment id is associated to a BiologicalEntity that is not a Compartment"
                );
            }

            Compartment comp = (Compartment)be;

            if (comp == null) {
                logger.info(
                        "There compartment is not in the model yet"
                );
                comp = new Compartment(this.compartmentId, m);
            }

            LinkReactionCompartment.insertLink(r, comp);
        }
    }

    private void handleReactionRate(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction rate..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        logger.info("Reaction rate = " + r.getRate() + ", atom rate = " + this.rate);
        if (this.rate != null) {
            if (r.getRate() != null) {
                RealInterval newRate = r.getRate().intersect(this.rate);
                r.setRate(newRate);
            } else {
                r.setRate(this.rate);
            }
        } else if (r.getRate() == null) {
            RealInterval newRate = new RealInterval(0, Double.MAX_VALUE);
            r.setRate(newRate);
        }

        logger.info("New value = " + r.getRate());
    }

    private void handleRevReactionRate(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction rateInv..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "ReversibleReaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        logger.info("Reaction rateInv = " + r.getRateInv() + ", atom rateInv = " + this.rateInv);
        if (this.rateInv != null) {
            if (r.getRateInv() != null) {
                RealInterval newRateInv = r.getRateInv().intersect(this.rateInv);
                r.setRate(newRateInv);
            } else {
                r.setRate(this.rate);
            }
        } else if (r.getRateInv() == null) {
            RealInterval newRate = new RealInterval(0, Double.MAX_VALUE);
            r.setRateInv(newRate);
        }
        logger.info("New value = " + r.getRateInv());

    }


    private void handleReactionReactants(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction reactants..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        Model m = r.getLinkComprises().getModel();
        Map<Species, Integer> existent = new HashMap<>();
        Map<String, Integer> nonExistent = new HashMap<>();

        for (SpeciesReference sr : this.reactants) {

            String speciesId = sr.getSpeciesId();
            Integer stoich = sr.getStoichiometry();

            Species s = (Species)m.getBioEntityById(speciesId);
            if (s != null) {
                existent.put(s, stoich);
            } else {
                nonExistent.put(speciesId, stoich);
            }
        }

        handleExistentReactionReactants(r, existent);
        handleNonExistentReactionReactants(r, nonExistent);

    }

    private void handleExistentReactionReactants(Reaction r, Map<Species, Integer> reactants) {
        logger.info(
                "Handling existent reaction reactants..."
        );
        for (Map.Entry<Species, Integer> entry : reactants.entrySet()) {
            Species s = entry.getKey();
            Integer stoich = entry.getValue();

            r.addReactant(s, stoich);
        }
    }

    private void handleNonExistentReactionReactants(Reaction r, Map<String, Integer> reactants) throws PreconditionsException {
        logger.info(
                "Handling non existent reaction reactants..."
        );
        for (Map.Entry<String, Integer> entry : reactants.entrySet()) {
            String s_id = entry.getKey();
            Integer stoich = entry.getValue();

            Species s = new Species(s_id, r.getLinkComprises().getModel());

            r.addReactant(s, stoich);
        }
    }


    private void handleReactionProducts(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction products..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        Model m = r.getLinkComprises().getModel();
        Map<Species, Integer> existent = new HashMap<>();
        Map<String, Integer> nonExistent = new HashMap<>();

        for (SpeciesReference sr : this.products) {

            String speciesId = sr.getSpeciesId();
            Integer stoich = sr.getStoichiometry();

            Species s = (Species)m.getBioEntityById(speciesId);
            if (s != null) {
                existent.put(s, stoich);
            } else {
                nonExistent.put(speciesId, stoich);
            }
        }

        handleExistentReactionProducts(r, existent);
        handleNonExistentReactionProducts(r, nonExistent);

    }

    private void handleExistentReactionProducts(Reaction r, Map<Species, Integer> products) {
        logger.info(
                "Handling existent reaction products..."
        );
        for (Map.Entry<Species, Integer> entry : products.entrySet()) {
            Species s = entry.getKey();
            Integer stoich = entry.getValue();

            r.addProduct(s, stoich);
        }
    }

    private void handleNonExistentReactionProducts(Reaction r, Map<String, Integer> products) throws PreconditionsException {
        logger.info(
                "Handling non existent reaction products..."
        );
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String s_id = entry.getKey();
            Integer stoich = entry.getValue();

            Species s = new Species(s_id, r.getLinkComprises().getModel());

            r.addProduct(s, stoich);
        }
    }


    private void handleReactionModifiers(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction modifiers..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        Model m = r.getLinkComprises().getModel();
        Map<Species, ModifierType> existent = new HashMap<>();
        Map<String, ModifierType> nonExistent = new HashMap<>();

        for (ModifierReference mr : this.modifiers) {

            String speciesId = mr.getSpeciesId();
            ModifierType stoich = mr.getType();

            Species s = (Species)m.getBioEntityById(speciesId);
            if (s != null) {
                existent.put(s, stoich);
            } else {
                nonExistent.put(speciesId, stoich);
            }
        }

        handleExistentReactionModifiers(r, existent);
        handleNonExistentReactionModifiers(r, nonExistent);
    }

    private void handleExistentReactionModifiers(Reaction r, Map<Species, ModifierType> modifiers) {
        logger.info(
                "Handling existent reaction modifiers..."
        );
        for (Map.Entry<Species, ModifierType> entry : modifiers.entrySet()) {
            Species s = entry.getKey();
            ModifierType type = entry.getValue();

            r.addModifier(s, type);
        }
    }

    private void handleNonExistentReactionModifiers(Reaction r, Map<String, ModifierType> modifiers) throws PreconditionsException{
        logger.info(
                "Handling non existent reaction modifiers..."
        );
        for (Map.Entry<String, ModifierType> entry : modifiers.entrySet()) {
            String s_id = entry.getKey();
            ModifierType type = entry.getValue();

            Species s = new Species(s_id, r.getLinkComprises().getModel());

            r.addModifier(s, type);
        }
    }
}
