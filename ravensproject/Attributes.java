package ravensproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
               int left = Integer.parseInt(entry.getValue());
               int right = Integer.parseInt(rhs.map.get(key));
               if (left == right) {
                  value = "unchanged";
               } else {
                  value = Integer.toString(left - right);
               }
            } else if (key.equals("shape")) {
               String left = entry.getValue();
               String right = rhs.map.get("shape");
               if (left.equals(right)) {
                  value = "unchanged";
               } else {
                  value = left;
               }
            } else if (key.equals("size")) {
               String left = entry.getValue();
               String right = rhs.map.get("size");
               if (left.equals(right)) {
                  value = "unchanged";
               } else {
                  value = left;
               }
            } else if (key.equals("fill")) {
               String left = entry.getValue();
               String right = rhs.map.get("fill");
               if (left.equals(right)) {
                  value = "unchanged";
               } else {
                  value = left;
               }
            } else if (key.equals("alignment")) {
               // save the left and right alignment attributes and pass them on for method add() later
               value = entry.getValue() + ";" + rhs.map.get("alignment");
            } else if (entry.getValue().equals(rhs.map.get(key))) {
               // if the left and right attributes are the same, save it
               value = entry.getValue();
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
            String value = "";
            if (key.equals("angle")) {
               if (rhs.map.get("angle").equals("unchanged")) {
                  value = entry.getValue();
               } else {
                  int left = Integer.parseInt(entry.getValue());
                  int right = Integer.parseInt(rhs.map.get(key));
                  int sum;
                  if (map.containsValue("pac-man")) {
                     if (left >= 270 && left < 360) {
                        sum = left - 90;
                     } else {
                        sum = left + 90;
                     }
                  } else {
                     sum = left + right;
                  }
                  value = Integer.toString(sum);
               }
            } else if (key.equals("alignment")) {
               // retrieve the alignment attribute "bottom-right;bottom-left" stored by subtract()
               String addAlignment = rhs.map.get("alignment");
               // split to break the value into 2 alignment attributes: "bottom-right" and "bottom-left"
               String[] splits = addAlignment.split(";");
               // save these 2 alignment attributes plus the current alignment attribute:
               // "bottom-right", "bottom-left", and "top-right"
               List<String> allAlignments = new ArrayList<>();
               for (String split : splits) {
                  allAlignments.add(split);
               }
               allAlignments.add(entry.getValue());

               // break the 3 alignment attributes ("bottom-right", "bottom-left" and "top-right")
               // into 4 labels ("bottom", "top", "right", "left")
               Set<String> left = new HashSet<>();
               Set<String> right = new HashSet<>();
               for (String alignment : allAlignments) {
                  String[] labels = alignment.split("-");
                  left.add(labels[0]);
                  right.add(labels[1]);
               }

               // make up 4 possible attributes out of those 4 labels: "bottom-right", "bottom-left",
               // "top-right", "top-left"
               Map<String, Boolean> map = new HashMap<>();
               for (String leftLabel : left) {
                  for (String rightLabel : right) {
                     String newLabel = leftLabel + "-" + rightLabel;
                     map.put(newLabel, true);
                  }
               }

               // mark the 3 existing attributes "bottom-right", "bottom-left" and "top-right"
               for (String alignment : allAlignments) {
                  map.put(alignment, false);
               }
               // look for the 1 missing attribute, which should be "top-left"
               for (Map.Entry<String, Boolean> entree : map.entrySet()) {
                  if (entree.getValue()) {
                     value = entree.getKey();
                     break;
                  }
               }
            } else if (key.equals("shape")) {
               if (rhs.map.get("shape").equals("unchanged")) {
                  value = entry.getValue();
               } else {
                  value = rhs.map.get("shape");
               }
            } else if (key.equals("fill")) {
               if (rhs.map.get("fill").equals("unchanged")) {
                  value = entry.getValue();
               } else {
                  value = rhs.map.get("fill");
               }
            } else if (key.equals("size")) {
               if (rhs.map.get("size").equals("unchanged")) {
                  value = entry.getValue();
               } else {
                  value = rhs.map.get("size");
               }
            }
            attributes.map.put(key, value);
         }
      }
      return attributes;
   }

   boolean isIdentical(Attributes rhs) {
      // for each key in this map, make sure the value for that key in this map matches the value
      // for the same key in the other map
      for (String key : map.keySet()) {
         if (key.equals("inside")) {
            // skip comparing the "inside" attribute
         } else if (key.equals("above")) {
            // skip comparing the "above" attribute
         } else {
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
