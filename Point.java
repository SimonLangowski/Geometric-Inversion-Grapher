
package graphingprogram;

/**
 * This is the classic point class
 * It contains an x and a y value, as well as methods to add points and find distances between
 * @author Slang
 */

import java.lang.Math;

public class Point {
    private double x;
    private double y;
    
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double calculateDistance(Point p){
        return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
    }
    
    public Point add(Point p){
        this.x = this.x + p.getX();
        this.y = this.y + p.getY();
        return new Point(this.x + p.getX(), this.y + p.getY());
    }
    
    public void add(Vector v){
        this.x = this.x + v.getDeltaX();
        this.y = this.y + v.getDeltaY();
    }
    
    public Point flipPoint(){
        return new Point(x, -y);
    }
     
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    public void setPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public String toString(){
        String str = String.format("(%.4f, %.4f)", this.x, this.y);
        return str;
        //return ("(" + this.x + ", " + this.y + ")");
    }
    
    
}
