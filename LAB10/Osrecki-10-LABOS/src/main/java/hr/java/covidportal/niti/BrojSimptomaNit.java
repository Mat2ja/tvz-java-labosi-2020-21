package main.java.hr.java.covidportal.niti;

import javafx.scene.control.Label;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.sample.Main;
import main.java.sample.controllers.PocetniEkranController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class BrojSimptomaNit implements Runnable {

    private static Connection veza;

    @Override
    public void run() {
        try {
            otvoriVezuSBazom();

            Integer brojSimptoma = BazaPodataka.brojSimptoma(veza);
            PocetniEkranController.brSimptoma = brojSimptoma;
            System.out.println("Broj simptoma:" + brojSimptoma);

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
