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
   // Map<String, MyObject> map;
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
      // can I iterate over the tmp List instead of the map?
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

   MyFigure(MyFigure rhs, String name) {
      this.name = name;
      for (MyObject rightObject : rhs.objects) {
         MyObject thisObject = new MyObject(rightObject);
         this.objects.add(thisObject);
      }
   }

   MyFigure add(MyFigure rhs) {
      MyFigure figure = new MyFigure();
      figure.name = "Add";
      figure.objects = new ArrayList<>();
      if (objects.size() == rhs.objects.size()) {
         for (int i = 0; i < objects.size(); i++) {
            MyObject target = objects.get(i).add(rhs.objects.get(i));
            figure.objects.add(target);
         }
      } else {
         // assume rhs.objects.size() is smaller
         for (int i = 0; i < rhs.objects.size(); i++) {
            int index = find(this.objects, rhs.objects.get(i));
            System.out.print("found at index: " + index);
            figure.objects.add(objects.get(index));
         }
      }
      return figure;
   }

   MyFigure generate(MyFigure left, MyFigure right) {
      MyFigure generatedFigure = new MyFigure(this, "generated");
      generatedFigure.objects = new ArrayList<>();

      int thisCount = this.objects.size();
      int leftCount = left.objects.size();
      int rightCount = right.objects.size();
      System.out.println("this: " + thisCount + ", left: " + leftCount + ", right: " + rightCount);
      if (thisCount == leftCount) {
         if (thisCount == rightCount) {
            for (int i = 0; i < objects.size(); i++) {
               MyObject thisObject = this.objects.get(i);
               MyObject leftObject = left.objects.get(i);
               MyObject rightObject = right.objects.get(i);
               MyObject generatedObject = thisObject.generate(leftObject, rightObject);
               generatedFigure.objects.add(generatedObject);
            }
         } else {
            System.out.println("this: " + thisCount + ", right: " + rightCount);
         }
      } else {
         List<MyObject> matchedObjects = new ArrayList<>();
         if (thisCount > leftCount) {
            List<MyObject> tmpObjects = new ArrayList<>(this.objects);
            for (int i = 0; i < leftCount; i++) {
               MyObject leftObject = left.objects.get(i);
               int index = find(tmpObjects, leftObject);
               MyObject thisObject = tmpObjects.remove(index);
               System.out.print(i + " matched object: " + thisObject);
               matchedObjects.add(thisObject);
            }

            for (int i = 0; i < matchedObjects.size(); i++) {
               MyObject matchedObject = matchedObjects.get(i);
               MyObject leftObject = left.objects.get(i);
               MyObject rightObject = right.objects.get(i);
               System.out.print("matched object: " + matchedObject);
               System.out.print("left object: " + leftObject);
               System.out.print("right object: " + rightObject);
               MyObject generatedObject = matchedObject.generate(leftObject, rightObject);
               System.out.print("generated object: " + generatedObject);
               generatedFigure.objects.add(generatedObject);
            }
            for (int i = 0; i < tmpObjects.size(); i++) {
               generatedFigure.objects.add(tmpObjects.get(i));
               System.out.print("adding generated object: " + tmpObjects.get(i));
            }
         } else {
            System.out.println("am i here???");
         }
      }
      System.out.print("generated: " + generatedFigure);
      return generatedFigure;
   }

   boolean isIdentical(MyFigure rhs) {
      for (int i = 0; i < objects.size(); i++) {
         if (!objects.get(i).isIdentical(rhs.objects.get(i))) {
            return false;
         }
      }
      return true;
   }

   int find(List<MyObject> list, MyObject target) {
      for (int i = 0; i < list.size(); i++) {
         MyObject object = list.get(i);
         if (object.match(target)) {
            System.out.print("Figure-find " + i + ": " + object);
            return i;
         }
      }
      System.out.println("Not found");
      return -1;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Figure<name:").append(name).append("\n");
      for (MyObject object : objects) {
         builder.append(object);
      }
      builder.append(">");
      return builder.toString();
   }
}
