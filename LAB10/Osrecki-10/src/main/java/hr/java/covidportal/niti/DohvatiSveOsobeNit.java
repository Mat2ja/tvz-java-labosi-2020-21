package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.sample.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DohvatiSveOsobeNit implements Runnable {

    private static Connection veza;
    public static List<Osoba> osobe;

    public DohvatiSveOsobeNit() {
        osobe = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            otvoriVezuSBazom();

            BazaPodataka.dohvatiSveZupanije(veza);
            BazaPodataka.dohvatiSveSimptome(veza);
            BazaPodataka.dohvatiSveBolesti(veza);
            BazaPodataka.dohvatiSveOsobe(veza);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            zatvoriVezuSBazom();
        }
    }


    public synchronized void otvoriVezuSBazom() throws IOException, SQLException {
        if (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
                System.out.println("Veza je zauzeta");
            } catch (InterruptedException e) {
                Main.logger.error(e.getMessage());
            }
        }

        veza = BazaPodataka.connectToDatabase();
    }

    public synchronized void zatvoriVezuSBazom() {
        try {
            BazaPodataka.disconnectFromDatabase(veza);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        notifyAll();
    }

    public static List<Osoba> getOsobe() {
        return osobe;
    }
}
