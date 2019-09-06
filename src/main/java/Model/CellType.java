package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public class CellType {
    private static Set<String> namesInUse = new HashSet<>();
    private String name;
    private static final double avogadroConstant = 6.022e23;
    private Double proteinsInCell;

    public CellType() {}

    public CellType(String name) throws PreconditionsException {
        if (namesInUse.contains(name)) throw new PreconditionsException();

        this.name = name;
    }

    public CellType(String name, double proteinsInCell) throws PreconditionsException {
        if (namesInUse.contains(name)) throw new PreconditionsException();

        this.name = name;
        this.proteinsInCell = proteinsInCell;
    }

    public double calculateAbundanceMicroMol(double abundancePPM) {
        double abundancePercent = abundancePPM * 1e-4;
        double numberOfProteins = abundancePercent * proteinsInCell / 100;
        double abundanceMol = numberOfProteins / avogadroConstant;
        double abundanceMicroMol = abundanceMol * 1e6;

        return abundanceMol;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProteinsInCell(Double proteinsInCell) {
        assert this.proteinsInCell == null;
        this.proteinsInCell = proteinsInCell;
    }

    public Double getProteinsInCell() {
        return proteinsInCell;
    }
}
