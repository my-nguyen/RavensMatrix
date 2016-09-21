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

   Attributes(Attributes rhs) {
      this();
      for (Map.Entry<String, String> entry : rhs.map.entrySet()) {
         map.put(entry.getKey(), entry.getValue());
      }
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
               for (String leftPosition : left) {
                  for (String rightPosition : right) {
                     String newPosition = leftPosition + "-" + rightPosition;
                     map.put(newPosition, true);
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
            } else {
               System.out.println("unprocessed attribute: (" + key + ", " + entry.getValue());
            }
            attributes.map.put(key, value);
         }
      }
      return attributes;
   }

   Attributes generate(Attributes left, Attributes right) {
      Attributes generatedAttributes = new Attributes();
      for (Map.Entry<String, String> entry : map.entrySet()) {
         String key = entry.getKey();
         if (!left.map.containsKey(key)) {
         } else if (!right.map.containsKey(key)) {
         } else if (key.equals("inside")) {
         } else {
            String thisValue = this.map.get(key);
            String leftValue = left.map.get(key);
            String rightValue = right.map.get(key);
            String generatedValue = "";

            if (key.equals("angle")) {
               int thisInt = Integer.parseInt(thisValue);
               int leftInt = Integer.parseInt(leftValue);
               int rightInt = Integer.parseInt(rightValue);

               int generatedInt;
               if (map.containsKey("shape") && map.get("shape").equals("pac-man")) {
                  int[] list = new int[3];
                  list[0] = thisInt;
                  list[1] = leftInt;
                  list[2] = rightInt;
                  Arrays.sort(list);

                  if (list[0] > 90) {
                     generatedInt = list[0] - 90;
                  } else {
                     if (list[1] - list[0] > 90) {
                        generatedInt = list[0] + 90;
                     } else {
                        generatedInt = list[1] + 90;
                     }
                  }
               } else {
                  int diffInt = rightInt - leftInt;
                  generatedInt = thisInt + diffInt;
               }
               generatedValue = Integer.toString(generatedInt);
            } else if (key.equals("alignment")) {
               // save the current 3 alignments "bottom-right", "bottom-left" and "top-right"
               List<String> allAlignments = new ArrayList<>();
               allAlignments.add(thisValue);
               allAlignments.add(leftValue);
               allAlignments.add(rightValue);

               // break those 3 alignment attributes into 2 sets of 2 positions each:
               // {"bottom", "top"} and {"right", "left"}
               Set<String> leftPositions = new HashSet<>();
               Set<String> rightPositions = new HashSet<>();
               for (String alignment : allAlignments) {
                  String[] positions = alignment.split("-");
                  leftPositions.add(positions[0]);
                  rightPositions.add(positions[1]);
               }

               // make up 4 possible attributes out of those 4 labels: "bottom-right", "bottom-left",
               // "top-right", "top-left"
               Map<String, Boolean> allPositions = new HashMap<>();
               for (String leftPosition : leftPositions) {
                  for (String rightPosition : rightPositions) {
                     String combinedPosition = leftPosition + "-" + rightPosition;
                     allPositions.put(combinedPosition, true);
                  }
               }

               // mark the 3 existing attributes "bottom-right", "bottom-left" and "top-right"
               for (String alignment : allAlignments) {
                  allPositions.put(alignment, false);
               }
               // look for the 1 missing attribute, which should be "top-left"
               for (Map.Entry<String, Boolean> position : allPositions.entrySet()) {
                  if (position.getValue()) {
                     generatedValue = position.getKey();
                     break;
                  }
               }
            } else if (key.equals("size") || key.equals("shape") || key.equals("fill")) {
               if (leftValue.equals(rightValue)) {
                  generatedValue = thisValue;
               } else {
                  generatedValue = rightValue;
               }
            }

            generatedAttributes.map.put(key, generatedValue);
         }
      }
      return generatedAttributes;
   }

   boolean match(Attributes rhs) {
      boolean equalSize = map.get("size").equals(rhs.map.get("size"));
      boolean equalShape = map.get("shape").equals(rhs.map.get("shape"));
      boolean equalFill = map.get("fill").equals(rhs.map.get("fill"));
      return equalSize && equalShape && equalFill;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this)
         return true;
      if (!(object instanceof Attributes))
         return false;

      Attributes rhs = (Attributes)object;
      boolean equalAngle = true;
      if (map.containsKey("angle") && rhs.map.containsKey("angle")) {
         equalAngle = map.get("angle").equals(rhs.map.get("angle"));
      }
      boolean equalAlignment = true;
      if (map.containsKey("alignment") && rhs.map.containsKey("alignment")) {
         equalAlignment = map.get("alignment").equals(rhs.map.get("alignment"));
      }
      return match(rhs) && equalAngle && equalAlignment;
   }

   @Override
   public int hashCode() {
      int result = 17;
      result = 31*result + map.get("size").hashCode();
      result = 31*result + map.get("shape").hashCode();
      result = 31*result + map.get("fill").hashCode();
      return result;
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
