package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.java.sample.Main;

import java.io.IOException;

/**
 * Kontoler glavnog izbornika
 */
public class PocetniEkranController {

    /**
     * Prikazuje novi ekran
     *
     * @param fxmlFile podatak o fxml datoteci koju prikazujemo
     * @param title    podatak o naslovu prozora
     */
    public void prikaziEkran(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlFile));
            Scene pretragaZupanijaScene = new Scene(root);

            Main.getMainStage().setTitle(title);
            Main.getMainStage().setScene(pretragaZupanijaScene);
        } catch (IOException e) {
            Main.logger.error("Greška kod prikaza ekrana " + title);
            e.printStackTrace();
        }
    }

    /**
     * Prikazuje ekran za pretragu županija
     */
    @FXML
    public void prikaziEkranZaPretraguZupanija() {
        prikaziEkran("pretragaZupanija.fxml", "Unos županije");
    }


    /**
     * Prikazuje ekran za unos županija
     */
    @FXML
    public void prikaziEkranZaUnosZupanije() {
        prikaziEkran("unosZupanije.fxml", "Unos županije");

    }

    /**
     * Prikazuje ekran za pretragu simptoma
     */
    @FXML
    public void prikaziEkranZaPretraguSimptoma() {
        prikaziEkran("pretragaSimptoma.fxml", "Pretraga simptoma");
    }

    /**
     * Prikazuje ekran za unos simptoma
     */
    @FXML
    public void prikaziEkranZaUnosSimptoma() {
        prikaziEkran("unosSimptoma.fxml", "Unos simptoma");
    }

    /**
     * Prikazuje ekran za pretragu bolesti
     */
    @FXML
    public void prikaziEkranZaPretraguBolesti() {
        prikaziEkran("pretragaBolesti.fxml", "Pretraga bolesti");
    }

    /**
     * Prikazuje ekran za unos bolesti
     */
    @FXML
    public void prikaziEkranZaUnosBolesti() {
        prikaziEkran("unosBolesti.fxml", "Unos bolesti");
    }


    /**
     * Prikazuje ekran za pretragu virusa
     */
    @FXML
    public void prikaziEkranZaPretraguVirusa() {
        prikaziEkran("pretragaVirusa.fxml", "Pretraga virusa");

    }

    /**
     * Prikazuje ekran za unos virusa
     */
    @FXML
    public void prikaziEkranZaUnosVirusa() {
        prikaziEkran("unosVirusa.fxml", "Unos virusa");

    }

    /**
     * Prikazuje ekran za pretragu osoba
     */
    @FXML
    public void prikaziEkranZaPretraguOsoba() {
        prikaziEkran("pretragaOsoba.fxml", "Pretraga osoba");
    }


    /**
     * Prikazuje ekran za unos osobe
     */
    @FXML
    public void prikaziEkranZaUnosOsobe() {
        prikaziEkran("unosOsobe.fxml", "Unos osobe");

    }

}