package InputKnowledge;

import DataTypes.*;
import Model.*;
import Model.Link.LinkModifier;
import Model.Link.LinkProduct;
import Model.Link.LinkReactant;
import Model.Link.LinkReactionCompartment;
import Model.LinkType.LinkTypeReactionCompartment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ReactionKA extends KnowledgeAtom {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Map<RateParameter, RealInterval> rateParameters = new HashMap<>();
    private boolean reversible = false;
    private Map<RateParameter, RealInterval> rateInvParameters = new HashMap<>();
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

    public void initializeRateParam(RateParameter rateParam, RealInterval rate) throws PreconditionsException {
        if (this.rateParameters.get(rateParam) != null || rate == null) throw new PreconditionsException();
        rateParameters.put(rateParam, rate);

        logger.info(
                "ReactionKA:" + this.id + ",\n\tset rateParam"+rateParam+"= " + rate
        );
    }

    public void setReversible() {
        this.reversible = true;
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public void initializeRateInvParam(RateParameter rateParam, RealInterval rate) throws PreconditionsException {
        if (this.rateInvParameters.get(rateParam) != null || rate == null) throw new PreconditionsException();
        rateInvParameters.put(rateParam, rate);

        logger.info(
                "ReactionKA:" + this.id + ",\n\tset rateParam"+rateParam+"= " + rate
        );
    }

    public void initializeCompartmentId(String compartmentId) throws PreconditionsException {
        if (this.compartmentId != null || compartmentId == null) throw new PreconditionsException();
        this.compartmentId = compartmentId;

        logger.info(
                "ReactionKA:" + this.id + ", \n\tset compartmentId = " + this.compartmentId
        );
    }

    public void initializeReactants(Set<SpeciesReference> reactants) throws PreconditionsException {
        if (!this.reactants.isEmpty() || reactants == null) throw new PreconditionsException();

        this.reactants.addAll(reactants);

        logger.info(
                "ReactionKA:" + this.id + ", \n\tset reactants = " + this.reactants
        );
    }

    public void initializeProducts(Set<SpeciesReference> products) throws PreconditionsException {
        if (!this.products.isEmpty() || products == null) throw new PreconditionsException();

        this.products.addAll(products);

        logger.info(
                "ReactionKA:" + this.id + ",\n\tset products = " + this.products
        );
    }

    public void initializeModifiers(Set<ModifierReference> modifiers) throws PreconditionsException {
        if (!this.modifiers.isEmpty() || modifiers == null) throw new PreconditionsException();

        this.modifiers.addAll(modifiers);

        logger.info(
                "ReactionKA:" + this.id + ",\n\tset modifiers = " + this.modifiers
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
            if (reversible) r = new Reaction(this.id, m, true);
            else r = new Reaction(this.id, m);
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
        for (Map.Entry<RateParameter, RealInterval> entry : this.rateParameters.entrySet()){
            RateParameter rateParameterKA = entry.getKey();
            RealInterval realIntervalKA = entry.getValue();
            RealInterval realIntervalReaction = r.getRate(rateParameterKA);
            logger.info("Reaction rateParam"+rateParameterKA+" = "
                    + realIntervalReaction + ", atom rate = "
                    + realIntervalKA);

            RealInterval newRate = realIntervalKA.intersect(realIntervalReaction);
            r.setRate(rateParameterKA, newRate);

            logger.info("New value = " + r.getRate(rateParameterKA));
        }

    }

    private void handleRevReactionRate(Reaction r) throws PreconditionsException {
        logger.info(
                "Handling reaction rate..."
        );
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }
        for (Map.Entry<RateParameter, RealInterval> entry : this.rateInvParameters.entrySet()) {
            RateParameter rateParameterKA = entry.getKey();
            RealInterval realIntervalKA = entry.getValue();
            RealInterval realIntervalReaction = r.getRate(rateParameterKA);
            logger.info("Reaction rateParam" + rateParameterKA + " = "
                    + realIntervalReaction + ", atom rate = "
                    + realIntervalKA);

            RealInterval newRate = realIntervalKA.intersect(realIntervalReaction);
            r.setRate(rateParameterKA, newRate);

            logger.info("New value = " + r.getRate(rateParameterKA));
        }
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

    private void handleExistentReactionReactants(Reaction r, Map<Species, Integer> reactants)
    throws PreconditionsException{
        logger.info(
                "Handling existent reaction reactants..."
        );
        for (Map.Entry<Species, Integer> entry : reactants.entrySet()) {
            Species s = entry.getKey();
            Integer stoich = entry.getValue();

            LinkReactant.insertLink(s, r, stoich);
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

            LinkReactant.insertLink(s, r, stoich);

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

    private void handleExistentReactionProducts(Reaction r, Map<Species, Integer> products)
    throws PreconditionsException {
        logger.info(
                "Handling existent reaction products..."
        );
        for (Map.Entry<Species, Integer> entry : products.entrySet()) {
            Species s = entry.getKey();
            Integer stoich = entry.getValue();

            LinkProduct.insertLink(s, r, stoich);

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

            LinkProduct.insertLink(s, r, stoich);
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

    private void handleExistentReactionModifiers(Reaction r, Map<Species, ModifierType> modifiers) throws PreconditionsException {
        logger.info(
                "Handling existent reaction modifiers..."
        );
        for (Map.Entry<Species, ModifierType> entry : modifiers.entrySet()) {
            Species s = entry.getKey();
            ModifierType type = entry.getValue();

            LinkModifier.insertLink(s, r, type);
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

            LinkModifier.insertLink(s, r, type);
        }
    }

    public void addRateParameter(RateParameter rateParam, RealInterval realInterval) {
        this.rateParameters.put(rateParam, realInterval);
    }
}
