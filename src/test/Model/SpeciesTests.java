package Model;
import DataTypes.PreconditionsException;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// naming convention: MethodName_StateUnderTest_ExpectedBehavior

public class SpeciesTests {

    @Test
    void override_speciesDifferentIds_throwPrecondException() throws PreconditionsException {
        Model model = new Model();
        Species overrider = new Species("Species_1", model);
        Species overridee = new Species("Species_2", model);
        assertThrows(PreconditionsException.class, () -> overrider.override(overridee));
    }

}
