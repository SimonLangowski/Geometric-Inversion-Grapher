

package graphingprogram;

/**
 *
 * @author Simon Langowski slangows@purdue.edu
 */

import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;

public class NumericParameter extends NumericShape{
 
    private String equation;
    private String parameter;
    private String name;
    private double lowerBound;
    private double upperBound;
    private double resolution;
    private Point[] points;
    private int numPoints;
    private Color color;
    private String errorMessage;
    
    public NumericParameter(String equation, String parameter, double lowerBound,
            double upperBound, double resolution){
        this.equation = equation;
        this.parameter = parameter;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.numPoints = (int)((upperBound - lowerBound) / resolution);
        this.color = Color.black;
        this.errorMessage = "";
        redrawShape(resolution);
    }
    
    
    //equation should be formatted with spaces between operators
    // ex y=: 2 * x + 5
    // ex y=: 2 ^ (x + 5)
    
    //ensures the equation is formatted correctly and splits it into an array
    //Also substitutes xValue wherever the parameter is found
    public double calculateEquationValueWrapper(double xValue){
        //validate initialization
        if (equation == null || parameter == null){
            errorMessage = "Empty equation";
            System.out.println("initialize numeric parameter");
            return 0;
        }
        equation = equation + " ";
        //break string into numbers and operations
        ArrayList<String> eqList = new ArrayList<String>();
        String past = "";
        for (int i = 0; i < equation.length(); i++){
            String current = equation.substring(i, i + 1);
            /*debugging
            System.out.println("Current: " + current + "\nPast: " + past);
            */
            if (current.equals(" ") && !(past.trim().equals(""))){
                eqList.add(past);
                past = "";
                continue;
            } else if (current.equals(" ")){
                continue;
            } else if (current.equals("(")){
                if (past.equals("cos")){
                    eqList.add("cos");
                    eqList.add("(");
                    past = "";
                } else if (past.equals("sin")){
                    eqList.add("sin");
                    eqList.add("(");
                    past = "";   
                } else {
                    eqList.add("(");
                    past = "";
                }
            } else if (current.equals(")")) {
                if (!(past.trim().equals("")))
                    eqList.add(past);
                eqList.add(")");
                past = "";
            } else {
                past = past + current;
            }
        }
        String[] eq2 = new String[eqList.size()];
        for (int i = 0; i < eqList.size(); i++)
            eq2[i] = eqList.get(i);
        
        //substitute parameter values
        int numTerms = eq2.length;
        for (int i = 0; i < numTerms; i++){
            //System.out.print(eq2[i] + ", ");
            if (eq2[i].equalsIgnoreCase(parameter)){
                eq2[i] = Double.toString(xValue);
            }
        }
        
        //check matching parenthesis
        int numLeftParenthesis = 0;
        int numRightParenthesis = 0;
        for (int i = 0; i < numTerms; i++){
            if (eq2[i].contains("("))
                numLeftParenthesis++;
            else if (eq2[i].contains(")"))
                numRightParenthesis++;
        }
        if (numLeftParenthesis != numRightParenthesis){
            errorMessage = "Mismatched parenthesis";
            System.out.println("Mistmatched parenthesis");
            return 0;
        }
        
        //validate strings
        for (int i = 0; i < numTerms; i++){
            boolean valid = (eq2[i].equals("+") || eq2[i].equals("-") ||
                    eq2[i].equals("*") || eq2[i].equals("/") || eq2[i].equals("cos") ||
                    eq2[i].equals("sin") || eq2[i].equals("^") || eq2[i].equals("(") ||
                    eq2[i].equals(")"));
            if (valid == false){
                try {
                    Double.parseDouble(eq2[i]);
                    valid = true;
                } catch (NumberFormatException e) {
                    //valid = false;
                }
            }
            if (!(valid)){
                errorMessage = "Invalid syntax: " + eq2[i];
                System.out.println("Invalid syntax: " + eq2[i]);
                return 0;
            }
        }
        
        return calculateEquationValue(eq2);    
    }
    
    
    //Recursive method calculates the value
    public double calculateEquationValue(String[] eq){
        /*debugging code
        System.out.println("length" + eq.length);
        for (int i = 0; i < eq.length; i++)
            System.out.print(eq[i] + "|");
        System.out.println("<-end");
        System.out.println("Numeric?" + checkNumeric(eq[0]));
        */
        if (eq.length == 1){
            return Double.parseDouble(eq[0].trim());
        } if (eq[0] == null || eq[0].equals("")){
            String[] remainingEquation = createNew(eq, 1, eq.length);
            return calculateEquationValue(remainingEquation);
        } else if (checkNumeric(eq[0])){
            if (eq[1].equals("+")){
                String[] remainingEquation = createNew(eq, 2, eq.length);
                return Double.parseDouble(eq[0]) + calculateEquationValue(remainingEquation);
            } else if (eq[1].equals("-")){
                String[] remainingEquation = createNew(eq, 2, eq.length);
                return Double.parseDouble(eq[0]) - calculateEquationValue(remainingEquation);
            } else if (eq[1].equals("*")){
                String[] remainingEquation = createNew(eq, 2, eq.length);
                return Double.parseDouble(eq[0]) * calculateEquationValue(remainingEquation);
            } else if (eq[1].equals("/")){
                String[] remainingEquation = createNew(eq, 2, eq.length);
                return Double.parseDouble(eq[0]) / calculateEquationValue(remainingEquation);
            } else if (eq[1].equals("^")){
                String[] remainingEquation = createNew(eq, 2, eq.length);
                return Math.pow(Double.parseDouble(eq[0]), calculateEquationValue(remainingEquation));
            } else {
                errorMessage = "Programming error: please report";
                System.out.println("Programming error");
                return 0;
            }
        } else if (eq[0].equals("cos")){
            String[] remainingEquation = createNew(eq, 1, eq.length);
            return Math.cos(calculateEquationValue(remainingEquation));
        } else if (eq[0].equals("sin")){
            String[] remainingEquation = createNew(eq, 1, eq.length);
            return Math.sin(calculateEquationValue(remainingEquation));
        } else if (eq[0].equals("(")) {
            int rightIndex = findRightParenthesis(eq);
            if(rightIndex == -1){
                errorMessage = ("Pight parenthesis incorrectly ordered");
                System.out.println("Right parenthesis method finding error");
                return 0;
            }
            String[] innerEquation = createNew(eq, 1, rightIndex);
            String[] remainingEquation = new String[eq.length - rightIndex];
            remainingEquation[0] = Double.toString(calculateEquationValue(innerEquation));
            for (int i = 1; i < remainingEquation.length; i++){
                remainingEquation[i] = eq[rightIndex + i];
            }
            return calculateEquationValue(remainingEquation);
        } else {
            errorMessage = "Programming error: please report";
            System.out.println("Programming error");
            return 0;    
        }   
    }
    
    public boolean checkNumeric(String str){
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public int findRightParenthesis(String[] strs){
        //to find corresponding parenthesis, work in reverse
        int numParenthesis = 0;
        for (int i = 0; i < strs.length; i++){
            if (strs[i].equals("(")){
                numParenthesis++;
            } else if ((strs[i].equals(")")) && (numParenthesis == 1)) {
                return i;
            } else if ((strs[i].equals(")"))){
                numParenthesis--;
            }
        }
        return -1;
    }
    
    public String[] createNew(String[] oldEquation, int begIndex, int endIndex){
        String[] newEquation = new String[endIndex - begIndex];
        int index = 0;
        for (int i = begIndex; i < endIndex; i++){
            newEquation[index] = oldEquation[i];
            index++;
        }
        return newEquation;
    }
    
     @Override
    public double calculateYValue(double xValue) {
        return calculateEquationValueWrapper(xValue);
    }

    //draws the shape equally spaced along the x axis 
    @Override
    public void redrawShape(double resolution) {
        this.resolution = (int)((upperBound - lowerBound) / resolution);
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        double xValue = lowerBound;
        double endXValue = upperBound;
        while (xValue <= endXValue){
            if (!(errorMessage.equals("")))
                break;
            double yValue = calculateYValue(xValue);
            tempPoints.add(new Point(xValue, yValue));
            xValue = xValue + resolution;
        }
        //convert to array
        convertToArrayAndSet(tempPoints);
    }

    //calls the recursive method fillInWrapper from NumericShape to fill in the shape
    @Override
    public void fillInDrawShape(double resolution) {
        this.resolution = (int)((upperBound - lowerBound) / resolution);
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        for (int i = 0; i < points.length - 1; i++){
            ArrayList<Point> veryTempPoints = new ArrayList<Point>();
            veryTempPoints = fillInWrapper(points[i], points[i + 1], resolution);
            for (int j = 0; j < veryTempPoints.size(); j++){
                tempPoints.add(veryTempPoints.get(j));
            }
        }
        convertToArrayAndSet(tempPoints);
       
    }
         
    // when find '(' -> look for index of next ')', take substring
    // and use it to make a new object and recursively solve

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
   

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
    
    
    
}
