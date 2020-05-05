package views;

import customClass.*;
import customClass.JButton;
import customClass.JFrame;
import customClass.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Principal extends JFrame {

    private JTextField txtInicioExp;
    private JTextField txtInicioAlm;
    private JTextField txtFimAlm;
    private JTextField txtFimExp;

    public Principal() {
        super("PointHelper", true);
        this.addComponent(JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(10, 10, 10, 10),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("inicio do expediente: "),
                        this.txtInicioExp = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtInicioExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("inicio do almoço: "),
                        this.txtInicioAlm = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtInicioAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("fim do almoço: "),
                        this.txtFimAlm = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtFimAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("fim do expediente: "),
                        this.txtFimExp = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtFimExp))
                    )
            ));
    }

    private Action getSetTime(JTextField target) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                target.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            }
        };
    }
}
