package ravensproject;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

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
    Map<String, Character> map = new HashMap<>();
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        map.put("000000", 'b');
        map.put("ffffff", 'w');
        map.put("b7b7b7", 'g');
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
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {
        /*
        if (problem.getName().equals("Basic Problem B-01")) {
            System.out.println(problem.getName() + ", type: " + problem.getProblemType());
            for(String figureName : problem.getFigures().keySet()) {
                RavensFigure figure = problem.getFigures().get(figureName);
                try { // Required by Java for ImageIO.read
                    BufferedImage figureImage = ImageIO.read(new File(figure.getVisual()));
                    System.out.println(figureName + ", width: " + figureImage.getWidth() + ", height: " + figureImage.getHeight());
                    if (figureName.equals("1")) {
                        for (int i = 0; i < figureImage.getWidth(); i++) {
                            for (int j = 0; j < figureImage.getHeight(); j++) {
                                int thisPixel = figureImage.getRGB(i, j);
                                String hex = Integer.toHexString(thisPixel).substring(2);
                                char color;
                                if (map.containsKey(hex)) {
                                    color = map.get(hex);
                                } else {
                                    System.out.println("(" + i + "," + j + ") pixel: " + thisPixel + ", hex: " + hex);
                                    color = 'g';
                                }
                                // System.out.print(color);
                                if (i == 28 && j == 75) {
                                    read = true;
                                }
                            }
                            System.out.println();
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("EXCEPTION " + ex);
                }
            }
        */

        System.out.println(toString(problem));
        // extract the problems: [A, B, C] for 2x2 or [A, B, C, D, E, F, G, H] for 3x3
        List<RavensFigure> problems = getProblems(problem.getFigures());
        // extract the answer choices: [1, 2, 3,..., 6] for 2x2 or [1, 2, 3,..., 8] for 3x3
        List<RavensFigure> choices = getChoices(problem.getFigures());
        // if Figure A to Figure B is UNCHANGED
        if (figureUnchanged(problems.get(0), problems.get(1))) {
            System.out.println(toString(problems.get(2)));
            // look in answer choices for a Figure that matches Figure C
            int index = findFigure(choices, problems.get(2));
            if (index != -1) {
                // if Figure A to Figure C is UNCHANGED
                if (figureUnchanged(problems.get(0), problems.get(2))) {
                    // make sure Figure B to answer choice is also UNCHANGED
                    if (figureUnchanged(problems.get(1), choices.get(index))) {
                        System.out.println("answer " + (index + 1));
                        return index;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            System.out.println("no identical figures");
            return -1;
        }
    }

    boolean containsObject(Collection<RavensObject> collection, RavensObject object) {
        for (RavensObject iterator : collection) {
            if (objectUnchanged(object, iterator)) {
                return true;
            }
        }
        return false;
    }

    boolean objectUnchanged(RavensObject leftObject, RavensObject rightObject) {
        Map<String, String> leftAttributes = leftObject.getAttributes();
        Map<String, String> rightAttributes = rightObject.getAttributes();

        // make sure the two maps of attributes are of the same size
        if (leftAttributes.size() != rightAttributes.size()) {
            return false;
        }
        // for each key in map 1, make sure the value for that key in map 1 matches the value
        // for the same key in the map 2
        for (String key : leftAttributes.keySet()) {
            if (!leftAttributes.get(key).equals(rightAttributes.get(key))) {
                return false;
            }
        }
        // System.out.println(toString(leftObject));
        // System.out.println(toString(rightObject));
        System.out.println("Object " + leftObject.getName() + " to Object " + rightObject.getName() + " is UNCHANGED.");
        return true;
    }

    boolean figureUnchanged(RavensFigure leftFigure, RavensFigure rightFigure) {
        Map<String, RavensObject> leftObjects = leftFigure.getObjects();
        Map<String, RavensObject> rightObjects = rightFigure.getObjects();

        if (leftObjects.size() != rightObjects.size()) {
            return false;
        }
        for (RavensObject object : leftObjects.values()) {
            if (!containsObject(rightObjects.values(), object)) {
                return false;
            }
        }
        // System.out.println(toString(leftFigure));
        // System.out.println(toString(rightFigure));
        System.out.println("Figure " + leftFigure.getName() + " to Figure " + rightFigure.getName() + " is UNCHANGED.");
        return true;
    }

    List<RavensFigure> getProblems(Map<String, RavensFigure> map) {
        List<RavensFigure> problems = new ArrayList<>();
        problems.add(map.get("A"));
        problems.add(map.get("B"));
        problems.add(map.get("C"));
        if (map.get("D") != null) {
            problems.add(map.get("D"));
            problems.add(map.get("E"));
            problems.add(map.get("F"));
            problems.add(map.get("G"));
            problems.add(map.get("H"));
        }
        return problems;
    }

    List<RavensFigure> getChoices(Map<String, RavensFigure> map) {
        List<RavensFigure> choices = new ArrayList<>();
        choices.add(map.get("1"));
        choices.add(map.get("2"));
        choices.add(map.get("3"));
        choices.add(map.get("4"));
        choices.add(map.get("5"));
        choices.add(map.get("6"));
        if (map.get("7") != null) {
            choices.add(map.get("7"));
            choices.add(map.get("8"));
        }
        return choices;
    }

    int findFigure(List<RavensFigure> choices, RavensFigure figure) {
        for (int i = 0; i < choices.size(); i++) {
            if (figureUnchanged(figure, choices.get(i))) {
                return i;
            }
        }
        return -1;
    }

    String toString(RavensProblem problem) {
        StringBuilder builder = new StringBuilder();
        builder.append("PROBLEM: " + problem.getName() + ", type: " + problem.getProblemType() + ", visual: " + problem.hasVisual() + ", verbal: " + problem.hasVerbal());
        return builder.toString();
    }

    String toString(RavensFigure figure) {
        StringBuilder builder = new StringBuilder();
        for (String objectName : figure.getObjects().keySet()) {
            RavensObject object = figure.getObjects().get(objectName);
            builder.append("Figure: " + figure.getName() + ", " + toString(object));
        }
        return builder.toString();
    }

    String toString(RavensObject object) {
        StringBuilder builder = new StringBuilder();
        builder.append("Object: " + object.getName());
        for (String attributeName : object.getAttributes().keySet()) {
            String attributeValue = object.getAttributes().get(attributeName);
            builder.append(", " + attributeName + ": " + attributeValue);
        }
        return builder.toString();
    }
}
