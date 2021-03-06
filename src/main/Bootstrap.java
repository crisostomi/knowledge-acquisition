import InputKnowledge.ProteinKA;
import Model.LinkType.LinkTypeComprises;
import Model.Model;
import Parser.SBMLParser;
import Parser.TSVParser;
import org.sbml.jsbml.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bootstrap {

    public static void joinAbundances(String sbmlPath, String tsvPath, String outputPath) throws IOException, XMLStreamException {
        SBMLParser sbmlParser = new SBMLParser(true);
        Set<String> externalIds = sbmlParser.getProteinsExternalIds(sbmlPath);
        TSVParser tsvParser = new TSVParser();
        List<String[]> rows = tsvParser.getContent(tsvPath);
        StringBuilder output = new StringBuilder();
        for(String id:externalIds){
            for (String[] row: rows){
                String externalID =  row[1].split("\\|")[0];
                if (externalID.equals(id)){
                    output.append(row[0] + "\t"+row[1]+"\t"+row[2]+"\n");
                }
            }
        }
        File outputFile = new File(outputPath);
        outputFile.createNewFile();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(output.toString());
        writer.flush();
        writer.close();
    }

    public static void buildQuantitativeFile(String sbmlPath, String configPath)
            throws ParserConfigurationException, TransformerException, IOException, XMLStreamException {
        QuantitativeXMLBuilder builder = new QuantitativeXMLBuilder(sbmlPath, configPath);
        builder.buildConfig();
    }

    public static void buildQuantitativeFile(String sbmlPath, String configPath,
                                             Double minInitialAmount, Double maxInitialAmount,
                                             Double minK, Double maxK,
                                             Double minKcat, Double maxKcat,
                                             Double minKm, Double maxKm
                                             )
            throws ParserConfigurationException, TransformerException, IOException, XMLStreamException {
        QuantitativeXMLBuilder builder = new QuantitativeXMLBuilder(
                sbmlPath, configPath,
                minInitialAmount, maxInitialAmount,
                minK, maxK,
                minKcat, maxKcat,
                minKm, maxKm);

        builder.buildConfig();
    }

    private static class QuantitativeXMLBuilder {

        private SBMLParser sbmlParser;
        private String sbmlPath;
        private String configPath;
        private Document config;
        private Double minInitialAmount;
        private Double maxInitialAmount;
        private Double minK;
        private Double maxK;
        private Double minKcat;
        private Double maxKcat;
        private Double minKm;
        private Double maxKm;

        public QuantitativeXMLBuilder(String sbmlPath, String configPath) throws ParserConfigurationException {
            this.sbmlPath = sbmlPath;
            this.sbmlParser = new SBMLParser(true);
            this.configPath = configPath;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.config = dBuilder.newDocument();
        }

        public QuantitativeXMLBuilder(String sbmlPath, String configPath,
                                      Double minInitialAmount, Double maxInitialAmount,
                                      Double minK, Double maxK,
                                      Double minKcat, Double maxKcat,
                                      Double minKm, Double maxKm) throws ParserConfigurationException {

            this(sbmlPath, configPath);
            this.minInitialAmount = minInitialAmount;
            this.maxInitialAmount = maxInitialAmount;
            this.minK = minK;
            this.maxK = maxK;
            this.minKcat = minKcat;
            this.maxKcat = maxKcat;
            this.minKm = minKm;
            this.maxKm = maxKm;
        }

        public void buildConfig() throws TransformerException, IOException, XMLStreamException {

            Node configuration = this.config.createElement("configuration");
            this.config.appendChild(configuration);
            Node listOfCompartments = this.config.createElement("listOfCompartments");
            Node listOfSpecies = this.config.createElement("listOfSpecies");
            Node listOfReactions = this.config.createElement("listOfReactions");
            configuration.appendChild(listOfCompartments);
            configuration.appendChild(listOfSpecies);
            configuration.appendChild(listOfReactions);

            SBase tree = sbmlParser.getContent(sbmlPath);

            for (Compartment compartment: tree.getModel().getListOfCompartments()) {
                listOfCompartments.appendChild(buildCompartmentElement(compartment));
            }

            for (Species species: tree.getModel().getListOfSpecies()) {
                listOfSpecies.appendChild(buildSpeciesElement(species));
            }

            for (Reaction reaction: tree.getModel().getListOfReactions()) {
                listOfReactions.appendChild(buildReactionElement(reaction));
            }

            this.closeConfig();
        }

        private Element buildCompartmentElement(Compartment compartment) {
            Element element = this.config.createElement("compartment");
            element.setAttribute("id", compartment.getId());
            if (compartment.getName() != null) element.setAttribute("name", compartment.getName());

            element.setAttribute("size", "");
            return element;
        }


        private Element buildSpeciesElement(Species species) {
            String elementName = (sbmlParser.isProtein(species))? "protein" : "species";
            Element element = this.config.createElement(elementName);
            element.setAttribute("id", species.getId());
            if (species.getName() != null) element.setAttribute("name", species.getName());

            String minInitalAmountValue = (this.minInitialAmount == null) ? "" : String.valueOf(this.minInitialAmount);
            element.setAttribute("minInitialAmount", minInitalAmountValue);
            String maxInitalAmountValue = (this.maxInitialAmount == null) ? "" : String.valueOf(this.maxInitialAmount);
            element.setAttribute("maxInitialAmount", maxInitalAmountValue);

            return element;
        }

        private Element buildReactionElement(Reaction reaction) {
            String tagName = "reaction";
            Element element = this.config.createElement(tagName);
            element.setAttribute("id", reaction.getId());
            String isReversible = String.valueOf(reaction.isReversible());
            element.setAttribute("reversible", isReversible);

            if (reaction.getName() != null) element.setAttribute("name", reaction.getName());

            String minKValue = (this.minK == null) ? "" : String.valueOf(this.minK);
            element.setAttribute("minK", minKValue);
            String maxKValue = (this.maxK == null) ? "" : String.valueOf(this.maxK);
            element.setAttribute("maxK", maxKValue);

            String minKmValue = (this.minKm == null) ? "" : String.valueOf(this.minKm);
            element.setAttribute("minKm", minKmValue);
            String maxKmValue = (this.maxKm == null) ? "" : String.valueOf(this.maxKm);
            element.setAttribute("maxKm", maxKmValue);

            String minKcatValue = (this.minKcat == null) ? "" : String.valueOf(this.minKcat);
            element.setAttribute("minKcat", minKcatValue);
            String maxKcatValue = (this.maxKcat == null) ? "" : String.valueOf(this.maxKcat);
            element.setAttribute("maxKcat", maxKcatValue);

            if (reaction.isReversible()) {
                element.setAttribute("minRateInv", "");
                element.setAttribute("maxRateInv", "");
                element.setAttribute("minKmInv", "");
                element.setAttribute("maxKmInv", "");
                element.setAttribute("minKcatInv", "");
                element.setAttribute("maxKcatInv", "");
            }

            return element;
        }

        public void trimWhitespace(Node node) {
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
}
