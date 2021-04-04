package Model;

import Model.Exceptions.AdditionalKnowledgeTypeNotUniqueException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AdditionalKnowledgeType implements Serializable {
    private static Set<String> idsInUse = new HashSet<>();
    private final String id;

    public AdditionalKnowledgeType(String id) throws AdditionalKnowledgeTypeNotUniqueException {
//        if (idsInUse.contains(id)) throw new AdditionalKnowledgeTypeNotUniqueException(
//                "AdditionalKnowledgeType " + id + " already exists"
//        );

        this.id = id;
        idsInUse.add(id);
    }

    public String getId() {
        return id;
    }
}
