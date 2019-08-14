package InputKnowledge;

import DataTypes.*;
import Model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReactionKA extends KnowledgeAtom {

    protected RealInterval rate = null;

    protected String compartmentId = null;
    protected Set<SpeciesReference> reactants = new HashSet<>();
    protected Set<SpeciesReference> products = new HashSet<>();
    protected Set<ModifierReference> modifiers = new HashSet<>();


    public ReactionKA(String id, boolean override, KnowledgeBase knowledgeBase) {
        super(id, override, knowledgeBase);
    }

    public ReactionKA(String id, boolean override, KnowledgeBase knowledgeBase, String name) {
        super(id, override, knowledgeBase, name);
    }

    public void initializeCompartmentId(String compartmentId) throws PreconditionsException {
        if (this.compartmentId != null || compartmentId == null) throw new PreconditionsException();
        this.compartmentId = compartmentId;
    }

    public void initializeReactants(Set<SpeciesReference> reactants) throws PreconditionsException {
        if (this.reactants != null || reactants == null) throw new PreconditionsException();

        this.reactants.addAll(reactants);
    }

    public void initializeProducts(Set<SpeciesReference> products) throws PreconditionsException {
        if (this.products != null || products == null) throw new PreconditionsException();

        this.products.addAll(products);
    }

    public void initializeModifiers(Set<ModifierReference> modifiers) throws PreconditionsException {
        if (this.modifiers != null || modifiers == null) throw new PreconditionsException();

        this.modifiers.addAll(modifiers);
    }

    @Override
    public void consolidateModelWithAtom(Model m) throws PreconditionsException {
        BiologicalEntity be = m.getBioEntityById(this.id);
        if (be != null && !(be instanceof Reaction)) {
            throw new PreconditionsException(
                    "The model comprises a BiologicalEntity with same ID that is not a Reaction"
            );
        }

        Reaction r;
        if (be == null) {
            r = new Reaction(this.id, m);
        } else {
            r = (Reaction) be;
        }

        this.handleReactionCompartment(r);
        this.handleReactionRate(r);
        this.handleReactionReactants(r);
        this.handleReactionProducts(r);
        this.handleReactionModifiers(r);

        this.handleBioEntityName(r);
        this.addAdditionalKnowledge(r);
    }

    private void handleReactionCompartment(Reaction r) throws PreconditionsException {
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

        Compartment c = null;
        LinkTypeReactionCompartment l = r.getLinkReactionCompartment();
        if (l != null) {
            c = l.getCompartment();
            if (!(c.getId().equals(this.compartmentId))) throw new CompartmentMismatchException();
        }

        if (c == null) {
            c = new Compartment(this.compartmentId, r.getLinkComprises().getModel());

        }

        LinkReactionCompartment.insertLink(r, c);
    }

    private void handleReactionRate(Reaction r) throws PreconditionsException {
        if (!this.id.equals(r.getId())) {
            throw new IdMismatchException(
                    "Reaction has different ID ("+r.getId()+") from atom ("+this.id+")"
            );
        }

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
    }


    private void handleReactionReactants(Reaction r) throws PreconditionsException {
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
            if (s == null) {
                existent.put(s, stoich);
            } else {
                nonExistent.put(speciesId, stoich);
            }
        }

        handleExistentReactionReactants(r, existent);
        handleNonExistentReactionReactants(r, nonExistent);

    }

    private void handleExistentReactionReactants(Reaction r, Map<Species, Integer> reactants) {
        for (Map.Entry<Species, Integer> entry : reactants.entrySet()) {
            Species s = entry.getKey();
            Integer stoich = entry.getValue();

            r.addReactant(s, stoich);
        }
    }

    private void handleNonExistentReactionReactants(Reaction r, Map<String, Integer> reactants) throws PreconditionsException {
        for (Map.Entry<String, Integer> entry : reactants.entrySet()) {
            String s_id = entry.getKey();
            Integer stoich = entry.getValue();

            Species s = new Species(s_id, r.getLinkComprises().getModel());

            r.addReactant(s, stoich);
        }
    }


    private void handleReactionProducts(Reaction r) throws PreconditionsException {
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
            if (s == null) {
                existent.put(s, stoich);
            } else {
                nonExistent.put(speciesId, stoich);
            }
        }

        handleExistentReactionProducts(r, existent);
        handleNonExistentReactionProducts(r, nonExistent);

    }

    private void handleExistentReactionProducts(Reaction r, Map<Species, Integer> products) {
        for (Map.Entry<Species, Integer> entry : products.entrySet()) {
            Species s = entry.getKey();
            Integer stoich = entry.getValue();

            r.addProduct(s, stoich);
        }
    }

    private void handleNonExistentReactionProducts(Reaction r, Map<String, Integer> products) throws PreconditionsException {
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String s_id = entry.getKey();
            Integer stoich = entry.getValue();

            Species s = new Species(s_id, r.getLinkComprises().getModel());

            r.addProduct(s, stoich);
        }
    }


    private void handleReactionModifiers(Reaction r) throws PreconditionsException {
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
            if (s == null) {
                existent.put(s, stoich);
            } else {
                nonExistent.put(speciesId, stoich);
            }
        }

        handleExistentReactionModifiers(r, existent);
        handleNonExistentReactionModifiers(r, nonExistent);
    }

    private void handleExistentReactionModifiers(Reaction r, Map<Species, ModifierType> modifiers) {
        for (Map.Entry<Species, ModifierType> entry : modifiers.entrySet()) {
            Species s = entry.getKey();
            ModifierType type = entry.getValue();

            r.addModifier(s, type);
        }
    }

    private void handleNonExistentReactionModifiers(Reaction r, Map<String, ModifierType> modifiers) throws PreconditionsException{
        for (Map.Entry<String, ModifierType> entry : modifiers.entrySet()) {
            String s_id = entry.getKey();
            ModifierType type = entry.getValue();

            Species s = new Species(s_id, r.getLinkComprises().getModel());

            r.addModifier(s, type);
        }
    }
}
