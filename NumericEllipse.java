

package graphingprogram;

/**
 * This class contains the information to create an ellipse as an array of points
 * It is very similar to the circle class, but with a different equation for calculate Y value
 * and starting and ending points
 * @author Simon Langowski slangows@purdue.edu
 */

import java.awt.Color;
import java.util.ArrayList;
import java.lang.Math;
 
public class NumericEllipse extends NumericShape {
    
    private Point center;
    private double xAxis; //corresponds to semimajor or semiminor axis in horizontal direction
    private double yAxis; //corresponds to semimajor or semiminor axis in vertical directon
    private Point[] points;
    private int numPoints;
    private double resolution;
    private String name;
    private Color color;

    
    public NumericEllipse(Point center, double xAxis, double yAxis, double resolution){
        this.center = center;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.resolution = resolution;
        this.color = Color.black;
        redrawShape(resolution);
    }
        
    public NumericEllipse(double x, double y, double xAxis, double yAxis, double resolution){
        this.center = new Point(x, y);
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.resolution = resolution;
        this.color = Color.black;
        redrawShape(resolution);
    }    
        
    //draws the shape equally spaced along the x axis    
    public void redrawShape(double resolution){
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        double xValue = -xAxis;
        double endXValue = xAxis;
        while (xValue <= endXValue){
            //top half of ellipse
            double yValue = calculateYValue(xValue);
            tempPoints.add(new Point(xValue + center.getX(), yValue + center.getY()));
            //bottom half of ellipse
            tempPoints.add(new Point(xValue + center.getX(), -yValue + center.getY()));
            xValue = xValue + resolution;
        }
        //convert to array
        numPoints = tempPoints.size();
        points = new Point[numPoints];
        for (int i = 0; i < numPoints; i++){
            points[i] = tempPoints.get(i);
        }
    }
    
    //calls the recursive method fillInWrapper from NumericShape to fill in the shape
    public void fillInDrawShape(double resolution){
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        double xValue = -xAxis;
        double endXValue = xAxis;
        Point begPoint = new Point(xValue, calculateYValue(xValue));
        Point endPoint = new Point(endXValue, calculateYValue(endXValue));
        tempPoints = fillInWrapper(begPoint, endPoint, resolution);
        
        int initialSize = tempPoints.size();
        numPoints = initialSize * 2;
        points = new Point[numPoints];
        for (int i = 0; i < numPoints; i = i + 2){
            double xVal = tempPoints.get(i / 2).getX() + center.getX();
            double yVal = tempPoints.get(i / 2).getY();
            points[i] = new Point(xVal, center.getY() + yVal);
            points[i + 1] = new Point(xVal, center.getY() - yVal);
        }   
    }
    
    public double calculateYValue(double xValue){
        return Math.sqrt(Math.pow(yAxis, 2) - Math.pow(xValue * yAxis / xAxis, 2));
    }
    
    
    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getxAxis() {
        return xAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public double getyAxis() {
        return yAxis;
    }

    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points){
        this.points = points;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
}
