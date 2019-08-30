package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public class CellType {
    private static Set<String> namesInUse = new HashSet<>();
    private String name;

    public CellType(String name) throws PreconditionsException {
        if (namesInUse.contains(name)) throw new PreconditionsException();

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
