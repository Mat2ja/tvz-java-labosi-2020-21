package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.sample.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SpremiSimptomNit implements Runnable{

    private static Connection veza;
    private Simptom simptom;

    public SpremiSimptomNit(Simptom noviSimptom) {
        simptom = noviSimptom;
    }

    @Override
    public void run() {
        try {
            otvoriVezuSBazom();

            BazaPodataka.spremiNoviSimptom(veza,simptom);

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
}
