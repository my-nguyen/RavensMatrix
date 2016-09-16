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

      // System.out.println("Figure " + name + ", " + figure.getObjects().size() + " Objects");
      // transfer the Map<String, RavensObject> from RavensFigure to a temporary Map<Integer, MyObject>
      // based on the "inside" attribute
      Map<Integer, MyObject> indices = new HashMap<>();
      for (Map.Entry<String, RavensObject> entry : figure.getObjects().entrySet()) {
         MyObject value = new MyObject(entry.getValue());
         int index = value.index();
         indices.put(index, value);
      }
      // set up
      objects = new ArrayList<>();
      for (int i = 0; i < indices.size(); i++) {
         objects.add(indices.get(i));
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
      // System.out.print("LEFT: " + this);
      // System.out.print("RIGHT: " + rhs);
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
