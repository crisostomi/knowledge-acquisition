package InputKnowledge;

import DataTypes.PreconditionsException;

public class CompartmentKASizeNotFoundException extends PreconditionsException {
    CompartmentKASizeNotFoundException(){super();}
    CompartmentKASizeNotFoundException(String message){super(message);}
}
