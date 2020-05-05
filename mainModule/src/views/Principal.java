package views;

import customClass.*;
import customClass.JButton;
import customClass.JFrame;
import customClass.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
                        new JButton("Set", getSetTime(this.txtInicioExp)),
                        new JButton("Copiar", getCopyText(this.txtInicioExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("inicio do almoço: "),
                        this.txtInicioAlm = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtInicioAlm)),
                        new JButton("Copiar", getCopyText(this.txtInicioAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("fim do almoço: "),
                        this.txtFimAlm = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtFimAlm)),
                        new JButton("Copiar", getCopyText(this.txtFimAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("fim do expediente: "),
                        this.txtFimExp = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtFimExp)),
                        new JButton("Copiar", getCopyText(this.txtFimExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JButton("Copiar Explicação", getInfoPonto())
                )
            ));
    }

    private Action getInfoPonto(){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int largura = 25;
                copiarString(setLengthToString("Inicio do Expediente", largura) + txtInicioExp.getText() + "\n" +
                             setLengthToString("Inicio do Almoço", largura) + txtInicioAlm.getText() + "\n" +
                             setLengthToString("Fim do Almoço", largura) + txtFimAlm.getText() + "\n" +
                             setLengthToString("Fim do Expediente", largura) + txtFimExp.getText());
            }
        };
    }

    private String setLengthToString(String str, int length){

        if(str.length() < length)
            str += " ".repeat(length - str.length());

        return str;
    }

    private Action getSetTime(JTextField target) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                target.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            }
        };
    }

    private Action getCopyText(JTextField target) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiarString(target.getText());
            }
        };
    }

    private void copiarString(String str) {
        StringSelection selection = new StringSelection(str);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
