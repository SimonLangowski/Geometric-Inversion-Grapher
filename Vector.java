

package graphingprogram;

/**
 * This method makes a vector to be used in circle inversion.  The vector is stored
 * as a deltaX and a deltaY rather than  magnitude and direction for simplicity
 * @author Simon Langowski slangows@purdue.edu
 */


import java.lang.Math;

public class Vector {
    private double deltaX;
    private double deltaY;
    private double magnitude;
        
        public Vector(double deltaX, double deltaY){
            this.deltaX = deltaX;
            this.deltaY = deltaY;
            this.magnitude = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        }
    
        public Vector(Point in, Point end){
            this.magnitude = in.calculateDistance(end);
            this.deltaX = end.getX() - in.getX();
            this.deltaY = end.getY() - in.getY();
        }
        
        public void scaleMagnitude(double length){
            this.deltaX = this.deltaX * length / magnitude;
            this.deltaY = this.deltaY * length / magnitude;
        }
        
        public void add(Vector v){
            this.deltaX = this.deltaX + v.getDeltaX();
            this.deltaY = this.deltaY + v.getDeltaY();
            this.magnitude = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        }

    public double getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public double getMagnitude() {
        return magnitude;
    }
    
    public void setMagnitude(double magnitude){
        scaleMagnitude(magnitude);
    }
                           
        
}
