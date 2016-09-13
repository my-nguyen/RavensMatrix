package ravensproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class Patterns {
   List<MyFigure> list;

   // extract the designs: [A, B, C] for 2x2 or [A, B, C, D, E, F, G, H] for 3x3
   Patterns(Map<String, RavensFigure> map, String problemType) {
      list = new ArrayList<>();
      list.add(new MyFigure(map.get("A")));
      list.add(new MyFigure(map.get("B")));
      list.add(new MyFigure(map.get("C")));
      if (problemType.equals("3x3")) {
         list.add(new MyFigure(map.get("D")));
         list.add(new MyFigure(map.get("E")));
         list.add(new MyFigure(map.get("F")));
         list.add(new MyFigure(map.get("G")));
         list.add(new MyFigure(map.get("H")));
      }
   }
}