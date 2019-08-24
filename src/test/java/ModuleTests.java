import DataTypes.ModifierType;
import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;
import Model.*;
import Parser.FormatNotSupportedException;
import Parser.SBMLParser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleTests {
    @Test
    public void knowledgeAcquisition_inputDummyPathway_createCorrectModel() throws PreconditionsException, IOException, XMLStreamException, FormatNotSupportedException, SAXException, ParserConfigurationException {
        String sbmlPath = "/home/don/Dropbox/Tesisti/software/development/test-cases/dummy/dummyPathway.sbml";
        Set<String> kbPaths = new HashSet<>();
        kbPaths.add(sbmlPath);
        Model generatedModel = HandleModel.createModel(kbPaths);
        Boolean equal = true;

        String[] bioEntitySet = {"compartment_1","reaction_1","reaction_2","reaction_3","species_A","species_B","species_C","species_D","species_E","species_F","species_G","species_S","species_P"};
        for (String speciesId:bioEntitySet){
            if(generatedModel.getBioEntityById(speciesId) == null){
                equal = false;
            }
        }

        assertTrue(equal);

    }

}
