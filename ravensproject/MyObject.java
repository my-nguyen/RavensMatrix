package ravensproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   Attributes attributes;
   List<MyObject> insides;

   MyObject(RavensObject object) {
      name = object.getName();
      attributes = new Attributes(object.getAttributes());
   }

   void initInsides(Map<String, MyObject> objectsInFigure) {
      insides = new ArrayList<>();
      String[] keys = { "inside" };
      for (String key : keys) {
         String value = attributes.map.get(key);
         if (value != null) {
            for (String token : value.split(",")) {
               MyObject inside = objectsInFigure.get(token);
               // System.out.print("this: " + value + " inside: " + inside);
               insides.add(inside);
            }
         }
      }
   }

   boolean isUnchanged(MyObject rhs) {
      System.out.print("isUnchanged, left: " + this);
      System.out.print("isUnchanged, right: " + rhs);
      // make sure the attributes are the same in both Objects
      if (attributes.isUnchanged(rhs.attributes.map)) {
         // make sure the two lists of "inside" attributes are of the same size
         if (insides.size() != rhs.insides.size()) {
            return false;
         } else {
            for (int i = 0; i < insides.size(); i++) {
               // make sure all the Objects the current Object is inside of match those of the
               // second Object
               System.out.println("making a recursive call");
               if (!insides.get(i).isUnchanged(rhs.insides.get(i))) {
                  return false;
               }
            }
            System.out.println("Object " + name + " to Object " + rhs.name + " is UNCHANGED.");
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Object: " + name);
      builder.append(attributes);
      // builder.append(dynamicAttributes);
      builder.append("\n");
      return builder.toString();
   }
}
