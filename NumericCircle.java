

package graphingprogram;

/**
 * This class represents a circle as an array of points.  It contains methods
 * to draw the circle, fill in the circle, and invert other point arrays across it
 * @author Simon Langowski slangows@purdue.edu
 */

import java.awt.Color;
import java.util.ArrayList;
import java.lang.Math;
 
public class NumericCircle extends NumericShape {
    
    private Point center;
    private double radius;
    private Point[] points;
    private int numPoints;
    private double resolution;
    private String name;
    private final double EPSILON2 = 0.00000001; //this constant prevents sending points to infinity during inversion
    private Color color;
    
    public NumericCircle(Point center, double radius, double resolution){
        this.center = center;
        this.radius = radius;
        this.resolution = resolution;
        this.color = Color.black;
        redrawShape(resolution);    
    }
    
    public NumericCircle(double x, double y, double radius, double resolution){
        this.center = new Point(x, y);
        this.radius = radius;
        this.resolution = resolution;
        this.color = Color.black;
        redrawShape(resolution);
    }
    
    public NumericCircle(double x, double y, double radius){
        this.center = new Point(x, y);
        this.radius = radius;
        this.resolution = 0.1;
        this.color = Color.black;
        redrawShape(0.01);
    }

    public NumericCircle(){
        this.center = new Point(0,0);
        this.radius = 1;
        this.resolution = 0.01;
        this.color = Color.black;
        redrawShape(resolution);
    }
    
    //draws the shape equally shaped along the x axis 
    public void redrawShape(double resolution){
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        double xValue = -radius;
        double endXValue = radius;
        while (xValue <= endXValue){
            //top half of circle
            double yValue = calculateYValue(xValue);
            tempPoints.add(new Point(xValue + center.getX(), yValue + center.getY()));
            //bottom half of circle
            tempPoints.add(new Point(xValue + center.getX(), -yValue + center.getY()));
            xValue = xValue + resolution;
        }
        //convert to array
        convertToArrayAndSet(tempPoints);
        
    }
    
    //calls the recursive method fillInWrapper from NumericShape to fill in the shape
    public void fillInDrawShape(double resolution){
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        double xValue = -radius;
        double endXValue = radius;
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
        return Math.sqrt(Math.pow(radius, 2) - Math.pow(xValue, 2));
    }
    
    //Geometrically inverts a set of points across this circle object and returns the inverted points
    public NumericPoints geoInvert(Point[] shape){
        ArrayList<Point> tempPoints = new ArrayList<Point>();
        double rSquared = Math.pow(radius, 2);
        for (int i = 0; i < shape.length; i++){
            if (shape[i] != null){
                Vector v = new Vector(center, shape[i]);
                if (!(v.getMagnitude() < EPSILON2)){
                    v.scaleMagnitude(rSquared/v.getMagnitude());
                    tempPoints.add(new Point(center.getX() + v.getDeltaX(), center.getY() + v.getDeltaY()));
                }
            }
        }    
        Point[] invertShape = new Point[shape.length];
        for (int i = 0; i < tempPoints.size(); i++)
            invertShape[i] = tempPoints.get(i);
        NumericPoints nums = new NumericPoints(invertShape);
        return nums;
    }


    
    
    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
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

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
}
