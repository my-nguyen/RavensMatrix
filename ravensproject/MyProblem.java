package ravensproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by My on 9/13/2016.
 */
public class MyProblem {
   String name;
   String problemType; //2x2 or 3x3
   boolean hasVisual; //true if visual representations are included
   boolean hasVerbal; //true if verbal representations are included
   // Map<String, MyFigure> figures;
   // List<MyFigure> designs;
   // List<MyFigure> choices;
   MyFigures designs;
   MyFigures choices;

   public MyProblem(RavensProblem problem) {
      name = problem.getName();
      problemType = problem.getProblemType();
      hasVisual = problem.hasVisual();
      hasVerbal = problem.hasVerbal();
      /*
      figures = new HashMap<>();
      for (Map.Entry<String, RavensFigure> entry : problem.getFigures().entrySet()) {
         MyFigure figure = new MyFigure(entry.getValue());
         figures.put(entry.getKey(), figure);
      }
      */
      designs = new MyFigures(problem.getFigures(), true, problemType);
      choices = new MyFigures(problem.getFigures(), false, problemType);
   }

   // extract the designs: [A, B, C] for 2x2 or [A, B, C, D, E, F, G, H] for 3x3
   List<MyFigure> getDesigns(Map<String, RavensFigure> map) {
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
   List<MyFigure> getChoices(Map<String, RavensFigure> map) {
      List<MyFigure> choices = new ArrayList<>();
      choices.add(new MyFigure(map.get("1")));
      choices.add(new MyFigure(map.get("2")));
      choices.add(new MyFigure(map.get("3")));
      choices.add(new MyFigure(map.get("4")));
      choices.add(new MyFigure(map.get("5")));
      choices.add(new MyFigure(map.get("6")));
      if (map.get("3x3") != null) {
         choices.add(new MyFigure(map.get("7")));
         choices.add(new MyFigure(map.get("8")));
      }
      return choices;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("PROBLEM: " + name + ", type: " + problemType + ", visual: " + hasVisual + ", verbal: " + hasVerbal);
      return builder.toString();
   }
}
