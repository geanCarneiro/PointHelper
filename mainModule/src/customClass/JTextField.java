package customClass;

import java.awt.event.FocusListener;

public class JTextField extends javax.swing.JTextField {

    public JTextField(FocusListener focusListener){
        super(10);
        this.addFocusListener(focusListener);
    }

}
