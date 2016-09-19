package ravensproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyFigure {
   String name;
   // do I need a Map<String, MyObject> ????
   List<MyObject> objects;

   MyFigure() {
   }

   MyFigure(RavensFigure figure) {
      name = figure.getName();

      // construct a temporary List<MyObject> based on the RavensFigure.HashMap<String, RavensObject>
      List<MyObject> tmp = new ArrayList<>();
      for (RavensObject value : figure.getObjects().values()) {
         MyObject object = new MyObject(value);
         tmp.add(object);
      }

      boolean containsInside = false;
      for (RavensObject value : figure.getObjects().values()) {
         // check if any RavensObject contains the attribute "inside"
         if (value.getAttributes().containsKey("inside")) {
            containsInside = true;
            break;
         }
      }

      // System.out.println("contains inside? " + containsInside);
      Map<Integer, MyObject> indices = new HashMap<>();
      if (containsInside) {
         // if the attribute "inside" exists, then for each MyObject in the temporary list,
         // calculate the MyObject index and pair the index with that MyObject to store in a
         // temporary map
         for (MyObject object : tmp) {
            int index = object.index();
            indices.put(index, object);
         }
         // insert the MyObjects from the temporary map into objects based on the calculated indices
         objects = new ArrayList<>();
         for (int i = 0; i < indices.size(); i++) {
            objects.add(indices.get(i));
            System.out.println("added at " + i + " object: " + indices.get(i));
         }
      } else {
         // check if any RavensObject contains the attribute "above"
         RavensObject aboveObject = null;
         for (RavensObject value : figure.getObjects().values()) {
            if (value.getAttributes().containsKey("above")) {
               aboveObject = value;
               break;
            }
         }

         if (aboveObject != null) {
            String belowName = aboveObject.getAttributes().get("above");
            RavensObject belowObject = figure.getObjects().get(belowName);
            MyObject above = new MyObject(aboveObject);
            MyObject below = new MyObject(belowObject);
            above.above = below;
            objects = new ArrayList<>();
            objects.add(above);
            objects.add(below);
         } else {
            // copy the temporary list created earlier
            objects = tmp;
         }
      }
   }

   MyFigure subtract(MyFigure rhs) {
      MyFigure figure = new MyFigure();
      figure.name = "Subtract";
      figure.objects = new ArrayList<>();
      for (int i = 0; i < objects.size(); i++) {
         MyObject object = objects.get(i).subtract(rhs.objects.get(i));
         figure.objects.add(object);
      }
      return figure;
   }

   MyFigure add(MyFigure rhs) {
      MyFigure figure = new MyFigure();
      figure.name = "Add";
      figure.objects = new ArrayList<>();
      for (int i = 0; i < objects.size(); i++) {
         MyObject target = objects.get(i).add(rhs.objects.get(i));
         figure.objects.add(target);
      }
      return figure;
   }

   boolean isIdentical(MyFigure rhs) {
      for (int i = 0; i < objects.size(); i++) {
         if (!objects.get(i).isIdentical(rhs.objects.get(i))) {
            return false;
         }
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Figure<name:").append(name).append(", ");
      for (MyObject object : objects) {
         builder.append(object).append(", ");
      }
      builder.append(">");
      return builder.toString();
   }
}
