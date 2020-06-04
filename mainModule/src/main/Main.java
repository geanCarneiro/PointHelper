package main;

import views.Principal;

import java.io.File;
import java.time.LocalTime;

public class Main {

    public static final File BACKUP = new File("backup.csv");
    public static final String TIMEMASK = "##:##";
    public static final LocalTime DURACAOALMOCO = LocalTime.of(0, 30);
    public static final String DEFAULTLIMITEHORA = "06:00";

    public static void main(String[] args) {
        new Principal();
    }
}
