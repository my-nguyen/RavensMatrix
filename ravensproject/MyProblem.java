package ravensproject;

/**
 * Created by My on 9/13/2016.
 */
public class MyProblem {
   String name;
   String problemType; //2x2 or 3x3
   boolean hasVisual; //true if visual representations are included
   boolean hasVerbal; //true if verbal representations are included
   Patterns patterns;
   Choices choices;

   public MyProblem(RavensProblem problem) {
      name = problem.getName();
      problemType = problem.getProblemType();
      hasVisual = problem.hasVisual();
      hasVerbal = problem.hasVerbal();
      patterns = new Patterns(problem.getFigures(), problemType);
      choices = new Choices(problem.getFigures(), problemType);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("PROBLEM: " + name + ", type: " + problemType + ", visual: " + hasVisual + ", verbal: " + hasVerbal);
      return builder.toString();
   }
}
