package Util;

import Model.BiologicalEntity;
import com.google.gson.*;

import java.lang.reflect.Type;

public class BiologicalEntityJSONAdapter
        implements JsonDeserializer<BiologicalEntity>, JsonSerializer<BiologicalEntity> {

    private static final String CLASS_META_KEY = "CLASS_META_KEY";

    @Override
    public BiologicalEntity deserialize(JsonElement jsonElement,
                              Type type,
                              JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {

        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String className = jsonObj.get(CLASS_META_KEY).getAsString();
        try {
            Class<?> clz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, clz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(BiologicalEntity be, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = jsonSerializationContext.serialize(be, be.getClass());
        jsonElement.getAsJsonObject().addProperty(CLASS_META_KEY,
                be.getClass().getCanonicalName());

        return jsonElement;
    }
}
