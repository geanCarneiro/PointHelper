package customClass;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

public class JFormattedTextField extends javax.swing.JFormattedTextField {

    private static char placeHolder = '_';

    private String mask = "";

    public JFormattedTextField(){
        this.setPreferredSize(new Dimension(0, 30));
        this.setColumns(10);
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if(getText().matches(".*\\d.*")) completeField();
            }
        });
    }

    public JFormattedTextField(FocusListener focusListener){
        this();
        this.addFocusListener(focusListener);
    }

    public JFormattedTextField(String mask){
        this();
        this.setMask(mask);
    }

    public JFormattedTextField(FocusListener focusListener, String mask) {
        this(focusListener);
        this.setMask(mask);
    }

    public void completeField(){
        char[] textArray = this.getText().toCharArray();
        for(int i = 0; i < textArray.length; i++){
            if(textArray[i] == placeHolder) {
                switch (mask.charAt(i)){
                    case '#':
                        textArray[i] = '0';
                        break;
                }
            }
        }
        this.setText(new String(textArray));
    }

    private void setMask(String mask) {
        this.mask = mask;
        try {
            MaskFormatter maskFormatter = new MaskFormatter(mask);
            maskFormatter.setPlaceholderCharacter(placeHolder);
            this.setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getText() {
        if(this.getValue() == null) return "";
        return super.getText();
    }
}
