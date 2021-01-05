package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler unosa županija
 */
public class UnosZupanijeController extends UnosController implements Initializable {

    private static List<Zupanija> listaZupanija;

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
        listaZupanija = BazaPodataka.dohvatiSveZupanije();

        prikaziStatus();
        inicijalizirajListenere();
    }


    /**
     * Dodaje novu županiju
     */
    @Override
    public void dodaj() {
        String naziv = toTitleCase(nazivZupanije.getText(), "-");
        String brStanovnikaUnos = brStanovnikaZupanije.getText();
        String brZarazenihUnos = brZarazenihZupanije.getText();

        Boolean valIme = validateField(nazivZupanije, naziv);
        Boolean valBrStan = validateTextFieldNumber(brStanovnikaZupanije, brStanovnikaUnos);
        Boolean valBrojZar = validateTextFieldNumber(brZarazenihZupanije, brZarazenihUnos);

        if (!(valIme && valBrStan && valBrojZar)) {
            prikaziErrorUnosAlert("Unos županije", "Unijeli ste županiju s nedozvoljenim vrijednostima.");
            return;
        }

        Integer brStanovnika = Integer.valueOf(brStanovnikaUnos);
        Integer brZarazenih = Integer.valueOf(brZarazenihUnos);
        Zupanija novaZupanija = new Zupanija(naziv, brStanovnika, brZarazenih);

        BazaPodataka.spremiNovuZupaniju(novaZupanija);

        prikaziSuccessUnosAlert(
                "Unos županije", "Županija dodana", "Unijeli ste županiju: " + novaZupanija);

        ocistiUnos();
        prikaziStatus();
    }

    /**
     * Prikazuje status
     */
    @Override
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + listaZupanija.size() + " županija");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    @Override
    public void ocistiUnos() {
        Arrays.asList(nazivZupanije, brStanovnikaZupanije, brZarazenihZupanije).forEach(TextInputControl::clear);
        resetIndicators();
    }

    /**
     * Resetira error indikatore
     */
    @Override
    public void resetIndicators() {
        Arrays.asList(nazivZupanije, brStanovnikaZupanije, brZarazenihZupanije).forEach(this::makniErrorIndicator);
    }

    /**
     * Incijalizira listenere
     */
    @Override
    public void inicijalizirajListenere() {
        nazivZupanije.textProperty()
                .addListener((obs, oldText, newText) -> validateField(nazivZupanije, newText));
        brStanovnikaZupanije.textProperty()
                .addListener((obs, oldText, newText) -> validateTextFieldNumber(brStanovnikaZupanije, newText));
        brZarazenihZupanije.textProperty()
                .addListener((obs, oldText, newText) -> validateTextFieldNumber(brZarazenihZupanije, newText));
    }

    public void izmijeniZupaniju(Zupanija zupanija) {
        nazivZupanije.setText(zupanija.getNaziv());
        brStanovnikaZupanije.setText(zupanija.getBrojStanovnika().toString());
        brZarazenihZupanije.setText(zupanija.getBrojZarazenih().toString());
    }
}
