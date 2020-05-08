package main;

import views.Principal;

import java.io.File;

public class Main {

    public static final File BACKUP = new File("backup.csv");

    public static void main(String[] args) {
        new Principal();
    }
}
