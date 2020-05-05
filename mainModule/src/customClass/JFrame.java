package customClass;

import javax.swing.*;

public class JFrame extends javax.swing.JFrame {

    public JFrame(String title, boolean exitOnClose) {
        super(title);
        this.setDefaultCloseOperation( exitOnClose ? EXIT_ON_CLOSE : DO_NOTHING_ON_CLOSE );
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public JFrame(String title, boolean exitOnClose, JPanel panel) {
        this(title, exitOnClose);
        this.addComponent(panel);
    }

    public void addComponent(JComponent component) {
        this.getContentPane().add(component);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
