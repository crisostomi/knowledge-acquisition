package Parser;

import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface KBParser {

    KnowledgeBase parse(String kbPath) throws XMLStreamException, IOException,
                                            FormatNotSupportedException, PreconditionsException;
}
