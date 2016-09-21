package ravensproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyFigure {
   String name;
   List<MyObject> objects;

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

   MyFigure(MyFigure rightFigure, String name) {
      this.name = name;
      this.objects = new ArrayList<>();
      for (MyObject rightObject : rightFigure.objects) {
         MyObject thisObject = new MyObject(rightObject, "generated");
         this.objects.add(thisObject);
      }
      // need to generate "inside" attribute also
   }

   MyFigure generate(MyFigure leftFigure, MyFigure rightFigure) {
      MyFigure generatedFigure = new MyFigure(this, "generated");
      // System.out.print("copied figure: " + generatedFigure);

      Map<MyObject, MyObject> generatedToLeft = transform(generatedFigure.objects, leftFigure.objects);
      Map<MyObject, MyObject> leftToRight = transform(generatedToLeft, leftFigure.objects, rightFigure.objects);
      // System.out.println("generatedToLeft size: " + generatedToLeft.size());
      // System.out.println("leftToRight size: " + leftToRight.size());

      // for (int i = 0; i < generatedFigure.objects.size(); i++) {
      ListIterator iterator = generatedFigure.objects.listIterator();
      while (iterator.hasNext()) {
         MyObject generatedObject = (MyObject)iterator.next();
         // System.out.print("generated: " + generatedObject);
         MyObject leftObject = generatedToLeft.get(generatedObject);
         // System.out.print("left: " + leftObject);
         if (leftObject != null) {
            MyObject rightObject = leftToRight.get(leftObject);
            // System.out.print("right: " + rightObject);
            if (rightObject == null) {
               iterator.remove();
            } else if (!leftObject.equals(rightObject)) {
               iterator.set(rightObject);
            }
         }
      }
      System.out.print("Generated : " + generatedFigure);
      return generatedFigure;
   }

   Map<MyObject, MyObject> transform(List<MyObject> firstObjects, List<MyObject> secondObjects) {
      Map<MyObject, MyObject> firstToSecond = new HashMap<>();
      int firstIndex = -1;
      int secondIndex = -1;
      boolean foundFirstMatch = false;
      // look for a first match between an Object in the first list and another Object in the second
      for (int i = 0; i < firstObjects.size(); i++) {
         MyObject firstObject = firstObjects.get(i);
         for (int j = 0; j < secondObjects.size(); j++) {
            MyObject secondObject = secondObjects.get(j);
            if (firstObject.match(secondObject)) {
               firstToSecond.put(firstObject, secondObject);
               firstIndex = i;
               secondIndex = j;
               foundFirstMatch = true;
               break;
            }
         }
         if (foundFirstMatch) {
            break;
         }
      }

      if (foundFirstMatch) {
         // map the matches down the child path
         for (int i = firstIndex + 1, j = secondIndex + 1; i < firstObjects.size() && j < secondObjects.size(); i++, j++) {
            firstToSecond.put(firstObjects.get(i), secondObjects.get(j));
         }
         // map the matches up the parent path
         for (int i = firstIndex - 1, j = secondIndex - 1; i >= 0 && j >= 0; i--, j--) {
            firstToSecond.put(firstObjects.get(i), secondObjects.get(j));
         }
      }

      return firstToSecond;
   }

   Map<MyObject, MyObject> transform(Map<MyObject, MyObject> map, List<MyObject> firstObjects, List<MyObject> secondObjects) {
      Map<MyObject, MyObject> firstToSecond = new HashMap<>();
      int secondIndex = -1;
      MyObject firstObject = null;
      for (MyObject mapValue : map.values()) {
         for (int i = 0; i < secondObjects.size(); i++) {
            MyObject secondObject = secondObjects.get(i);
            if (mapValue.match(secondObject)) {
               firstObject = mapValue;
               firstToSecond.put(firstObject, secondObject);
               secondIndex = i;
               break;
            }
         }
         if (firstObject != null) {
            break;
         }
      }

      if (firstObject != null) {
         // reverse look up the first index based on the sole object in the map
         int firstIndex = find(firstObjects, firstObject);
         // map the matches down the child path
         for (int i = firstIndex+1, j = secondIndex+1; i < firstObjects.size() && j < secondObjects.size(); i++, j++) {
            firstToSecond.put(firstObjects.get(i), secondObjects.get(j));
         }
         // map the matches up the parent path
         for (int i = firstIndex-1, j = secondIndex-1; i >= 0 && j >= 0; i--, j--) {
            firstToSecond.put(firstObjects.get(i), secondObjects.get(j));
         }
      }

      return firstToSecond;
   }

   int find(List<MyObject> list, MyObject target) {
      for (int i = 0; i < list.size(); i++) {
         MyObject object = list.get(i);
         // object could be null???
         if (object != null && object.equals(target)) {
            // System.out.print("Figure-find " + i + ": " + object);
            return i;
         }
      }
      System.out.println("Not found");
      return -1;
   }

   @Override
   public boolean equals(Object obj) {
      MyFigure rightFigure = (MyFigure)obj;
      if (this.objects.size() != rightFigure.objects.size())
         return false;

      for (int i = 0; i < this.objects.size(); i++) {
         if (!this.objects.get(i).equals(rightFigure.objects.get(i))) {
            return false;
         }
      }
      return true;
   }

   @Override
   public int hashCode() {
      int result = 17;
      for (MyObject object : objects) {
         result = 31*result + object.hashCode();
      }
      return result;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Figure<name:").append(name);
      builder.append(", ").append(objects.size()).append(" objects:");
      builder.append("\n");
      for (MyObject object : objects) {
         builder.append(object);
      }
      builder.append(">");
      return builder.toString();
   }
}
