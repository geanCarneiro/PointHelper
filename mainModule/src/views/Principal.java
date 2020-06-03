package views;

import customClass.JButton;
import customClass.JFrame;
import customClass.JPanel;
import customClass.JLabel;
import customClass.JTextField;
import customClass.JFormattedTextField;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Principal extends JFrame {

    private JFormattedTextField txtInicioExp;
    private JFormattedTextField txtInicioAlm;
    private JFormattedTextField txtFimAlm;
    private JFormattedTextField txtFimExp;


    public Principal() {
        super("PointHelper", false);

        this.txtInicioExp = new JFormattedTextField(getFocusListener(), Main.TIMEMASK);
        this.txtInicioAlm = new JFormattedTextField(getFocusListener(), Main.TIMEMASK);
        this.txtFimAlm = new JFormattedTextField(getFocusListener(), Main.TIMEMASK);
        this.txtFimExp = new JFormattedTextField(getFocusListener(), Main.TIMEMASK);

        JLabel.defaultWidth = 145;

        this.loadBackup();

        this.addComponent(JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(10, 10, 10, 10),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("inicio do expediente: ", SwingConstants.RIGHT),
                        this.txtInicioExp,
                        new JButton("Set", getSetTime(this.txtInicioExp)),
                        new JButton("Copiar", getCopyText(this.txtInicioExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("inicio do almoço: ", SwingConstants.RIGHT),
                        this.txtInicioAlm,
                        new JButton("Set", getSetTime(this.txtInicioAlm)),
                        new JButton("Copiar", getCopyText(this.txtInicioAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("fim do almoço: ", SwingConstants.RIGHT),
                        this.txtFimAlm,
                        new JButton("Set", getSetTime(this.txtFimAlm)),
                        new JButton("Copiar", getCopyText(this.txtFimAlm))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("fim do expediente: ", SwingConstants.RIGHT),
                        this.txtFimExp,
                        new JButton("Set", getSetTime(this.txtFimExp)),
                        new JButton("Copiar", getCopyText(this.txtFimExp))
                    ),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JButton("Copiar Explicação", getInfoPonto()),
                        new JButton("Distribuir Horario...", distribuirHorario())
                )
            ));
        this.addWindowListener(this.getWindowListener());
    }

    private Action distribuirHorario() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LimiteHoraDialog dialog = new LimiteHoraDialog();
                String limiteHora = dialog.run();

                if (limiteHora == null) return;

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                LocalDateTime limite;
                LocalDateTime inicioExp;
                LocalDateTime fimExp;
                try {
                    limite = dateToLocalDateTime(dateFormat.parse(limiteHora));
                    inicioExp = dateToLocalDateTime(dateFormat.parse((String) txtInicioExp.getValue()));
                    fimExp = dateToLocalDateTime(dateFormat.parse((String) txtFimExp.getValue()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    return;
                }

                LocalDateTime duracao = fimExp.minusHours(inicioExp.getHour()).minusMinutes(inicioExp.getMinute());

                if (duracao.isBefore(limite)) return;

                LocalDateTime sobra = duracao.minusHours(limite.getHour()).minusMinutes(limite.getMinute());

                LocalDateTime fimAlm = fimExp.minusHours(limite.getHour()).minusMinutes(limite.getMinute());
                LocalDateTime inicioAlm = fimAlm.minusHours(Main.DURACAOALMOCO.getHour()).minusMinutes(Main.DURACAOALMOCO.getMinute());
                inicioExp = inicioAlm.minusHours(sobra.getHour()).minusMinutes(sobra.getMinute());

                txtInicioExp.setText(localDateTimeToString(inicioExp));
                txtInicioAlm.setText(localDateTimeToString(inicioAlm));
                txtFimAlm.setText(localDateTimeToString(fimAlm));
                txtFimExp.setText(localDateTimeToString(fimExp));

            }
        };
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String localDateTimeToString(LocalDateTime localDateTime){
        return String.format( "%02d:%02d", localDateTime.getHour(), localDateTime.getMinute() );
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
                saveInfos();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                saveInfos();
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
                if(!txtInicioAlm.getText().trim().isEmpty()){
                    info += setLengthToString("Inicio do Almoço", largura) + txtInicioAlm.getText() + "\n" +
                            setLengthToString("Fim do Almoço", largura) + txtFimAlm.getText() + "\n";
                }
                info += setLengthToString("Fim do Expediente", largura) + txtFimExp.getText();
                copiarString(info);
            }
        };
    }

    private String setLengthToString(String str, int length) {
        return setLengthToString(str, length, SwingConstants.LEFT);
    }

    private String setLengthToString(String str, int length, int align){

        if(align == SwingConstants.LEFT)
            length = -length;

        return String.format("%" + length + "s", str);
    }

    private  Action getSetTime(JFormattedTextField target) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                target.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                saveInfos();
            }
        };
    }

    private Action getCopyText(JFormattedTextField target) {
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
