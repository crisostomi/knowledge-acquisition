package DataTypes;

import java.io.Serializable;
import java.util.Objects;

public class RealInterval implements Serializable {
    private double lowerBound;
    private double upperBound;

    /**
     * Class that represents a real interval
     * @param lb the lower bound of the interval
     * @param ub the upper bound of the interval
     * @throws PreconditionsException if provided with lb > ub
     */
    public RealInterval(double lb, double ub) throws PreconditionsException {

        if (lb > ub) {
            throw new PreconditionsException("" +
                    "Cannot create RealInterval instance with lower bound greater than upper bound");
        }
        this.lowerBound = lb;
        this.upperBound = ub;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Method that intersects this with another instance of RealInterval. If the intersection
     * results in an empty interval, null is returned
     * @param other the other interval to be intersected with this
     * @return a new RealInterval that is the result of the intersection
     */
    public RealInterval intersect(RealInterval other) {
        double lb = Math.max(this.lowerBound, other.lowerBound);
        double ub = Math.min(this.upperBound, other.upperBound);

        try {
            return new RealInterval(lb, ub);
        } catch (PreconditionsException exc) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealInterval that = (RealInterval) o;
        return Double.compare(that.lowerBound, lowerBound) == 0 &&
                Double.compare(that.upperBound, upperBound) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }
}
