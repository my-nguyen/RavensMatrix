package ravensproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by My on 9/13/2016.
 */
public class Attributes extends HashMap<String, String> {
   Attributes() {
      super();
   }

   Attributes(Attributes rhs) {
      super();
      for (Map.Entry<String, String> entry : rhs.entrySet()) {
         put(entry.getKey(), entry.getValue());
      }
   }

   Attributes(Map<String, String> rhs) {
      super();
      // copy the whole contents of the rhs map over to member map
      for (Map.Entry<String, String> entry : rhs.entrySet()) {
         put(entry.getKey(), entry.getValue());
      }
   }

   Attributes generate(Attributes left, Attributes right) {
      Attributes generatedAttributes = new Attributes();
      for (Map.Entry<String, String> entry : entrySet()) {
         String key = entry.getKey();
         if (!left.containsKey(key)) {
         } else if (!right.containsKey(key)) {
         } else if (key.equals("inside")) {
         } else {
            String thisValue = this.get(key);
            String leftValue = left.get(key);
            String rightValue = right.get(key);
            String generatedValue = "";

            if (key.equals("angle")) {
               int thisInt = Integer.parseInt(thisValue);
               int leftInt = Integer.parseInt(leftValue);
               int rightInt = Integer.parseInt(rightValue);

               int generatedInt;
               if (containsKey("shape") && get("shape").equals("pac-man")) {
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

            generatedAttributes.put(key, generatedValue);
         }
      }
      return generatedAttributes;
   }

   // match() differs from equals() in that match() doesn't compare the angle and alignment
   // attributes, while equals() does.
   boolean match(Attributes rhs) {
      boolean equalSize = get("size").equals(rhs.get("size"));
      boolean equalShape = get("shape").equals(rhs.get("shape"));
      boolean equalFill = get("fill").equals(rhs.get("fill"));
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
      if (containsKey("angle") && rhs.containsKey("angle")) {
         equalAngle = get("angle").equals(rhs.get("angle"));
      }
      boolean equalAlignment = true;
      if (containsKey("alignment") && rhs.containsKey("alignment")) {
         equalAlignment = get("alignment").equals(rhs.get("alignment"));
      }
      return match(rhs) && equalAngle && equalAlignment;
   }

   @Override
   public int hashCode() {
      int result = 17;
      result = 31*result + get("size").hashCode();
      result = 31*result + get("shape").hashCode();
      result = 31*result + get("fill").hashCode();
      return result;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Attributes<");
      for (Map.Entry<String, String> entry : entrySet()) {
         builder.append(entry.getKey() + ":" + entry.getValue() + ", ");
      }
      builder.append(">");
      return builder.toString();
   }
}
