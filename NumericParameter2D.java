

/**
 *
 * This class will store a numeric shape object constructed with parameters
 * in both the x and y directions
 *
 * @author Simon Langowski slangows@purdue.edu
 *
 * @version 11/02/2017
 */

package graphingprogram;

import java.awt.Color;

public class NumericParameter2D extends NumericShape{
    private NumericParameter x;
    private NumericParameter y;
    private String xEquation;
    private String yEquation;
    private String parameter;
    private double lowerBound;
    private double upperBound;
    private Point[] points;
    private int numPoints;
    private double resolution;
    private Color color;
    private String name;
    private String errorMessage;
    
    public NumericParameter2D(String xEquation, String yEquation, String parameter, double lowerBound, double upperBound, double resolution){
        this.xEquation = xEquation;
        this.yEquation = yEquation;
        this.resolution = resolution;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.parameter = parameter;
        this.color = Color.black;
        this.errorMessage = "";
        redrawShape(resolution);
    }
    
    @Override
    void redrawShape(double resolution) {
        x = new NumericParameter(xEquation, parameter, lowerBound, upperBound, resolution);
        y = new NumericParameter(yEquation, parameter, lowerBound, upperBound, resolution);
        if (!(x.getErrorMessage().equals("")) || !(y.getErrorMessage().equals(""))){
            errorMessage = x.getErrorMessage() + ", " + y.getErrorMessage();
        }
        if (x.getPoints().length != y.getPoints().length){
            System.out.println("Programming error");
            return;
        }
        numPoints = x.getPoints().length;
        points = new Point[numPoints];
        for (int i = 0; i < numPoints; i++){
            Point p = new Point(x.getPoints()[i].getY(), y.getPoints()[i].getY());
            points[i] = p;
        }
    }
    

    @Override
    void fillInDrawShape(double resolution) {
    }
    
    @Override
    double calculateYValue(double xValue) {
        //search for corresponding t value?  Might be more than one
        return 0;
    }
    
    public NumericParameter getX() {
        return x;
    }

    public void setX(NumericParameter x) {
        this.x = x;
    }

    public NumericParameter getY() {
        return y;
    }

    public void setY(NumericParameter y) {
        this.y = y;
    }

    public String getxEquation() {
        return xEquation;
    }

    public void setxEquation(String xEquation) {
        this.xEquation = xEquation;
    }

    public String getyEquation() {
        return yEquation;
    }

    public void setyEquation(String yEquation) {
        this.yEquation = yEquation;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
    
}
