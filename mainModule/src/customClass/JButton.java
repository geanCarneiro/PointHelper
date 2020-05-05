package customClass;

import javax.swing.*;

public class JButton extends javax.swing.JButton {

    public JButton(String label, Action action){
        super(label);
        this.addActionListener(action);
    }

}
