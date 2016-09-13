package ravensproject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   Map<String, String> attributes;

   MyObject(RavensObject object) {
      name = object.getName();
      attributes = new HashMap<>();
      for (Map.Entry<String, String> entry : object.getAttributes().entrySet()) {
         attributes.put(entry.getKey(), entry.getValue());
      }
   }

   boolean isUnchanged(MyObject rhs) {
      // make sure the two maps of attributes are of the same size
      if (attributes.size() != rhs.attributes.size()) {
         return false;
      }
      // for each key in this map, make sure the value for that key in this map matches the value
      // for the same key in the other map
      for (String key : attributes.keySet()) {
         if (!attributes.get(key).equals(rhs.attributes.get(key))) {
            return false;
         }
      }
      // System.out.println(toString(leftObject));
      // System.out.println(toString(rhs));
      System.out.println("Object " + name + " to Object " + rhs.name + " is UNCHANGED.");
      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Object: " + name);
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
         builder.append(", " + entry.getKey() + ": " + entry.getValue());
      }
      return builder.toString();
   }
}
