package Parser;

import Model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class ConfigBuilder {

    private Model model;
    private String configPath;
    private Document config;

    /**
     * Class to build an XML of atoms for the model
     * @param model the model of interest
     * @param configDir the output directory where to put the built XML
     */
    public ConfigBuilder(Model model, String configDir) throws ParserConfigurationException, IOException, SAXException {
        this.model = model;
        this.configPath = configDir;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.config = dBuilder.newDocument();

    }


    public void buildConfig() throws TransformerException {

        Node configuration = this.config.createElement("configuration");
        this.config.appendChild(configuration);
        Node listOfCompartments = this.config.createElement("listOfCompartments");
        Node listOfSpecies = this.config.createElement("listOfSpecies");
        Node listOfReactions = this.config.createElement("listOfReactions");
        configuration.appendChild(listOfCompartments);
        configuration.appendChild(listOfSpecies);
        configuration.appendChild(listOfReactions);

        for (LinkTypeComprises l: this.model.getLinkComprisesSet()) {
            BiologicalEntity be = l.getBiologicalEntity();
            if (be instanceof Compartment) {
                Compartment c = (Compartment) be;
                listOfCompartments.appendChild(buildCompartmentElement(c));
            } else if (be instanceof Species) {
                Species s = (Species) be;
                listOfSpecies.appendChild(buildSpeciesElement(s));
            } else if (be instanceof Reaction) {
                Reaction r = (Reaction) be;
                listOfReactions.appendChild(buildReactionElement(r));
            }
        }

        this.closeConfig();
    }

    /**
     * Method to build data element  for a single compartment, initially all blank
     * @param compartment the compartment of interest
     * @return a line of XML with the definition of the constraint for the species
     */
    private Element buildCompartmentElement(Compartment compartment) {
        Element element = this.config.createElement("compartment");
        element.setAttribute("id", compartment.getId());
        if (compartment.getName() != null) element.setAttribute("name", compartment.getName());

        element.setAttribute("size", "");
        return element;
    }

    /**
     * Method to build data element for a single species, initially all blank
     * @param species the species of interest
     * @return a line of XML with the definition of the constraint for the species
     */
    private Element buildSpeciesElement(Species species) {
        String elementName = (species instanceof Protein)? "protein" : "species";
        Element element = this.config.createElement(elementName);
        element.setAttribute("id", species.getId());
        if (species.getName() != null) element.setAttribute("name", species.getName());

        element.setAttribute("minInitialAmount", "");
        element.setAttribute("maxInitialAmount", "");

        element.setAttribute("lowerBound", "");
        element.setAttribute("upperBound", "");

        return element;
    }

    /**
     * Method to build data element for a single reaction, initially all blank
     * @param reaction the reaction of interest
     * @return a line of XML with the definition of the constraint for the species
     */
    private Element buildReactionElement(Reaction reaction) {
        String tagName = "reaction";
        Element element = this.config.createElement(tagName);
        element.setAttribute("id", reaction.getId());
        String isReversible = String.valueOf(reaction.isReversible());
        element.setAttribute("reversible", isReversible);

        if (reaction.getName() != null) element.setAttribute("name", reaction.getName());

        element.setAttribute("minRate", "");
        element.setAttribute("maxRate", "");

        if (reaction.isReversible()) {
            element.setAttribute("minRateInv", "");
            element.setAttribute("maxRateInv", "");
        }

        return element;
    }

    public static void trimWhitespace(Node node) {
        NodeList children = node.getChildNodes();
        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if(child.getNodeType() == Node.TEXT_NODE) {
                child.setTextContent(child.getTextContent().trim());
            }
            trimWhitespace(child);
        }
    }


    public void closeConfig() throws TransformerException {

        this.config.normalizeDocument();
        trimWhitespace(this.config);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(this.config);
        StreamResult streamResult = new StreamResult(new File(this.configPath));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(domSource, streamResult);
    }
}
