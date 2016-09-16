package ravensproject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class Attributes {
   Map<String, String> map;

   Attributes() {
      map = new LinkedHashMap<>();
   }

   Attributes(Map<String, String> rhs) {
      this();
      // copy the whole contents of the rhs map over to member map
      for (Map.Entry<String, String> entry : rhs.entrySet()) {
         map.put(entry.getKey(), entry.getValue());
      }
   }

   Attributes subtract(Attributes rhs) {
      Attributes attributes = new Attributes();
      for (Map.Entry<String, String> entry : map.entrySet()) {
         String key = entry.getKey();
         if (!rhs.map.containsKey(key) || key.equals("inside")) {
            // ignore keys that don't exist in the second map and the "inside" attribute
            ;
         } else {
            String value;
            if (key.equals("angle")) {
               // if the attribute is angle, calculate the difference and save it
               try {
                  int left = Integer.parseInt(entry.getValue());
                  int right = Integer.parseInt(rhs.map.get(key));
                  value = Integer.toString(left - right);
                  System.out.println("left: " + left + ", right: " + right + ", subtract: " + value);
               } catch (NumberFormatException e) {
                  value = "0";
               }
            } else if (entry.getValue().equals(rhs.map.get(key))) {
               // if the left and right attributes are the same, save it
               value = entry.getValue();
            } else if (key.equals("shape") || key.equals("size") || key.equals("fill")) {
               // if the attribute is shape, or size, or fill, save the rhs value
               value = rhs.map.get(key);
            } else if (key.equals("alignment")) {
               // what to do????
               value = "";
            } else {
               value = "";
            }
            attributes.map.put(key, value);
         }
      }
      return attributes;
   }

   Attributes add(Attributes rhs) {
      Attributes attributes = new Attributes();
      for (Map.Entry<String, String> entry : map.entrySet()) {
         String key = entry.getKey();
         if (!rhs.map.containsKey(key) || key.equals("inside")) {
            ;
         } else {
            String value = entry.getValue();
            if (key.equals("angle")) {
               try {
                  int left = Integer.parseInt(entry.getValue());
                  int right = Integer.parseInt(rhs.map.get(key));
                  value = Integer.toString(left + right);
               } catch(NumberFormatException e) {
                  value = "0";
               }
            }
            attributes.map.put(key, value);
         }
      }
      return attributes;
   }

   boolean isIdentical(Attributes rhs) {
      // make sure the two maps of attributes are of the same size
      // for each key in this map, make sure the value for that key in this map matches the value
      // for the same key in the other map
      for (String key : map.keySet()) {
         // skip comparing the "inside" attribute
         if (!key.equals("inside")) {
            if (!map.get(key).equals(rhs.map.get(key))) {
               return false;
            }
         }
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Attributes<");
      for (Map.Entry<String, String> entry : map.entrySet()) {
         builder.append(entry.getKey() + ":" + entry.getValue() + ", ");
      }
      builder.append(">");
      return builder.toString();
   }
}
