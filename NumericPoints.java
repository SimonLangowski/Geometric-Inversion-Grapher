

package graphingprogram;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This class is meant to contain inverted shapes as point arrays, for which equations cannot be calculated.
 * It allows them to be graphed as NumericShapes.
 * @author Simon Langowski slangows@purdue.edu
 */
public class NumericPoints extends NumericShape{

    private Point[] points;
    private int numPoints;
    private String name;
    private Color color;
    
    public NumericPoints(Point[] points){
        this.points = points;
        this.numPoints = points.length;
        this.color = Color.black;
    }
    
    @Override
    public Point[] getPoints() {
        return points;
    }

    @Override
    public int getNumPoints() {
        return numPoints;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    @Override
    public double calculateYValue(double xValue) {
        //possibly implementation
        //get points in sorted order
        //find which points x value lies between
        //give a weighted average of those points y values
        return 0;
    }

    
    @Override
    public void redrawShape(double resolution) {
        return;
    }
    
    //be careful about filling in a million offscreen points, perhaps request screen size from grapher first.
    @Override
    public void fillInDrawShape(double resolution) {
        //essentially linear interpolation
        /*
        double minX = getMinX();
        double maxX = getMaxX();
        
        ArrayList<Point> points2 = new ArrayList<Point>();
        for (double i = minX; i <= maxX; i = i + resolution)
            points2.add(new Point(i, calculateYValue(i)));
        convertToArrayAndSet(points2);
        */
        return;
    }

    public void setPoints(Point[] points) {
        this.points = points;
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
    
    
}


