package customClass;

import javax.swing.*;
import java.awt.*;

public class JLabel extends javax.swing.JLabel {

    public static int defaultWidth = 200;


    public JLabel(String txt, int width, int alignment) {
        super(txt, alignment);
        this.setFixedSize(new Dimension(width, 30));
    }

    public JLabel(String txt, int alignment) {
        this(txt, JLabel.defaultWidth, alignment);
    }

    public JLabel(String txt) {
        this(txt, txt.length()*5, SwingConstants.LEFT);
    }

    private void setFixedSize(Dimension dimension) {
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
    }

}
