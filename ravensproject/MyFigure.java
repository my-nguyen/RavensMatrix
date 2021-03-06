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
   boolean containsInside = false;

   MyFigure(RavensFigure figure) {
      init(figure.getName());

      // construct a temporary List<MyObject> based on the RavensFigure.HashMap<String, RavensObject>
      List<MyObject> tmp = new ArrayList<>();
      for (RavensObject value : figure.getObjects().values()) {
         MyObject object = new MyObject(value);
         tmp.add(object);
      }

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

   MyFigure(String name, MyFigure rightFigure) {
      init(name);
      for (MyObject rightObject : rightFigure.objects) {
         MyObject thisObject = new MyObject("generated", rightObject.attributes);
         this.objects.add(thisObject);
      }
      // need to generate "inside" attribute also
   }

   MyFigure(String name) {
      init(name);
   }

   void init(String name) {
      this.name = name;
      this.objects = new ArrayList<>();
   }

   class Pair {
      int left;
      int right;
      Pair(int lhs, int rhs) {
         left = lhs;
         right = rhs;
      }
   }

   MyFigure generate(MyFigure leftFigure, MyFigure rightFigure) {
      MyFigure generatedFigure;
      if (containsInside) {
         // copy all MyObjects from this figure to the generated figure
         generatedFigure = new MyFigure("generated", this);
         Pair pair = firstMatch(generatedFigure.objects, leftFigure.objects);
         Map<MyObject, MyObject> generatedToLeft = null;
         if (pair != null) {
            // map all matching objects from generatedFigure to leftFigure
            generatedToLeft = map(pair, generatedFigure.objects, leftFigure.objects);
            Pair indices = firstMatch(generatedToLeft, leftFigure.objects, rightFigure.objects);
            if (indices != null) {
               Map<MyObject, MyObject> leftToRight = null;
               // map all such matching objects from leftFigure to rightFigure
               leftToRight = map(indices, leftFigure.objects, rightFigure.objects);

               // collect all objects in rightFigure that map to those in leftFigure
               ListIterator iterator = generatedFigure.objects.listIterator();
               while (iterator.hasNext()) {
                  MyObject generatedObject = (MyObject) iterator.next();
                  MyObject leftObject = generatedToLeft.get(generatedObject);
                  // only process those objects that exist in the generatedToLeft map
                  if (leftObject != null) {
                     // map an object in generatedFigure to an object in rightFigure
                     MyObject rightObject = leftToRight.get(leftObject);
                     if (rightObject == null) {
                        // remove object from generatedFigure if there's no mapping to rightFigure
                        iterator.remove();
                     } else if (!leftObject.equals(rightObject)) {
                        // replace object in generatedFigure if there's a mapping to rightFigure
                        iterator.set(rightObject);
                     }
                  }
               }

               // collect all objects in rightFigure that don't map to those in leftFigure
               Map<Integer, Boolean> map = match(indices, leftFigure.objects, rightFigure.objects);
               for (int i = 0; i < rightFigure.objects.size(); i++) {
                  if (!map.containsKey(i)) {
                     MyObject object = new MyObject(rightFigure.objects.get(i));
                     generatedFigure.objects.add(object);
                  }
               }
            }
         }
      } else {
         generatedFigure = new MyFigure("generated");
         for (int i = 0; i < objects.size(); i++) {
            MyObject thisObject = this.objects.get(i);
            MyObject leftObject = leftFigure.objects.get(i);
            MyObject rightObject = rightFigure.objects.get(i);
            MyObject generatedObject = thisObject.generate(leftObject, rightObject);
            generatedFigure.objects.add(generatedObject);
         }
      }
      // System.out.print("Generated : " + generatedFigure);
      return generatedFigure;
   }

   Pair firstMatch(List<MyObject> firstObjects, List<MyObject> secondObjects) {
      // look for a first match between an Object in the first list and another Object in the second
      for (int i = 0; i < firstObjects.size(); i++) {
         MyObject firstObject = firstObjects.get(i);
         for (int j = 0; j < secondObjects.size(); j++) {
            MyObject secondObject = secondObjects.get(j);
            if (firstObject.match(secondObject)) {
               return new Pair(i, j);
            }
         }
      }
      return null;
   }

   Map<MyObject, MyObject> map(Pair indices, List<MyObject> firstObjects, List<MyObject> secondObjects) {
      Map<MyObject, MyObject> firstToSecond = new HashMap<>();
      firstToSecond.put(firstObjects.get(indices.left), secondObjects.get(indices.right));

      // map the matches down the child path
      for (int i = indices.left + 1, j = indices.right + 1; i < firstObjects.size() && j < secondObjects.size(); i++, j++) {
         firstToSecond.put(firstObjects.get(i), secondObjects.get(j));
      }
      // map the matches up the parent path
      for (int i = indices.left - 1, j = indices.right - 1; i >= 0 && j >= 0; i--, j--) {
         firstToSecond.put(firstObjects.get(i), secondObjects.get(j));
      }

      return firstToSecond;
   }

   Map<Integer, Boolean> match(Pair indices, List<MyObject> firstObjects, List<MyObject> secondObjects) {
      Map<Integer, Boolean> map = new HashMap<>();
      map.put(indices.right, true);

      // map the matches down the child path
      for (int i = indices.left + 1, j = indices.right + 1; i < firstObjects.size() && j < secondObjects.size(); i++, j++) {
         map.put(j, true);
      }
      // map the matches up the parent path
      for (int i = indices.left - 1, j = indices.right - 1; i >= 0 && j >= 0; i--, j--) {
         map.put(j, true);
      }

      return map;
   }

   Pair firstMatch(Map<MyObject, MyObject> generatedToLeft, List<MyObject> firstObjects, List<MyObject> secondObjects) {
      for (MyObject mapValue : generatedToLeft.values()) {
         for (int i = 0; i < secondObjects.size(); i++) {
            MyObject secondObject = secondObjects.get(i);
            if (mapValue.match(secondObject)) {
               // reverse look up the first index based on the sole object in the map
               int firstIndex = find(firstObjects, mapValue);
               int secondIndex = i;
               return new Pair(firstIndex, secondIndex);
            }
         }
      }
      return null;
   }

   int find(List<MyObject> list, MyObject target) {
      for (int i = 0; i < list.size(); i++) {
         MyObject object = list.get(i);
         // object could be null???
         if (object.equals(target)) {
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
