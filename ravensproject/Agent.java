package ravensproject;

import java.util.List;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return an int representing its
     * answer to the question: 1, 2, 3, 4, 5, or 6. Strings of these ints 
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName(). Return a negative number to skip a problem.
     * 
     * Make sure to return your answer *as an integer* at the end of Solve().
     * Returning your answer as a string may cause your program to crash.
     * @param probleme the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem probleme) {
        MyProblem problem = new MyProblem(probleme);
        System.out.println(problem);

        // if Figure A to Figure B is UNCHANGED
        List<MyFigure> patterns = problem.patterns.list;
        MyFigure figureA = patterns.get(0);
        MyFigure figureB = patterns.get(1);
        MyFigure figureC = patterns.get(2);
        System.out.println(figureA);
        System.out.println(figureB);
        System.out.println(figureC);

        MyFigure horizontalDiff = figureB.subtract(figureA);
        System.out.println("A -> B:" + horizontalDiff);
        MyFigure horizontalTarget = figureC.add(horizontalDiff);
        System.out.println("C -> TARGET:" + horizontalTarget);
        int horizontalChoice = problem.choices.find(horizontalTarget);

        MyFigure verticalDiff = figureC.subtract(figureA);
        System.out.println("A -> C:" + verticalDiff);
        MyFigure verticalTarget = figureB.add(verticalDiff);
        System.out.println("B -> TARGET:" + verticalTarget);
        int verticalChoice = problem.choices.find(verticalTarget);
        if (horizontalChoice == verticalChoice) {
            System.out.println("Answer: " + (horizontalChoice+1) + ": " + verticalTarget);
            return horizontalChoice;
        } else {
            return -1;
        }
    }
}
