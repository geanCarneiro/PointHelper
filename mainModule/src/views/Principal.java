package views;

import customClass.*;
import customClass.JButton;
import customClass.JFrame;
import customClass.JPanel;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Principal extends JFrame {

    private JTextField txtInicioExp;
    private JTextField txtInicioAlm;
    private JTextField txtFimAlm;
    private JTextField txtFimExp;

    public Principal() {
        super("PointHelper", false);
        this.addComponent(JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(10, 10, 10, 10),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("inicio do expediente: "),
                        this.txtInicioExp = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtInicioExp)),
                        new JButton("Copiar", getCopyText(this.txtInicioExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel(" ".repeat(7) + "inicio do almoço: "),
                        this.txtInicioAlm = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtInicioAlm)),
                        new JButton("Copiar", getCopyText(this.txtInicioAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel(" ".repeat(11) + "fim do almoço: "),
                        this.txtFimAlm = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtFimAlm)),
                        new JButton("Copiar", getCopyText(this.txtFimAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel(" ".repeat(4) + "fim do expediente: "),
                        this.txtFimExp = new JTextField(10),
                        new JButton("Set", getSetTime(this.txtFimExp)),
                        new JButton("Copiar", getCopyText(this.txtFimExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JButton("Copiar Explicação", getInfoPonto())
                )
            ));
        this.addWindowListener(this.getWindowListener());
        this.txtInicioExp.addFocusListener(this.getFocusListener());
        this.loadBackup();
    }

    private FocusListener getFocusListener(){
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                saveInfos();
            }
        };
    }

    private WindowListener getWindowListener() {
        return new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                int opt = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fechar?", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(opt == JOptionPane.YES_OPTION) {
                    Main.BACKUP.delete();
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
    }

    private void loadBackup(){
        try (Scanner scan = new Scanner(Main.BACKUP)) {
            String[] infos = scan.nextLine().split(";");
            if(infos.length > 0) this.txtInicioExp.setText(infos[0]);
            if(infos.length > 1) this.txtInicioAlm.setText(infos[1]);
            if(infos.length > 2) this.txtFimAlm.setText(infos[2]);
            if(infos.length > 3) this.txtFimExp.setText(infos[3]);
        } catch (FileNotFoundException e) {
            this.saveInfos();
        }
    }

    private void saveInfos(){
        try (FileWriter writer = new FileWriter(Main.BACKUP)){
            writer.write(this.txtInicioExp.getText() + ";"
                        + this.txtInicioAlm.getText() + ";"
                        + this.txtFimAlm.getText() + ";"
                        + this.txtFimExp.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Action getInfoPonto(){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int largura = 25;
                String info = setLengthToString("Inicio do Expediente", largura) + txtInicioExp.getText() + "\n";
                if(!txtInicioAlm.getText().isBlank()){
                    info += setLengthToString("Inicio do Almoço", largura) + txtInicioAlm.getText() + "\n" +
                            setLengthToString("Fim do Almoço", largura) + txtFimAlm.getText() + "\n";
                }
                info += setLengthToString("Fim do Expediente", largura) + txtFimExp.getText();
                copiarString(info);
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
                saveInfos();
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
