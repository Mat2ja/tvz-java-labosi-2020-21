package main.java.sample.controllers;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.hr.java.covidportal.model.Utility;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler unosa županija
 */
public class UnosZupanijeController implements Initializable {

    private static List<Zupanija> listaZupanija;
    private static Long brojZupanija;

    @FXML
    private TextField nazivZupanije;
    @FXML
    private TextField brStanovnikaZupanije;
    @FXML
    private TextField brZarazenihZupanije;
    @FXML
    private Label status;


    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaZupanija = UcitavanjePodataka.ucitajZupanije();
        brojZupanija = (long) listaZupanija.size();

        prikaziStatus();
    }

    /**
     * Dodaje novu županiju
     */
    public void dodaj() {
        String naziv = nazivZupanije.getText().toUpperCase();
        Integer brStanovnika = Utility.ucitajBroj(brStanovnikaZupanije.getText());
        Integer brZarazenih = Utility.ucitajBroj(brZarazenihZupanije.getText());
        
        if (naziv.isBlank() || brStanovnika == null || brZarazenih == null) {
            Main.prikaziErrorUnosAlert("Unos županije", "Unijeli ste županiju s nedozvoljenim vrijednostima.");
            return;
        }

        Long id = ++brojZupanija;
        Zupanija novaZupanija = new Zupanija(id, naziv, brStanovnika, brZarazenih);
        UcitavanjePodataka.zapisiZupaniju(novaZupanija);
        listaZupanija.add(novaZupanija);

        Main.prikaziSuccessUnosAlert(
                "Unos županije", "Županija dodana", "Unijeli ste županiju: " + novaZupanija);

        ocistiUnos();
        prikaziStatus();
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + brojZupanija + " županija");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        nazivZupanije.clear();
        brStanovnikaZupanije.clear();
        brZarazenihZupanije.clear();
    }
}
