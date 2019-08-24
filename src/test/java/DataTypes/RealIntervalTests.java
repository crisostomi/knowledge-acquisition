package DataTypes;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class RealIntervalTests {

    @Test
    public void constructor_lowerBoundGTupperBound_throwPrecException() {
        assertThrows(PreconditionsException.class, () -> new RealInterval(2,1));
    }

    @Test
    public void intersect_nonIntersectingParams_returnNull() throws PreconditionsException {
        RealInterval realInt1 = new RealInterval(2, 10);
        RealInterval realInt2 = new RealInterval(-4, 0);
        assertNull(realInt1.intersect(realInt2));
    }

    @Test
    public void intersect_intersectingParams_calculate() throws PreconditionsException {
        assertEquals(new RealInterval(0, 10).intersect(new RealInterval(7, 20)), new RealInterval(7, 10));
        assertEquals(new RealInterval(-10, 0).intersect(new RealInterval(-5, 20)), new RealInterval(-5, 0));
    }



}
