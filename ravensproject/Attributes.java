package ravensproject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class Attributes {
   Map<String, String> map;

   Attributes(Map<String, String> rhs) {
      map = new HashMap<>();
      // copy the whole contents of the rhs map over to member map
      for (Map.Entry<String, String> entry : rhs.entrySet()) {
         map.put(entry.getKey(), entry.getValue());
      }
   }

   boolean isUnchanged(Map<String, String> rhs) {
      // make sure the two maps of attributes are of the same size
      if (map.size() != rhs.size()) {
         return false;
      }
      // for each key in this map, make sure the value for that key in this map matches the value
      // for the same key in the other map
      for (String key : map.keySet()) {
         // skip comparing the "inside" attribute
         if (!key.equals("inside")) {
            if (!map.get(key).equals(rhs.get(key))) {
               return false;
            }
         }
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, String> entry : map.entrySet()) {
         builder.append(", " + entry.getKey() + ": " + entry.getValue());
      }
      return builder.toString();
   }
}
