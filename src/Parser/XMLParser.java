package Parser;

import DataTypes.PreconditionsException;
import DataTypes.RealInterval;
import InputKnowledge.CompartmentKA;
import InputKnowledge.KnowledgeBase;
import InputKnowledge.ReactionKA;
import InputKnowledge.SpeciesKA;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLParser implements KBParser {

    @Override
    public KnowledgeBase parse(String kbPath)
            throws ParserConfigurationException,
            IOException,
            SAXException,
            PreconditionsException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File f = new File(kbPath);
        Document doc = builder.parse(f);

        KnowledgeBase kb = new KnowledgeBase(f.getName(), kbPath);
        doc.getDocumentElement().normalize();

        parseCompartments(doc, kb);
        parseSpecies(doc, kb);
        parseReactions(doc, kb);

        return kb;

    }

    private void parseCompartments(Document doc, KnowledgeBase kb)
                    throws PreconditionsException {
        NodeList nodeList = doc.getElementsByTagName("compartment");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                Double size = (element.getAttribute("size").isEmpty()) ? null : Double.valueOf(element.getAttribute("size"));

                CompartmentKA ka;
                if (name.equals("")) {
                    ka = new CompartmentKA(id, false, kb);
                } else {
                    ka = new CompartmentKA(id, false, kb, name);
                }

                if (size != null) {
                    ka.initializeSize(size);
                }
            }
        }
    }

    private void parseSpecies(Document doc, KnowledgeBase kb)
                throws PreconditionsException{
        NodeList nodeList = doc.getElementsByTagName("species");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                Double minInitialAmount =
                        element.getAttribute("minInitialAmount").isEmpty() ? null : Double.valueOf(element.getAttribute("minInitialAmount"));
                Double maxInitialAmount =
                        element.getAttribute("maxInitialAmount").isEmpty() ? null : Double.valueOf(element.getAttribute("maxInitialAmount"));

                SpeciesKA ka;
                if (name.equals("")) {
                    ka = new SpeciesKA(id, false, kb);
                } else {
                    ka = new SpeciesKA(id, false, kb, name);
                }

                if (minInitialAmount == null) {
                    minInitialAmount = Double.valueOf(0);
                }
                if (maxInitialAmount == null) {
                    maxInitialAmount = Double.MAX_VALUE;
                }

                ka.initializeInitialAmount(new RealInterval(minInitialAmount, maxInitialAmount));
            }
        }
    }

    private void parseReactions(Document doc, KnowledgeBase kb)
            throws PreconditionsException{
        NodeList nodeList = doc.getElementsByTagName("reaction");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                Double minRate =
                        element.getAttribute("minRate").isEmpty() ? null : Double.valueOf(element.getAttribute("minRate"));
                Double maxRate =
                        element.getAttribute("maxRate").isEmpty() ? null : Double.valueOf(element.getAttribute("maxRate"));

                ReactionKA ka;
                if (name.equals("")) {
                    ka = new ReactionKA(id, false, kb);
                } else {
                    ka = new ReactionKA(id, false, kb, name);
                }

                if (minRate == null) {
                    minRate = Double.valueOf(0);
                }
                if (maxRate == null) {
                    maxRate = Double.MAX_VALUE;
                }

                ka.initializeRate(new RealInterval(minRate, maxRate));

                if (element.getAttribute("reversible").equals("true")) {
                    ka.setReversible();
                    Double minRateInv =
                            element.getAttribute("minRateInv").isEmpty() ? null : Double.valueOf(element.getAttribute("minRateInv"));
                    Double maxRateInv =
                            element.getAttribute("maxRateInv").isEmpty() ? null : Double.valueOf(element.getAttribute("maxRateInv"));

                    if (minRateInv == null) {
                        minRateInv = Double.valueOf(0);
                    }
                    if (maxRateInv == null) {
                        maxRateInv = Double.MAX_VALUE;
                    }

                    ka.initializeRate(new RealInterval(minRateInv, maxRateInv));
                }
            }
        }
    }
}
