package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.sample.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SpremiBolestNit implements Runnable{

    private static Connection veza;
    private Bolest bolest;

    public SpremiBolestNit(Bolest novaBolest) {
        bolest = novaBolest;
    }

    @Override
    public void run() {
        try {
            otvoriVezuSBazom();

            BazaPodataka.spremiNovuBolest(veza, bolest);

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
