package ravensproject;

/**
 * Created by My on 9/13/2016.
 */
public class MyObject {
   String name;
   Attributes attributes;
   MyObject above;

   MyObject(RavensObject object) {
      this.name = object.getName();
      this.attributes = new Attributes(object.getAttributes());
   }

   MyObject(String name, Attributes attributes) {
      init(name, attributes);
   }

   MyObject(MyObject rhs) {
      init(rhs.name, rhs.attributes);
   }

   void init(String name, Attributes attributes) {
      this.name = name;
      this.attributes = new Attributes(attributes);
   }

   MyObject generate(MyObject left, MyObject right) {
      MyObject object = new MyObject("generated", this.attributes.generate(left.attributes, right.attributes));
      return object;
   }

   int index() {
      String inside = attributes.get("inside");
      int result = (inside == null ? 0 : inside.split(",").length);
      return result;
   }

   boolean match(MyObject rhs) {
      return attributes.match(rhs.attributes);
   }

   @Override
   public boolean equals(Object obj) {
      return attributes.equals(((MyObject)obj).attributes);
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
