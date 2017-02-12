/**
 * Graphing Program - random math idea
 * 
 * This program will graph shapes as a set of points, in order to geometrically
 * invert them.  See "circle inversion" at https://en.wikipedia.org/wiki/Inversive_geometry
 * for the mathematical idea.  Thus, the user must create a circle and another shape,
 * this can be another circle, an ellipse, or a function, with parametrics planned later.
 * Then the user can invert the circle.
 * There are also methods to scale and translate the graph in order to make it visible
 * as well as change the number of points calculated
 * Users enter commands in a jTextField below the graph.
 * 
 * @author Slang
 */


package graphingprogram;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

public class GraphingProgram{
    public static final int SCALE = 4; //THIS IS NOT THE SCALE OF THE SHAPES!!!
    public static final int WIDTH = 320 * SCALE; //Size
    public static final int HEIGHT = 240 * SCALE; //Size
    public int state;
    public int itemNumber;
    private Color[] colors;
    
    //Main method, creates JFrame and JPanels
    public static void main(String[] args) {

        JFrame window = new JFrame(); //create JFrame
        Grapher grapher = new Grapher();
        TextListener t = new TextListener();
        window.setTitle("Graphing program");
        window.setResizable(false); //don't mess me up!
        window.setMinimumSize(new Dimension(WIDTH, HEIGHT)); //overide size to correct
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make closeable
        JPanel co = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 12.0;
        co.add(grapher, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        co.add(t, c);
        co.setBackground(Color.white);

        window.add(co);        
        window.pack(); //makes JFrame fit window
        window.setLocationRelativeTo(null); //center on screen
        window.setVisible(true); //now you can see it

        grapher.construct();

        GraphingProgram prog = new GraphingProgram();
        prog.loop(t, grapher);    
    }
    
    //This loop extends the main method, and makes it less crowded
    //It contains the logic for determining what to do
    public void loop(TextListener t, Grapher g){
        itemNumber = 1;
        initializeColors();
        String instructions1 = "Create new shape: C - Circle. E - Ellipse. F - Function. P - parameter";
        String instructions2 = "Operations: G - Geometric invert. R - Remove Shape. I - Estimate intersections";
        String instructions3 = "View: S - Change scale.  D - Change fill level. T - Translate center.";
        String instructions4 = "L - List shapes.  Q - Quit";
        while(true) {
            sendMessage(t, instructions1);
            sendMessage(t, instructions2);
            sendMessage(t, instructions3);
            sendMessage(t, instructions4);
            String message = getMessage(t).trim();
            if (message.equalsIgnoreCase("c")){
                sendMessage(t, "What is the radius?");
                double radius = getDoubleMessage(t);
                if (radius < 0){
                    sendMessage(t, "Invalid radius, returning to menu");
                    continue;
                }
                sendMessage(t, "What is the x coordinate of the center?");
                double xCoordinate = getDoubleMessage(t);
                sendMessage(t, "What is the y coordinate of the center?");
                double yCoordinate = getDoubleMessage(t);

                NumericCircle circle = new NumericCircle(xCoordinate, yCoordinate, radius, 0.01);
                circle.setName("Circle " + itemNumber);
                circle.setColor(getNextColor(itemNumber));
                circle.fillInDrawShape(0.001);
                g.addShape(circle);
                itemNumber++;
                sendMessage(t, "Circle successfully created");
            } else if (message.equalsIgnoreCase("e")){
                sendMessage(t, "What is the length of the axis in the x direction?");
                double xAxis = getDoubleMessage(t);
                if (xAxis < 0){
                    sendMessage(t, "Invalid number, returning to menu");
                    continue;
                }
                sendMessage(t, "What is the length of the axis in the y direction?");
                double yAxis = getDoubleMessage(t);
                if (yAxis < 0){
                    sendMessage(t, "Invalid number, returning to menu");
                    continue;
                }
                sendMessage(t, "What is the x coordinate of the center?");
                double xCoordinate = getDoubleMessage(t);
                sendMessage(t, "What is the y coordinate of the center?");
                double yCoordinate = getDoubleMessage(t);

                NumericEllipse ellipse = new NumericEllipse(xCoordinate, yCoordinate, xAxis / 2, yAxis / 2, 0.01);
                ellipse.setName("Ellipse " + itemNumber);
                ellipse.setColor(getNextColor(itemNumber));
                ellipse.fillInDrawShape(0.001);
                g.addShape(ellipse);
                itemNumber++;
                sendMessage(t, "Ellipse successfully created");
            } else if (message.equalsIgnoreCase("f")){
                sendMessage(t, "Enter your function in the form: \"3 * (x ^ 2)\" or \"cos(x ^ 3)\" or \"(-0.5 * (x ^ 2)) + 2\"");
                sendMessage(t, "Warning: does not follow order of operations, use parenthesis as necessary");
                String equation;
                while (true){
                    equation = getMessage(t);
                    if (!(equation.trim() == ""))
                        break;
                }
                sendMessage(t, "If you are going to fill your shape, choose small bounds so that y values are within screen");
                sendMessage(t, "Enter the lower bound");
                double lowerBound = getDoubleMessage(t);
                sendMessage(t, "Enter the upper bound");
                double upperBound = getDoubleMessage(t);
                sendMessage(t, "Please wait");
                NumericParameter function = new NumericParameter(equation, "x", lowerBound, upperBound, 0.001);
                function.setColor(getNextColor(itemNumber));
                function.setName("Function " + itemNumber);
                if (function.getErrorMessage().equals("")){
                    g.addShape(function);
                    itemNumber++;
                    sendMessage(t, "Function creation successful");
                    sendMessage(t, "Use F to fill in polynomial.  WARNING, this make take a very long time depending on the size");
                } else {
                    String errorMessage = function.getErrorMessage();
                    sendMessage(t, "Function creation failed: " + errorMessage);
                }
            } else if (message.equalsIgnoreCase("p")){
                sendMessage(t, "Enter your X parameterization in the form: \"3 * (t ^ 2)\" or \"cos(t ^ 3)\" or \"(-0.5 * (t ^ 2)) + 2\"");
                sendMessage(t, "Warning: does not follow order of operations, use parenthesis as necessary");
                String equationX;
                while (true){
                    equationX = getMessage(t);
                    if (!(equationX.trim() == ""))
                        break;
                }
                sendMessage(t, "Enter your Y parameterization in the form: \"3 * (t ^ 2)\" or \"cos(t ^ 3)\" or \"(-0.5 * (t ^ 2)) + 2\"");
                sendMessage(t, "Warning: does not follow order of operations, use parenthesis as necessary");
                String equationY;
                while (true){
                    equationY = getMessage(t);
                    if (!(equationY.trim() == ""))
                        break;
                }
                sendMessage(t, "If you are going to fill your shape, choose small bounds so that values are within screen");
                sendMessage(t, "Enter the lower bound for t");
                double lowerBound = getDoubleMessage(t);
                sendMessage(t, "Enter the upper boundfor t");
                double upperBound = getDoubleMessage(t);
                sendMessage(t, "Please wait");
                NumericParameter2D parameterization = new NumericParameter2D(equationX, equationY, "t", lowerBound, upperBound, 0.001);
                parameterization.setColor(getNextColor(itemNumber));
                parameterization.setName("Function " + itemNumber);
                if (parameterization.getErrorMessage().equals("")){
                    g.addShape(parameterization);
                    itemNumber++;
                    sendMessage(t, "Function creation successful");
                    sendMessage(t, "Use F to fill in polynomial.  WARNING, this make take a very long time depending on the size");
                } else {
                    String errorMessage = parameterization.getErrorMessage();
                    sendMessage(t, "Function creation failed: " + errorMessage);
                }
            } else if (message.equalsIgnoreCase("i")){
                String[] manifest = g.getManifest();
                if (manifest.length < 2)
                    sendMessage(t, "Not enough shapes");
                else {
                    sendMessage(t, "Select two shapes to find intersections for (-1 to quit)");
                    for (int i = 0; i < manifest.length; i++){
                        String name = "" + (i + 1) + ": " + manifest[i];
                        sendMessage(t, name);
                    }
                    while (true){
                        int choice1 = getIntegerMessage(t);
                        if (choice1 <= 0) {
                            break;
                        } else if (choice1 > manifest.length) {
                            sendMessage(t, "Select a valid shape");
                        } else {
                            sendMessage(t, "Select the second shape (-1 to quit)");
                            while (true){
                                int choice2 = getIntegerMessage(t);
                                if (choice2 <= 0) {
                                    break;
                                } else if (choice2 > manifest.length) {
                                    sendMessage(t, "Select a valid shape");
                                } else {
                                    ArrayList<Point> result = new ArrayList<Point>();
                                    result = g.getShape(choice1 - 1).numericIntersects(g.getShape(choice2 - 1));
                                    ArrayList<String> strings = new ArrayList<String>();
                                    for (Point p : result){
                                        boolean alreadyFound = false;
                                        for (String s : strings){
                                            if (p.toString().equals(s))
                                                alreadyFound = true;
                                        }
                                        if (!alreadyFound)
                                            strings.add(p.toString());
                                    }
                                    if (strings.size() > 1000)
                                        sendMessage(t, "Warning more than 1000 intersections");
                                    else if (strings.size() == 0)
                                        sendMessage(t, "No intersections found");
                                    else
                                        for (String s: strings)
                                            sendMessage(t, s);
                                    break;
                                }
                            }
                            break;
                        }         
                    }
                }
            } else if (message.equalsIgnoreCase("g")){
                String[] manifest = g.getManifest();
                if (manifest.length < 2)
                    sendMessage(t, "Not enough shapes");
                else {
                    sendMessage(t, "Select a circle to use for inversion (enter -1 to quit)");
                    for (int i = 0; i < manifest.length; i++){
                        String name = "" + (i + 1) + ": " + manifest[i];
                        sendMessage(t, name);
                    }
                    while (true){
                        int choice = getIntegerMessage(t);
                        if (choice <= 0) {
                            break;
                        } else if (choice > manifest.length) {
                            sendMessage(t, "Select a circle or -1 to quit");
                        } else if (manifest[choice - 1].substring(0,6).equalsIgnoreCase("circle")){
                            sendMessage(t, "Select a shape to invert");
                            for (int i = 0; i < manifest.length; i++){
                                String name = "" + (i + 1) + ": " + manifest[i];
                                sendMessage(t, name);
                            }
                            int choice2 = getIntegerMessage(t);
                            if ((choice2 >= 1) && (choice2 <= manifest.length)){
                                System.out.println("I got here!!");
                                invert(choice - 1, choice2 - 1, g);
                                sendMessage(t, "Inversion successful!");
                                break;
                            } else {
                                sendMessage(t, "Invalid selection, returning to menu");
                                break;
                            }
                        } else {
                            sendMessage(t, "Select a circle or -1 to quit");
                        }         
                    }
                }
            } else if (message.equalsIgnoreCase("r")){    
                String[] manifest = g.getManifest();
                sendMessage(t, "Select a shape to delete (-1 to go back to menu");
                for (int i = 0; i < manifest.length; i++){
                    String name = "" + (i + 1) + ": " + manifest[i];
                    sendMessage(t, name);
                }
                while (true){
                    int choice = getIntegerMessage(t);
                    if (choice <= 0) {
                        break;
                    } else if (choice > manifest.length) {
                        sendMessage(t, "-1 to quit");
                    } else {
                        g.deleteShape(choice - 1);
                        sendMessage(t, "Deletion successful");
                        break;
                    }        
                }
                    
            } else if (message.equalsIgnoreCase("d")){
                sendMessage(t, "Recommended fill level: 0.01 for solid, 0.001 if planning to invert");
                sendMessage(t, "How accurate?");
                double resolution = getDoubleMessage(t);
                sendMessage(t, "Working...");
                for (int i = 0; i < g.getLength(); i++){
                    g.getShape(i).fillInDrawShape(resolution);
                }
                g.repaint();    
            } else if (message.equalsIgnoreCase("s")){
                int currentScale = g.getScale();
                sendMessage(t, "Choose a new scale (-1 to quit)");
                sendMessage(t, "Current scale: " + currentScale);
                int newScale = getIntegerMessage(t);
                if (newScale <= 0){
                    sendMessage(t, "Returning to menu");
                    continue;
                } else {
                    g.setScale(newScale);
                }
            } else if (message.equalsIgnoreCase("t")){
                sendMessage(t, "Choose integer coordinates of new center");
                sendMessage(t, "Enter the X coordinate:");
                int xCoordinate = getIntegerMessage(t); 
                sendMessage(t, "Enter the Y coordinate:");
                int yCoordinate = getIntegerMessage(t);
                g.setxMove(xCoordinate);
                g.setyMove(yCoordinate);
                g.calculateGrid();
                
            } else if (message.equalsIgnoreCase("l")){
                String[] manifest = g.getManifest();
                if (manifest.length < 1){
                    sendMessage(t, "No shapes");
                } else {
                    for (int i = 0; i < manifest.length; i++){
                        String name = "" + (i + 1) + ": " + manifest[i];
                        sendMessage(t, name);
                    }   
                }
            } else if (message.equalsIgnoreCase("ld")){
                String[] manifest = g.getManifest();
                if (manifest.length < 1){
                    sendMessage(t, "No shapes");
                } else {
                    for (int i = 0; i < manifest.length; i++){
                        String name = "" + (i + 1) + ": " + manifest[i] + "  Points: " +  g.getShapes().get(i).getNumPoints();
                        sendMessage(t, name);
                    }   
                }
            } else if (message.equalsIgnoreCase("q")){
                //the text listener also quits the program when q is entered
                sendMessage(t, "Quitting program");
                System.exit(0);
            } else {
                sendMessage(t, "Invalid choice");    
            }   
        } 
    }
    
    //This method gets the next message from the jtextfield
    public String getMessage(TextListener t){
        while(!(t.wasChange())){
            t.refresh();
        }
        return t.getEntry();
    }
    
    //This method ensures the message is a double
    public double getDoubleMessage(TextListener t){
        while(true){
            try{
                return Double.parseDouble(getMessage(t));
            } catch (java.lang.NumberFormatException e) {
                sendMessage(t, "Enter a number");
            }
        }
    }
    
    //This method ensures the message is an int
    public int getIntegerMessage(TextListener t){
        while(true){
            try{
                return Integer.parseInt(getMessage(t));
            } catch (java.lang.NumberFormatException e) {
                sendMessage(t, "Enter an integer number");
            }
        }
    }
    
    //Displays message on screen
    public void sendMessage(TextListener t, String message){
        t.displayMessage(message); 
    }
    
    //Calls the circle's inversion method
    public void invert(int index1, int index2, Grapher grapher){
        NumericCircle inverter = (NumericCircle) grapher.getShape(index1);
        NumericShape invertee =  grapher.getShape(index2);
        NumericPoints inverted = inverter.geoInvert(invertee.getPoints());
        inverted.setName("Inverted " + invertee.getName());
        inverted.setColor(invertee.getColor());
        grapher.addShape(inverted);
        grapher.repaint();
    }
    
    //An array of colors 
    public void initializeColors(){
        colors = new Color[]{Color.cyan, Color.red, Color.green, Color.orange, Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.PINK};
    }
    
    //Cycle through the array
    public Color getNextColor(int number){
        number = (number - 1) % colors.length;
        return colors[number];
    }
}
