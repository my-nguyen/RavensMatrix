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

   MyObject(String name, Attributes attributes) {
      this.name = name;
      this.attributes = attributes;
   }

   MyObject subtract(MyObject rhs) {
      return new MyObject("Subtract", attributes.subtract(rhs.attributes));
   }

   MyObject add(MyObject rhs) {
      return new MyObject("Add", attributes.add(rhs.attributes));
   }

   int index() {
      String inside = attributes.map.get("inside");
      int result = (inside == null ? 0 : inside.split(",").length);
      return result;
   }

   boolean isIdentical(MyObject rhs) {
      // System.out.print("isIdentical, left: " + this);
      // System.out.print("isIdentical, right: " + rhs);
      return attributes.isIdentical(rhs.attributes);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Object<name:" + name + ", ");
      builder.append(attributes);
      // builder.append(dynamicAttributes);
      builder.append(">");
      return builder.toString();
   }
}
