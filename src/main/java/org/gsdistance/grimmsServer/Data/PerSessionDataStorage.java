package org.gsdistance.grimmsServer.Data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.gsdistance.grimmsServer.Constructable.Data;

public class PerSessionDataStorage {
   public static final Map<String, Data<Object, Type>> dataStore = new HashMap();
   public static Double tpCost = (double)500.0F;

   public PerSessionDataStorage() {
   }

   public static <T> void softSave(T data, Class<T> ignoredType, String key) {
      dataStore.put(key, Data.of(data, ignoredType));
   }

   public static <T> T getData(String key, Class<T> type) {
      Data<Object, Type> data = (Data)dataStore.get(key);
      return (T)(data != null && type.isInstance(data.key()) ? type.cast(data.key()) : null);
   }
}
