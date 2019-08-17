package Util;
import com.google.gson.*;

public class MyExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipField(FieldAttributes fa) {
        return fa.getAnnotation(GsonRepellent.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        // never skips any class
        return false;
    }
}