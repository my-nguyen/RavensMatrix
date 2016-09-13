package ravensproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyFigures {
   List<MyFigure> list;

   MyFigures(Map<String, RavensFigure> map, boolean isDesign, String problemType) {
      if (isDesign) {
         list = getDesigns(map, problemType);
      } else {
         list = getChoices(map, problemType);
      }
   }

   // extract the designs: [A, B, C] for 2x2 or [A, B, C, D, E, F, G, H] for 3x3
   List<MyFigure> getDesigns(Map<String, RavensFigure> map, String problemType) {
      List<MyFigure> designs = new ArrayList<>();
      designs.add(new MyFigure(map.get("A")));
      designs.add(new MyFigure(map.get("B")));
      designs.add(new MyFigure(map.get("C")));
      if (problemType.equals("3x3")) {
         designs.add(new MyFigure(map.get("D")));
         designs.add(new MyFigure(map.get("E")));
         designs.add(new MyFigure(map.get("F")));
         designs.add(new MyFigure(map.get("G")));
         designs.add(new MyFigure(map.get("H")));
      }
      return designs;
   }

   // extract the answer choices: [1, 2, 3, 4, 5, 6] for 2x2 or [1, 2, 3, 4, 5, 6, 7, 8] for 3x3
   List<MyFigure> getChoices(Map<String, RavensFigure> map, String problemType) {
      List<MyFigure> choices = new ArrayList<>();
      choices.add(new MyFigure(map.get("1")));
      choices.add(new MyFigure(map.get("2")));
      choices.add(new MyFigure(map.get("3")));
      choices.add(new MyFigure(map.get("4")));
      choices.add(new MyFigure(map.get("5")));
      choices.add(new MyFigure(map.get("6")));
      if (problemType.equals("3x3")) {
         choices.add(new MyFigure(map.get("7")));
         choices.add(new MyFigure(map.get("8")));
      }
      return choices;
   }

   int find(MyFigure figure) {
      for (int i = 0; i < list.size(); i++) {
         if (figure.isUnchanged(list.get(i))) {
            return i;
         }
      }
      return -1;
   }
}
