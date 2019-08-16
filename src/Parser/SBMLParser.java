package Parser;
import DataTypes.ModifierReference;
import DataTypes.ModifierType;
import DataTypes.RealInterval;
import org.sbml.jsbml.*;
import DataTypes.PreconditionsException;
import InputKnowledge.*;
import Model.AdditionalKnowledgeType;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SBMLParser implements KBParser {

    AdditionalKnowledgeType sboTerm;

    public SBMLParser() {
        try {
            sboTerm = new AdditionalKnowledgeType("sboTerm");
        } catch (Exception e) {}
    }

    @Override
    public KnowledgeBase parse(String kbPath) throws XMLStreamException, IOException {

        File f = new File(kbPath);
        SBase tree = SBMLReader.read(f);

        KnowledgeBase kb = new KnowledgeBase(f.getName(), kbPath);

        try {
            parseCompartments(tree, kb);
            parseSpecies(tree, kb);
            parseReactions(tree, kb);
        } catch (PreconditionsException exc) {
            exc.printStackTrace();
            return null;
        }

        return null;
    }

    private void parseCompartments(SBase tree, KnowledgeBase kb)
                                throws PreconditionsException {

        for (Compartment c : tree.getModel().getListOfCompartments()) {
            String id = c.getId();
            String name = c.getName();
            Double size = c.getSize();
            int sboTermValue = c.getSBOTerm();

            CompartmentKA ka;
            if (name.equals("")) {
                ka = new CompartmentKA(id, false, kb);
            } else {
                ka = new CompartmentKA(id, false, kb, name);
            }

            if (Double.compare(size, Double.NaN) != 0) {
                ka.initializeSize(size);
            }

            if (Integer.compare(sboTermValue, -1) != 0) {
                ka.insertLinkAdditionalKA(sboTerm, String.valueOf(sboTermValue));
            }
        }
    }

    private void parseSpecies(SBase tree, KnowledgeBase kb)
                            throws PreconditionsException {
        for (Species s : tree.getModel().getListOfSpecies()) {
            String id = s.getId();
            String name = s.getName();
            String compartment = s.getCompartment();
            Double initialAmount = s.getInitialAmount();
            int sboTermValue = s.getSBOTerm();

            SpeciesKA ka;
            if (name.equals("")) {
                ka = new SpeciesKA(id, false, kb);
            } else {
                ka = new SpeciesKA(id, false, kb, name);
            }

            if (!compartment.equals("")) {
                ka.initializeCompartmentId(compartment);
            }

            if (Double.compare(initialAmount, Double.NaN) != 0) {
                RealInterval ia = new RealInterval(initialAmount, initialAmount);
                ka.initializeInitialAmount(ia);
            }

            if (Integer.compare(sboTermValue, -1) != 0) {
                ka.insertLinkAdditionalKA(sboTerm, String.valueOf(sboTermValue));
            }
        }
    }

    private void parseReactions(SBase tree, KnowledgeBase kb)
            throws PreconditionsException {
        for (Reaction r : tree.getModel().getListOfReactions()) {
            if (r.isReversible()) {
                parseRevReaction(r, kb);
            } else {
                parseReaction(r, kb);
            }
        }
    }

    private ModifierType getModifierType(ModifierSpeciesReference sr) {
        String name = sr.getName();

        if (name.contains("catalyst")) {
            return ModifierType.CATALYST;
        } else if (name.contains("positive_regulator")) {
            return ModifierType.POS_REG;
        } else if (name.contains("negative_regulator")) {
            return ModifierType.NEG_REG;
        } else return null;
    }

    private void parseReaction(Reaction r, KnowledgeBase kb)
            throws PreconditionsException {

        String id = r.getId();
        String name = r.getName();

        String compartment = r.getCompartment();
        int sboTermValue = r.getSBOTerm();

        ReactionKA ka;
        if (name.equals("")) {
            ka = new ReactionKA(id, false, kb);
        } else {
            ka = new ReactionKA(id, false, kb, name);
        }

        Set<DataTypes.SpeciesReference> reactants = new HashSet<>();
        for (SpeciesReference speciesReference : r.getListOfReactants()) {
            DataTypes.SpeciesReference sr = new DataTypes.SpeciesReference(
                    speciesReference.getId(), (int)speciesReference.getStoichiometry()
            );
            reactants.add(sr);
        }

        ka.initializeReactants(reactants);

        Set<DataTypes.SpeciesReference> products = new HashSet<>();
        for (SpeciesReference speciesReference : r.getListOfProducts()) {
            DataTypes.SpeciesReference sr = new DataTypes.SpeciesReference(
                    speciesReference.getId(), (int)speciesReference.getStoichiometry()
            );
            products.add(sr);
        }

        ka.initializeProducts(products);

        Set<DataTypes.ModifierReference> modifiers = new HashSet<>();
        for (ModifierSpeciesReference mr : r.getListOfModifiers()) {
            ModifierType mt = getModifierType(mr);
            if (mt == null) throw new PreconditionsException(
                    "Could not recognize modifier " + mr.getId() + " type"
            );

            modifiers.add(new ModifierReference(mr.getId(), mt));
        }

        ka.initializeModifiers(modifiers);

        if (!compartment.equals("")) {
            ka.initializeCompartmentId(compartment);
        }

        if (Integer.compare(sboTermValue, -1) != 0) {
            ka.insertLinkAdditionalKA(sboTerm, String.valueOf(sboTermValue));
        }
    }

    private void parseRevReaction(Reaction r, KnowledgeBase kb)
            throws PreconditionsException {

        String id = r.getId();
        String name = r.getName();

        String compartment = r.getCompartment();
        int sboTermValue = r.getSBOTerm();

        RevReactionKA ka;
        if (name.equals("")) {
            ka = new RevReactionKA(id, false, kb);
        } else {
            ka = new RevReactionKA(id, false, kb, name);
        }

        Set<DataTypes.SpeciesReference> reactants = new HashSet<>();
        for (SpeciesReference speciesReference : r.getListOfReactants()) {
            DataTypes.SpeciesReference sr = new DataTypes.SpeciesReference(
                    speciesReference.getId(), (int)speciesReference.getStoichiometry()
            );
            reactants.add(sr);
        }

        ka.initializeReactants(reactants);

        Set<DataTypes.SpeciesReference> products = new HashSet<>();
        for (SpeciesReference speciesReference : r.getListOfProducts()) {
            DataTypes.SpeciesReference sr = new DataTypes.SpeciesReference(
                    speciesReference.getId(), (int)speciesReference.getStoichiometry()
            );
            products.add(sr);
        }

        ka.initializeProducts(products);

        Set<DataTypes.ModifierReference> modifiers = new HashSet<>();
        for (ModifierSpeciesReference mr : r.getListOfModifiers()) {
            ModifierType mt = getModifierType(mr);
            if (mt == null) throw new PreconditionsException(
                    "Could not recognize modifier " + mr.getId() + " type"
            );

            modifiers.add(new ModifierReference(mr.getId(), mt));
        }

        ka.initializeModifiers(modifiers);

        if (!compartment.equals("")) {
            ka.initializeCompartmentId(compartment);
        }

        if (Integer.compare(sboTermValue, -1) != 0) {
            ka.insertLinkAdditionalKA(sboTerm, String.valueOf(sboTermValue));
        }
    }
}
