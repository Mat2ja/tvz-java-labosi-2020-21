package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PocetniEkranController implements Initializable {

    /**
     * Prikazuje ekran za pretragu županija
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame);

        Main.getMainStage().setTitle("Pretraga županija");
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    /**
     * Prikazuje ekran za pretragu simptoma
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame);

        Main.getMainStage().setTitle("Pretraga simptoma");
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    /**
     * Prikazuje ekran za pretragu bolesti
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame);

        Main.getMainStage().setTitle("Pretraga bolesti");
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    /**
     * Prikazuje ekran za pretragu virusa
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame);

        Main.getMainStage().setTitle("Pretraga virusa");
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    /**
     * Prikazuje ekran za pretragu osoba
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame);

        Main.getMainStage().setTitle("Pretraga osoba");
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}