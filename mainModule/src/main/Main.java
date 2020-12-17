package main;

import persistencia.GenericDAO;
import persistencia.Jornada;
import views.Principal;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class Main {

    public static final String DRIVERPATH = "org.h2.Driver";
    public static final String URL = "jdbc:h2:./data";
    public static final String USER = "super";
    public static final String PASS = "123";

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    public static final String TIMEMASK = "##:##";
    public static final LocalTime DURACAOMINALMOCO = LocalTime.of(0, 30);
    public static final String DEFAULTLIMITEHORA = "06:00";

    public static void main(String[] args) {

        try {
            GenericDAO.openConnection();

            if (GenericDAO.isConnectionOpen()) GenericDAO.createTableFromEntity(Jornada.class);
        } catch (Exception ex) {
            StringBuilder builder = new StringBuilder();
            builder.append(ex.getClass().getSimpleName() + ": " + ex.getMessage()+"\n\n");
            Stream.of(ex.getStackTrace()).forEach(stackTraceElement -> builder.append(stackTraceElement.toString() + "\n"));
            JOptionPane.showMessageDialog(null, "Erro ao conectar com banco de dados\n\n" + builder, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        new Principal();
    }
}
