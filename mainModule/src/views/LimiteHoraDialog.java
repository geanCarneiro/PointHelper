package views;

import customClass.JButton;
import customClass.JFormattedTextField;
import customClass.JLabel;
import customClass.JPanel;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LimiteHoraDialog extends JDialog {

    private JFormattedTextField input;
    private String output = null;

    public LimiteHoraDialog() {
        super((JDialog) null, "LIMITE DE HORA/DIA", true);

        input = new customClass.JFormattedTextField(Main.TIMEMASK);
        input.setText(Main.DEFAULTLIMITEHORA);

        this.getContentPane().add(JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(10, 10, 10, 10),
                    JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(0, 0, 0, 0),
                            new JLabel("Entre com o limite de hora/dia:", SwingConstants.LEFT),
                            this.input
                        ),
                    JPanel.createPanel(BoxLayout.X_AXIS, new Insets(5, 0, 0, 0),
                            new JButton("Ok", getOkAction()),
                            new JButton("Cancelar", getCancelarAction())
                        )
                ));


        this.pack();
        this.setLocationRelativeTo(null);
    }

    public String run() {
        this.setVisible(true);
        return output;
    }

    private Action getOkAction(){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output = (String) input.getValue();
                dispose();
            }
        };
    }

    private Action getCancelarAction(){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
    }
}
