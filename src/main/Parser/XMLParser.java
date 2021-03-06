package Parser;

import DataTypes.PreconditionsException;
import DataTypes.RateParameter;
import DataTypes.RealInterval;
import InputKnowledge.*;

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
import java.util.Set;

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
                boolean override =
                        (element.getAttribute("override").isEmpty()) ?
                                false : Boolean.valueOf(element.getAttribute("override"));

                Double size = (element.getAttribute("size").isEmpty()) ?
                        null : Double.valueOf(element.getAttribute("size"));

                CompartmentKA ka;
                if (name.equals("")) {
                    ka = new CompartmentKA(id, override, kb);
                } else {
                    ka = new CompartmentKA(id, override, kb, name);
                }

                if (size != null) {
                    ka.initializeSize(size);
                }
            }
        }
    }

    private void parseSpecies(Document doc, KnowledgeBase kb)
                throws PreconditionsException{

        String[] elements = {"species", "protein"};
        for (String elementName: elements) {
            NodeList nodeList = doc.getElementsByTagName(elementName);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    String name = element.getAttribute("name");
                    boolean override =
                            (element.getAttribute("override").isEmpty()) ?
                                    false : Boolean.valueOf(element.getAttribute("override"));
                    Double minInitialAmount =
                            element.getAttribute("minInitialAmount").isEmpty() ?
                                    null : Double.valueOf(element.getAttribute("minInitialAmount"));
                    Double maxInitialAmount =
                            element.getAttribute("maxInitialAmount").isEmpty() ?
                                    null : Double.valueOf(element.getAttribute("maxInitialAmount"));


                    SpeciesKA ka;
                    if (elementName.equals("protein")) {
                        if (name.equals("")) {
                            ka = new ProteinKA(id, override, kb);
                        } else {
                            ka = new ProteinKA(id, override, kb, name);
                        }
                    } else {
                        if (name.equals("")) {
                            ka = new SpeciesKA(id, override, kb);
                        } else {
                            ka = new SpeciesKA(id, override, kb, name);
                        }
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

                ReactionKA ka;
                if (name.equals("")) {
                    ka = new ReactionKA(id, false, kb);
                } else {
                    ka = new ReactionKA(id, false, kb, name);
                }

                for ( RateParameter rate: RateParameter.values()){
                    String minRateName = "min"+rate.name();
                    String maxRateName = "max"+rate.name();
                    Double minRate =
                            element.getAttribute(minRateName).isEmpty() ? null : Double.valueOf(element.getAttribute(minRateName));
                    Double maxRate =
                            element.getAttribute(maxRateName).isEmpty() ? null : Double.valueOf(element.getAttribute(maxRateName));

                    if (minRate == null) {
                        minRate = Double.valueOf(0);
                    }
                    if (maxRate == null) {
                        maxRate = Double.MAX_VALUE;
                    }

                    ka.initializeRateParam(rate, new RealInterval(minRate, maxRate));

                    if (element.getAttribute("reversible").equals("true")) {
                        ka.setReversible();
                        Double minRateInv =
                                element.getAttribute(minRateName+"Inv").isEmpty() ? null : Double.valueOf(element.getAttribute(minRateName+"Inv"));
                        Double maxRateInv =
                                element.getAttribute(maxRateName+"Inv").isEmpty() ? null : Double.valueOf(element.getAttribute(maxRateName+"Inv"));

                        if (minRateInv == null) {
                            minRateInv = Double.valueOf(0);
                        }
                        if (maxRateInv == null) {
                            maxRateInv = Double.MAX_VALUE;
                        }

                        ka.initializeRateInvParam(rate, new RealInterval(minRateInv, maxRateInv));
                    }
                }
            }
        }
    }
}
