package ravensproject;

import java.util.Collection;

/**
 * Created by My on 9/13/2016.
 */
public class Utils {
   static boolean containsObject(Collection<MyObject> collection, MyObject object) {
      for (MyObject iterator : collection) {
         /*
         if (object.isUnchanged(iterator)) {
            return true;
         }
         */
      }
      return false;
   }
}
