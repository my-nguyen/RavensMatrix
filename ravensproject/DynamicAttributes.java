package ravensproject;

import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class DynamicAttributes extends Attributes {
   DynamicAttributes(Map<String, String> rhs) {
      super(rhs);

      // remove entries inside rhs map that match keys before adding such entries to member map
      String[] keys = { "inside" };
      for (String key : keys) {
         String value = rhs.remove(key);
         if (value != null) {
            map.put(key, value);
         }
      }
   }
}
