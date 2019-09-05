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

    public SBMLParser(boolean preProcesser) {
        if (!preProcesser){
            try {
                sboTerm = new AdditionalKnowledgeType("sboTerm");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public KnowledgeBase parse(String kbPath)
            throws XMLStreamException, IOException, PreconditionsException {

        SBase tree = this.getContent(kbPath);

        KnowledgeBase kb = new KnowledgeBase("reactome", kbPath);

        parseCompartments(tree, kb);
        parseSpecies(tree, kb);
        parseReactions(tree, kb);

        return kb;
    }

    public SBase getContent(String sbmlPath) throws IOException, XMLStreamException {
        File f = new File(sbmlPath);
        return SBMLReader.read(f);
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

    public boolean isProtein(Species s) {
        try {
            return s.getNotesString().contains("protein");
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getExternalId(Species s){
        String annotationString = s.getAnnotation().getFullAnnotationString();
        String[] lines = annotationString.split("\n");
        for (String line: lines) {
            if (line.contains("uniprot")) {
                String[] partsOfLine = line.split("/");
                return partsOfLine[4].replace("\"", "");
            }
        }
        return null;
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

            if (isProtein(s)) {
                if (name.equals("")) {
                    ka = new ProteinKA(id, false, kb);
                    ((ProteinKA) ka).initializeExternalId(getExternalId(s));
                } else {
                    ka = new ProteinKA(id, false, kb, name);
                    ((ProteinKA) ka).initializeExternalId(getExternalId(s));
                }
            } else {
                if (name.equals("")) {
                    ka = new SpeciesKA(id, false, kb);
                } else {
                    ka = new SpeciesKA(id, false, kb, name);
                }
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

    public Set<String> getProteinsExternalIds(String sbmlPath) throws IOException, XMLStreamException {
        File f = new File(sbmlPath);
        SBase tree = SBMLReader.read(f);

        Set<String> externalIds = new HashSet<>();
        for (Species s : tree.getModel().getListOfSpecies()) {
            if (isProtein(s)) {
                externalIds.add(getExternalId(s));
            }
        }

        return externalIds;
    }

    private void parseReactions(SBase tree, KnowledgeBase kb)
            throws PreconditionsException {
        for (Reaction r : tree.getModel().getListOfReactions()) {
            parseReaction(r, kb);
        }
    }

    private ModifierType getModifierType(ModifierSpeciesReference sr) {
        String id = sr.getId();

        if (id.contains("catalyst")) {
            return ModifierType.CATALYST;
        } else if (id.contains("positiveregulator")) {
            return ModifierType.POS_REG;
        } else if (id.contains("negativeregulator")) {
            return ModifierType.NEG_REG;
        } else return null;
    }

    private void parseReaction(Reaction r, KnowledgeBase kb)
            throws PreconditionsException {

        String id = r.getId();
        String name = r.getName();
        boolean reversible = r.isReversible();

        String compartment = r.getCompartment();
        int sboTermValue = r.getSBOTerm();

        ReactionKA ka;
        if (name.equals("")) {
            ka = new ReactionKA(id, false, kb);
        } else {
            ka = new ReactionKA(id, false, kb, name);
        }

        if (reversible) ka.setReversible();

        Set<DataTypes.SpeciesReference> reactants = new HashSet<>();
        for (SpeciesReference speciesReference : r.getListOfReactants()) {
            DataTypes.SpeciesReference sr = new DataTypes.SpeciesReference(
                    speciesReference.getSpecies(), (int)speciesReference.getStoichiometry()
            );
            reactants.add(sr);
        }

        ka.initializeReactants(reactants);

        Set<DataTypes.SpeciesReference> products = new HashSet<>();
        for (SpeciesReference speciesReference : r.getListOfProducts()) {
            DataTypes.SpeciesReference sr = new DataTypes.SpeciesReference(
                    speciesReference.getSpecies(), (int)speciesReference.getStoichiometry()
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

            modifiers.add(new ModifierReference(mr.getSpecies(), mt));
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
