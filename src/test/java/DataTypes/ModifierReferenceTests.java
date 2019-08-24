package DataTypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModifierReferenceTests {

    @Test
    public void equals_equalModReferences_returnsTrue(){
        ModifierReference modRef1 = new ModifierReference("1", ModifierType.CATALYST);
        ModifierReference modRef2 = new ModifierReference("1", ModifierType.CATALYST);
        assertEquals(modRef1, modRef2);
    }


}
