package ravensproject;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   Attributes attributes;

   MyObject(RavensObject object) {
      name = object.getName();
      attributes = new Attributes(object.getAttributes());
   }

   boolean isUnchanged(MyObject rhs) {
      boolean unchanged = attributes.isUnchanged(rhs.attributes.map);
      if (unchanged) {
         // System.out.println(toString(leftObject));
         // System.out.println(toString(rhs));
         System.out.println("Object " + name + " to Object " + rhs.name + " is UNCHANGED.");
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Object: " + name);
      builder.append(attributes);
      return builder.toString();
   }
}
