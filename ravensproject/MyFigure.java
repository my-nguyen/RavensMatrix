package ravensproject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyFigure {
   String name;
   Map<String, MyObject> objects;
   String visualFilename;

   MyFigure(RavensFigure figure) {
      name = figure.getName();
      objects = new HashMap<>();
      for (Map.Entry<String, RavensObject> entry : figure.getObjects().entrySet()) {
         MyObject object = new MyObject(entry.getValue());
         objects.put(entry.getKey(), object);
      }
      visualFilename = figure.getVisual();
   }

   boolean isUnchanged(MyFigure rhs) {
      // make sure the 2 Figures contain the same number of Objects
      if (objects.size() != rhs.objects.size()) {
         return false;
      }
      for (MyObject object : objects.values()) {
         if (!Utils.containsObject(rhs.objects.values(), object)) {
            return false;
         }
         /*
         for (MyObject iterator : rhs.objects.values()) {
            if (object.isUnchanged(iterator)) {
               return true;
            }
         }
         */
      }
      System.out.println("Figure " + name + " to Figure " + rhs.name + " is UNCHANGED.");
      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, MyObject> entry : objects.entrySet()) {
         builder.append("Figure: " + name + ", " + entry.getValue());
      }
      return builder.toString();
   }
}
