package ravensproject;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   DynamicAttributes dynamicAttributes;
   StaticAttributes staticAttributes;

   MyObject(RavensObject object) {
      name = object.getName();
      dynamicAttributes = new DynamicAttributes(object.getAttributes());
      staticAttributes = new StaticAttributes(object.getAttributes());
   }

   boolean isUnchanged(MyObject rhs) {
      if (staticAttributes.isUnchanged(rhs.staticAttributes.map)) {
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
      builder.append(staticAttributes);
      builder.append(dynamicAttributes);
      builder.append("\n");
      return builder.toString();
   }
}
