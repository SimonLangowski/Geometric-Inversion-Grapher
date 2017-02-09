

package graphingprogram;

/**
 * A Numeric shape is an array of points representing a geometric shape,
 * such as a circle, line, etc.  It contains methods and ensures conformity
 * for methods to getPoints() to draw them, color and name conformity, and 
 * a recursive method that uses the shapes calculateYValue() method to generate more points
 * 
 * @author Simon Langowski slangows@purdue.edu
 */

import java.awt.Color;
import java.util.ArrayList;

public abstract class NumericShape {
   
    //tolerance for numeric intersection
    final double EPSILON = 0.0001;
            
    //expect Point[] points to represent shape as a set of Points
    //numPoints indicates number of points and the size of the array
    //expect name to indicate name of object
    abstract Point[] getPoints();
    abstract void setPoints(Point[] points);
    abstract int getNumPoints();
    abstract void setNumPoints(int numPoints);
    abstract String getName();
    abstract double calculateYValue(double xValue);
    abstract void redrawShape(double resolution);
    abstract void fillInDrawShape(double resolution);
    abstract Color getColor();
    abstract void setColor(Color color);
    //intersects will implement intersection between shapes by solving equations
    //numericIntersects will only be called otherwise and brute forces intersections
    
    
    //This method is untested, but should return intersections
    ArrayList<Point> numericIntersects(NumericShape shape){
       ArrayList<Point> intersections = new ArrayList<Point>();
       for (Point myPoint : this.getPoints()){
           for (Point otherPoint : shape.getPoints()){
               if (myPoint.calculateDistance(otherPoint) < EPSILON){
                   intersections.add(myPoint);
               }
           }
       }
       return intersections;
    }
    
    //Wrapper class for the fillIn method
    public ArrayList<Point> fillInWrapper(Point p1, Point p2, double tolerance){
        ArrayList<Point> temp = new ArrayList<Point>();
        temp.add(p1);
        temp.add(p2);
        temp = fillIn(p1, p2, tolerance, temp);
        return temp;
    }
    
    //Recursively fills in the shape.  If two adjacent points in the shape are not the inputted
    //tolerance distance apart, calculates a new point with the xValue halfway in between them
    //Then uses this new point and the two sides until the shape is filled in
    public ArrayList<Point> fillIn (Point p1, Point p2, double tolerance, ArrayList<Point> needed){
        if (p1.calculateDistance(p2) <= tolerance)
            return needed;
        else {
            double xVal = (p1.getX() + p2.getX()) / 2;
            Point calculated = new Point(xVal, calculateYValue(xVal));
            ArrayList<Point> section1 = new ArrayList<Point>();
            ArrayList<Point> section2 = new ArrayList<Point>();
            section1 = fillInWrapper(p1, calculated, tolerance);
            section2 = fillInWrapper(calculated, p2, tolerance);
            for (int i = 0; i < section1.size() - 1; i++)
                needed.add(section1.get(i));
            for (int i = 1; i < section2.size(); i++)
                needed.add(section2.get(i));
            needed.add(calculated);
            return needed;
        }
    }
    
    public double getMinX(){
        Point[] points = getPoints();
        int numPoints = points.length;
        if (numPoints < 1)
            return 0;
        double minX = points[0].getX();
        for (int i = 0; i < numPoints; i++){
            if(points[i].getX() < minX)
                minX = points[i].getX();
        }
        return minX;
    }
    
    public double getMaxX(){
        Point[] points = getPoints();
        int numPoints = points.length;
        if (numPoints < 1)
            return 0;
        double maxX = points[numPoints - 1].getX();
        for (int i = 0; i < numPoints; i++){
            if(points[i].getX() > maxX)
                maxX = points[i].getX();
        }
        return maxX;
    }
    
    //don't use
    public void convertToArrayAndSet(ArrayList<Point> tempPoints){
        int numPoints = tempPoints.size();
        Point [] points = new Point[numPoints];
        for (int i = 0; i < numPoints; i++){
            points[i] = tempPoints.get(i);
        }
        setPoints(points);
        setNumPoints(numPoints);
    }
    
    
    public String toString(){
        Point[] points = this.getPoints();
        String string = "";
        if (getName() != null)
            string = string + getName() + "\n";
        for (Point p: points){
            string = string + p + "\n";
        }
        return string;
    } 
   
}
