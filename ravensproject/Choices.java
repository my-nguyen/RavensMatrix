package ravensproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class Choices {
   List<MyFigure> list;

   // extract the answer list: [1, 2, 3, 4, 5, 6] for 2x2 or [1, 2, 3, 4, 5, 6, 7, 8] for 3x3
   Choices(Map<String, RavensFigure> map, String problemType) {
      list = new ArrayList<>();
      list.add(new MyFigure(map.get("1")));
      list.add(new MyFigure(map.get("2")));
      list.add(new MyFigure(map.get("3")));
      list.add(new MyFigure(map.get("4")));
      list.add(new MyFigure(map.get("5")));
      list.add(new MyFigure(map.get("6")));
      if (problemType.equals("3x3")) {
         list.add(new MyFigure(map.get("7")));
         list.add(new MyFigure(map.get("8")));
      }
   }

   int find(MyFigure figure) {
      for (int i = 0; i < list.size(); i++) {
         System.out.print(list.get(i));
         if (figure.isUnchanged(list.get(i))) {
            return i;
         }
      }
      return -1;
   }
}
