package customClass;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class JPanel extends javax.swing.JPanel {

    public static JPanel createPanel(int axis, Insets insets, JComponent... components) {
        JPanel out = new JPanel();

        out.setBorder(new EmptyBorder(insets));
        out.setLayout(new BoxLayout(out, axis));
        Arrays.asList(components).forEach(out::add);

        return out;
    }

}
