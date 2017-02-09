

package graphingprogram;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

/**
 *  This class should accept a series of point arrays representing shapes
 *  It should then plot these on a graph and output the points
 *  It should also provide methods to scale and translate the graph
 *  and a grid background.  It will provide the image for the JFrame.
 * @author Simon Langowski slangows@purdue.edu
 */


public class Grapher extends JPanel{
   
    private int scale;
    private int xTranslation;
    private int yTranslation;
    private ArrayList<NumericShape> shapes;
    private final int POINTSIZE = 1;
    private ArrayList<Line2D> grid;
    private ArrayList<String> numbers;
    private ArrayList<Point> numberLocations;
    private ArrayList<Color> colors;
    private int xMove;
    private int yMove;
    
    public Grapher(){
        super();
        this.setBackground(Color.white);
        shapes = new ArrayList<NumericShape>();
        grid = new ArrayList<Line2D>();
        numbers = new ArrayList<String>();
        numberLocations = new ArrayList<Point>();
        colors = new ArrayList<Color>();
        scale = 100;
        xMove = 0;
        yMove = 0;
    }
    
    //Gets arrays of points and draws them as Ellipse2D at the calculated location
    //locations are changed to make (0,0) the center of the screen with xTranslation and yTranslation.
    //The values xMove and yMove are for user translations of the screen,
    // and the value scale is used to space points out
    // It also draws the grid using line2D and the corresponding array of lines and numbers
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        for (int i = 0; i < numbers.size(); i++){
            g.drawString(numbers.get(i), (int)(numberLocations.get(i).getX() + xTranslation), (int)(numberLocations.get(i).getY() + yTranslation));
        }
        
        Graphics2D g2d = (Graphics2D) g;
        for (Line2D line : grid){
            g2d.draw(line);
        }
        
        for (NumericShape n : shapes){
            Point[] shape = n.getPoints();
            if (n.getColor() != null)
                g2d.setColor(n.getColor());
            else
                g2d.setColor(Color.black);
            for (Point p: shape){
                if (p != null){
                    double xCoordinate = ((p.getX() - xMove) * scale) - POINTSIZE + xTranslation;
                    double yCoordinate = ((p.getY() - yMove) * -scale) - POINTSIZE + yTranslation;
                    //check bounds and avoid drawing offscreen
                    if (((xCoordinate >= 0) && (xCoordinate <= xTranslation * 2)) && ((yCoordinate >= 0) && (yCoordinate <= yTranslation * 2)))
                        g2d.draw(new Ellipse2D.Double(xCoordinate, yCoordinate, POINTSIZE * 2, POINTSIZE * 2));
                }
            }
        }
        
             
    }
    
    public void construct(){
        calculateGrid();
    }
    
    public void setScale(int scale){
        this.scale = scale;
        calculateGrid();
    }
    
    public int getScale(){
        return scale;
    }
    
    //this method calculates the locations of the lines and numbers for the grid
    public void calculateGrid(){
        xTranslation = this.getWidth()/2;
        yTranslation = this.getHeight()/2;
        grid.clear();
        numbers.clear();
        numberLocations.clear();
        
        //horizontal lines
        int numHozLines = (yTranslation / scale) + 1;
        grid.add(new Line2D.Double(0,yTranslation, xTranslation * 2,yTranslation));
        for (int i = 0; i < numHozLines; i++){
            grid.add(new Line2D.Double(0,scale * i + yTranslation, xTranslation * 2,scale * i + yTranslation));
            grid.add(new Line2D.Double(0,scale * -i + yTranslation, xTranslation * 2,scale * -i + yTranslation));
            
            numbers.add(Integer.toString(i + xMove));
            numberLocations.add(new Point(0, -scale * i));
            numbers.add(Integer.toString(-i + xMove));
            numberLocations.add(new Point(0, -scale * -i));
        }
        
        
        //vertical lines
        int numVerLines = (xTranslation / scale) + 1;
        grid.add(new Line2D.Double(xTranslation,0,xTranslation,yTranslation * 2));
        for (int i = 0; i < numVerLines; i++){
            grid.add(new Line2D.Double(scale * i + xTranslation,0,scale * i + xTranslation,yTranslation * 2));
            grid.add(new Line2D.Double(scale * -i + xTranslation,0,scale * -i + xTranslation,yTranslation * 2));
        
            numbers.add(Integer.toString(i + yMove));
            numberLocations.add(new Point(scale * i, 0));
            numbers.add(Integer.toString(-i + yMove));
            numberLocations.add(new Point(-scale * i, 0));
        
        }
        repaint();
        
    }
    
    
    
    public void addShape(NumericShape n){
        shapes.add(n);
        repaint();
    }
    
    public void deleteShape(NumericShape n){
        shapes.remove(n);
        repaint();
    }
    
    public void deleteShape(int index){
        shapes.remove(shapes.get(index));
        repaint();
    }
    
    public void replaceShape(NumericShape n1, NumericShape n2){
        int index = shapes.indexOf(n1);
        if (index != -1)
            shapes.set(index, n2);
        repaint();
    }
    
    public NumericShape getShape(int index){
        return shapes.get(index);
    }
    
    public int getLength(){
        return shapes.size();
    }
    
    public String toString(){
        if (shapes.size() <= 0)
            return "None";
        else {
            String manifest = "";
            for (int i = 1; i <= shapes.size(); i++){
                manifest = manifest + shapes.get(i).getName() + "\n";
            }
            return manifest;
        }       
    }
    
    public String[] getManifest(){
        String[] manifest = new String[shapes.size()];
        for (int i = 0; i < shapes.size(); i++){
            manifest[i] = shapes.get(i).getName();
        }
        return manifest;
    }
    
    public int getxTranslation() {
        return xTranslation;
    }

    public void setxTranslation(int xTranslation) {
        this.xTranslation = xTranslation;
    }

    public int getyTranslation() {
        return yTranslation;
    }

    public void setyTranslation(int yTranslation) {
        this.yTranslation = yTranslation;
    }

    public ArrayList<NumericShape> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<NumericShape> shapes) {
        this.shapes = shapes;
    }

    public ArrayList<Line2D> getGrid() {
        return grid;
    }

    public void setGrid(ArrayList<Line2D> grid) {
        this.grid = grid;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public ArrayList<Point> getNumberLocations() {
        return numberLocations;
    }

    public void setNumberLocations(ArrayList<Point> numberLocations) {
        this.numberLocations = numberLocations;
    }

    public int getxMove() {
        return xMove;
    }

    public void setxMove(int xMove) {
        this.xMove = xMove;
    }

    public int getyMove() {
        return yMove;
    }

    public void setyMove(int yMove) {
        this.yMove = yMove;
    }
    
    
}
