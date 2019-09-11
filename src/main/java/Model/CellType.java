package Model;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;

public class CellType {
    private static Set<String> namesInUse = new HashSet<>();
    private String name;
    private static final double avogadroConstant = 6.022e23;
    private Double proteinsInCell;
    private Double size;

    public CellType() {}

    public CellType(String name, Double size, Double proteinsInCell) throws PreconditionsException {
        if (namesInUse.contains(name)) throw new PreconditionsException();

        this.name = name;
        this.size = size;
        this.proteinsInCell = proteinsInCell;
    }

    public double calculateAbundanceMol(double abundancePPM) {
        double abundancePercent = abundancePPM * 1e-4;
        double numberOfProteins = abundancePercent * proteinsInCell / 100;
        double abundanceMol = numberOfProteins / avogadroConstant;

        return abundanceMol;
    }

    public double calculateAbundanceMicroMol(double abundancePPM) {
        double abundanceMol = calculateAbundanceMol(abundancePPM);
        double abundanceMicroMol = abundanceMol * 1e6;
        return abundanceMicroMol;
    }

    public double calculateAbundanceConcentration(double abundancePPM) {
        double abundanceMol = calculateAbundanceMol(abundancePPM);
        double abundanceConcentration = abundanceMol / this.size;
        return abundanceConcentration;
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
