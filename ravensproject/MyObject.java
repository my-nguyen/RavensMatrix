package ravensproject;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   Attributes attributes;
   MyObject above;
   MyObject inside;

   MyObject(MyObject object) {
      name = object.name;
      attributes = new Attributes(object.attributes);
   }
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

   MyObject generate(MyObject left, MyObject right) {
      MyObject object = new MyObject("generated", this.attributes.generate(left.attributes, right.attributes));
      return object;
   }

   int index() {
      String inside = attributes.map.get("inside");
      int result = (inside == null ? 0 : inside.split(",").length);
      return result;
   }

   boolean isIdentical(MyObject rhs) {
      return attributes.isIdentical(rhs.attributes);
   }

   boolean match(MyObject target) {
      boolean size = attributes.map.get("size").equals(target.attributes.map.get("size"));
      boolean fill = attributes.map.get("fill").equals(target.attributes.map.get("fill"));
      return size && fill;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("\tObject<name:" + name + ", ");
      builder.append(attributes);
      builder.append(">\n");
      return builder.toString();
   }
}
