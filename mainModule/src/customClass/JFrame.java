package customClass;

public class JFrame extends javax.swing.JFrame {

    public JFrame(String title, boolean exitOnClose, JPanel panel) {
        super(title);
        this.setDefaultCloseOperation( exitOnClose ? EXIT_ON_CLOSE : DO_NOTHING_ON_CLOSE );
        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
