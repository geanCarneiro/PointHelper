package views;

import customClass.JButton;
import customClass.JFrame;
import customClass.JPanel;
import customClass.JLabel;
import customClass.JFormattedTextField;
import customClass.JXDatePicker;
import main.Main;
import persistencia.GenericDAO;
import persistencia.Jornada;
import persistencia.JornadaDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Principal extends JFrame {

    private JXDatePicker datePicker;
    private JButton btnRemoverJornada;
    private JFormattedTextField txtInicioExp;
    private JFormattedTextField txtInicioAlm;
    private JFormattedTextField txtFimAlm;
    private JFormattedTextField txtFimExp;

    private final Action COPIAREXPLICACAOACTION = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int largura = 25;
            String info = setLengthToString("Inicio do Expediente", largura) + txtInicioExp.getText() + "\n";
            if(txtInicioAlm.getValue() != null){
                info += setLengthToString("Inicio do Almoço", largura) + txtInicioAlm.getText() + "\n" +
                        setLengthToString("Fim do Almoço", largura) + txtFimAlm.getText() + "\n";
            }
            info += setLengthToString("Fim do Expediente", largura) + txtFimExp.getText();
            copiarString(info);
        }
    };

    private final Action DISTRIBUIRHORARIO = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LimiteHoraDialog dialog = new LimiteHoraDialog();
            String limiteHora = dialog.run();

            if (limiteHora == null) return;

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            LocalDateTime limite;
            LocalDateTime inicioExp;
            LocalDateTime inicioAlm;
            LocalDateTime fimAlm;
            LocalDateTime fimExp;
            try {
                limite = dateToLocalDateTime(dateFormat.parse(limiteHora));
                inicioExp = dateToLocalDateTime(dateFormat.parse((String) txtInicioExp.getValue()));
                inicioAlm = (txtInicioAlm.getValue() != null) ? dateToLocalDateTime(dateFormat.parse((String) txtInicioAlm.getValue())) : LocalDateTime.MIN;
                fimAlm = (txtFimAlm.getValue() != null) ? dateToLocalDateTime(dateFormat.parse((String) txtFimAlm.getValue())) : LocalDateTime.MIN;
                fimExp = dateToLocalDateTime(dateFormat.parse((String) txtFimExp.getValue()));
            } catch (ParseException ex) {
                ex.printStackTrace();
                return;
            }

            LocalDateTime duracao = fimExp.minusHours(inicioExp.getHour()).minusMinutes(inicioExp.getMinute());
            LocalDateTime duracaoAlm = fimAlm.minusHours(inicioAlm.getHour()).minusMinutes(inicioAlm.getMinute());

            duracao.minusHours(duracaoAlm.getHour()).minusMinutes(duracaoAlm.getMinute());

            if (duracao.isBefore(limite)) return;

            LocalDateTime sobra = duracao.minusHours(limite.getHour()).minusMinutes(limite.getMinute());

            fimAlm = fimExp.minusHours(limite.getHour()).minusMinutes(limite.getMinute());
            inicioAlm = fimAlm.minusHours(Main.DURACAOMINALMOCO.getHour()).minusMinutes(Main.DURACAOMINALMOCO.getMinute());
            inicioExp = inicioAlm.minusHours(sobra.getHour()).minusMinutes(sobra.getMinute());

            txtInicioExp.setText(localDateTimeToString(inicioExp));
            txtInicioAlm.setText(localDateTimeToString(inicioAlm));
            txtFimAlm.setText(localDateTimeToString(fimAlm));
            txtFimExp.setText(localDateTimeToString(fimExp));

        }
    };

    private final Action REMOVERJORNADA = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!GenericDAO.isConnectionOpen()) return;

            GenericDAO.delete(JornadaDAO.getByDate(datePicker.getStringDate()));
            GenericDAO.commit();
            loadInfo(null);
        }
    };

    public Principal() {
        super("PointHelper", false);
        this.setResizable(false);

        this.datePicker = new JXDatePicker(getDatePropertyChangeListener());
        this.btnRemoverJornada = new JButton("Remover", REMOVERJORNADA);
        this.txtInicioExp = new JFormattedTextField(getTextFocusListener(), Main.TIMEMASK);
        this.txtInicioAlm = new JFormattedTextField(getTextFocusListener(), Main.TIMEMASK);
        this.txtFimAlm = new JFormattedTextField(getTextFocusListener(), Main.TIMEMASK);
        this.txtFimExp = new JFormattedTextField(getTextFocusListener(), Main.TIMEMASK);

        JLabel.defaultWidth = 145;

        this.addComponent(JPanel.createPanel(BoxLayout.Y_AXIS, new Insets(10, 10, 10, 10),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 5, 0),
                        new JLabel("data da jornada: "),
                        this.datePicker
                ),
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
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(0, 0, 10, 0),
                        new JLabel("fim do expediente: ", SwingConstants.RIGHT),
                        this.txtFimExp,
                        new JButton("Set", getSetTime(this.txtFimExp)),
                        new JButton("Copiar", getCopyText(this.txtFimExp))
                    ),
                new JSeparator(),
                JPanel.createPanel(BoxLayout.X_AXIS, new Insets(10, 0, 5, 0),
                        new JButton("Copiar Explicação", this.COPIAREXPLICACAOACTION),
                        new JButton("Distribuir Horario...", this.DISTRIBUIRHORARIO),
                        this.btnRemoverJornada
                )
            ));

        this.loadBackup();

        this.addWindowListener(this.getWindowListener());
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String localDateTimeToString(LocalDateTime localDateTime){
        return String.format( "%02d:%02d", localDateTime.getHour(), localDateTime.getMinute() );
    }

    private FocusListener getTextFocusListener(){
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                salvarJornada();
            }
        };
    }

    private PropertyChangeListener getDatePropertyChangeListener (){
        return new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(datePicker == null) return;
                Date date = datePicker.getDate();
                if(date != null) {
                    String formattedDate = Main.SIMPLE_DATE_FORMAT.format(date);
                    Jornada jornada = JornadaDAO.getByDate(getNow());
                    if (jornada != null && !jornada.isJornadaValida()) {
                        GenericDAO.delete(jornada);
                        GenericDAO.commit();
                    }
                    loadInfo(GenericDAO.isConnectionOpen() ? JornadaDAO.getByDate(formattedDate) : null);
                }
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
                if(!datePicker.getStringDate().equals(getNow()) || isJornadaValida()) {
                    System.exit(0);
                } else {
                    if(GenericDAO.isConnectionOpen()) {
                        String msg = "A jornada está no formato invalido e, se fechar, ela será perdida.\n Tem certeza que deseja fechar?";
                        int opt = JOptionPane.showConfirmDialog(null, msg, "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opt == JOptionPane.YES_OPTION) {
                                Jornada jornada = JornadaDAO.getByDate(getNow());
                                if (jornada != null) {
                                    GenericDAO.delete(jornada);
                                    GenericDAO.commit();
                                }
                                GenericDAO.closeConnection();
                            System.exit(0);
                        }
                    }
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

        if (!GenericDAO.isConnectionOpen()) return;

        loadInfo(JornadaDAO.getByDate(datePicker.getStringDate()));
    }

    private void loadInfo(Jornada jornada){

        this.datePicker.setFlaggedDatesByJornadas(GenericDAO.query(Jornada.class));
        this.btnRemoverJornada.setVisible(jornada != null);
        this.txtInicioExp.setValue( (jornada == null) ? null : jornada.getInicioExpediente() );
        this.txtInicioAlm.setValue( (jornada == null) ? null : jornada.getInicioAlmoco() );
        this.txtFimAlm.setValue( (jornada == null) ? null : jornada.getFimAlmoco() );
        this.txtFimExp.setValue( (jornada == null) ? null : jornada.getFimExpediente() );

    }

    private boolean salvarJornada(){
        if(GenericDAO.isConnectionOpen()) {
            Jornada novaJornada = new Jornada();
            novaJornada.setData(this.datePicker.getStringDate());
            novaJornada.setInicioExpediente(txtInicioExp.getValueAsString());
            novaJornada.setInicioAlmoco(txtInicioAlm.getValueAsString());
            novaJornada.setFimAlmoco(txtFimAlm.getValueAsString());
            novaJornada.setFimExpediente(txtFimExp.getValueAsString());
            novaJornada.calcularDuracao();

            JornadaDAO.saveByDate(novaJornada);
            GenericDAO.commit();
            this.btnRemoverJornada.setVisible(isJornadaValida());
            if(isJornadaValida()) this.datePicker.setFlaggedDatesByJornadas(GenericDAO.query(Jornada.class));
            return true;
        }
        return false;
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
                target.setValue(new SimpleDateFormat("HH:mm").format(new Date()));
                salvarJornada();
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

    private String getNow(){
        return LocalDate.now().format(Main.DATEFORMATTER);
    }

    private boolean isJornadaValida(){
        Jornada jornada = new Jornada();

        jornada.setInicioExpediente(this.txtInicioExp.getValueAsString());
        jornada.setInicioAlmoco(this.txtInicioAlm.getValueAsString());
        jornada.setFimAlmoco(this.txtFimAlm.getValueAsString());
        jornada.setFimExpediente(this.txtFimExp.getValueAsString());

        return jornada.isJornadaValida();
    }
}
