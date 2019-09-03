package Miner;

import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Set;

public interface Miner {

    KnowledgeBase mine(Set<String> ka_id) throws IOException, XMLStreamException, PreconditionsException;

}
