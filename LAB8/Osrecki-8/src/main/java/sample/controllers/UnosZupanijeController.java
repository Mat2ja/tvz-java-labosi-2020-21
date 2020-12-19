package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler unosa županija
 */
public class UnosZupanijeController extends UnosController implements Initializable {

    private static List<Zupanija> listaZupanija;
    private static Long brojZupanija;

    private List<TextField> textFields;

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

        textFields = new ArrayList<>(Arrays.asList(nazivZupanije, brStanovnikaZupanije, brZarazenihZupanije));

        prikaziStatus();

        nazivZupanije.textProperty().addListener((obs, oldText, newText) -> validateTextField(nazivZupanije, newText));
        brStanovnikaZupanije.textProperty().addListener((obs, oldText, newText) -> validateTextFieldNumber(brStanovnikaZupanije, newText));
        brZarazenihZupanije.textProperty().addListener((obs, oldText, newText) -> validateTextFieldNumber(brZarazenihZupanije, newText));
    }

    /**
     * Dodaje novu županiju
     */
    public void dodaj() {
        String naziv = nazivZupanije.getText().toUpperCase();
        Integer brStanovnika = ucitajBroj(brStanovnikaZupanije.getText());
        Integer brZarazenih = ucitajBroj(brZarazenihZupanije.getText());


        Boolean valIme = validateTextField(nazivZupanije,naziv);
        Boolean valBrStan = validateTextFieldNumber(brStanovnikaZupanije,brStanovnika);
        Boolean valBrojZar = validateTextFieldNumber(brZarazenihZupanije,brZarazenih);

        if (!(valIme && valBrStan && valBrojZar)) {
            prikaziErrorUnosAlert("Unos županije", "Unijeli ste županiju s nedozvoljenim vrijednostima.");
            return;
        }

        Long id = ++brojZupanija;
        Zupanija novaZupanija = new Zupanija(id, naziv, brStanovnika, brZarazenih);
        UcitavanjePodataka.zapisiZupaniju(novaZupanija);
        listaZupanija.add(novaZupanija);

        prikaziSuccessUnosAlert(
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

    /**
     * Prikazuje status
     */
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + brojZupanija + " županija");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        textFields.forEach(TextInputControl::clear);
        resetIndicators();
    }

    public void resetIndicators() {
        textFields.forEach(Main::makniErrorIndicator);
    }


}
