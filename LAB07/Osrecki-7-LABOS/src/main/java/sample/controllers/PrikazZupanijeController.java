package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.hr.java.covidportal.model.Zupanija;

/**
 * Kontoler za prikaz zupanije
 */
public class PrikazZupanijeController {

    @FXML
    Label sifraZupanijePrikaz;
    @FXML
    Label idZupanijePrikaz;
    @FXML
    Label nazivZupanijePrikaz;
    @FXML
    Label brojStanovnikaPrikaz;
    @FXML
    Label brojZarazenihPrikaz;


    // zadatak 2

    /**
     * Prikazuje podatke županije
     *
     * @param zupanija podatak o županiji
     */
    public void prikaziZupaniju(Zupanija zupanija) {
        System.out.println(zupanija);
        sifraZupanijePrikaz.setText(zupanija.getSifra());
        idZupanijePrikaz.setText(zupanija.getId().toString());
        nazivZupanijePrikaz.setText(zupanija.getNaziv());
        brojStanovnikaPrikaz.setText(zupanija.getBrojStanovnika().toString());
        brojZarazenihPrikaz.setText(zupanija.getBrojZarazenih().toString());
    }
}
