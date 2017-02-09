

package graphingprogram;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class implements the JTextField to get user commands.  It was made
 * based on the JavaDocs tutorial.
 * @author Simon Langowski slangows@purdue.edu
 */
public class TextListener extends JPanel implements ActionListener{
    protected JTextField textField;
    protected JTextArea textArea;
    private String entry;
    private Boolean change;

    public TextListener(){
        super(new GridBagLayout());
        textField = new JTextField(20);
        textField.addActionListener(this);
        
        textArea = new JTextArea(5,20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
 
	c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        
        textArea.append("Welcome to the graphing program!" + "\n");
        entry = "";
        change = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        entry = textField.getText();
        textArea.append(entry + "\n");
        textField.selectAll();
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
        change = true;
        if (entry.equalsIgnoreCase("q")){
            System.exit(0);
        }
    }
    
    public boolean wasChange(){
        return change;
    }
    
    //Someone with knowledge of thread safe should improve this so this isn't necessary
    //Variables only refresh to the GraphingProgram class whenever a print statement is called (found through debugging)
    //Calls an empty print statement to make variable update.
    public void refresh(){
        System.out.print("");
    }
    
    public String getEntry(){
        change = false;
        return entry;
    }
    
    public void displayMessage(String message){
        textArea.append(message + "\n");
    }

}
