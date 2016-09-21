package ravensproject;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   Attributes attributes;
   MyObject above;

   MyObject(MyObject object, String name) {
      this.name = name;
      this.attributes = new Attributes(object.attributes);
   }
   MyObject(RavensObject object) {
      name = object.getName();
      attributes = new Attributes(object.getAttributes());
   }

   MyObject(String name, Attributes attributes) {
      this.name = name;
      this.attributes = attributes;
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

   // match() differs from equals() in that match() skips comparing the angle attribute
   boolean match(MyObject rhs) {
      return attributes.match(rhs.attributes);
   }

   @Override
   public boolean equals(Object obj) {
      return obj != null && attributes.equals(((MyObject)obj).attributes);
   }

   @Override
   public int hashCode() {
      return attributes.hashCode();
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
