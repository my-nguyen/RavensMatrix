package ravensproject;

import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class StaticAttributes extends Attributes {
   StaticAttributes(Map<String, String> rhs) {
      super(rhs);

      // copy the whole contents of the rhs map over to member map
      for (Map.Entry<String, String> entry : rhs.entrySet()) {
         map.put(entry.getKey(), entry.getValue());
      }
   }
}
