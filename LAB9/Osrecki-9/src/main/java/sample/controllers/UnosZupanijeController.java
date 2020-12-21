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

    private Integer idPromjenjenog;

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

        resetirajIdPromijenjenog();

        listaZupanija = UcitavanjePodataka.ucitajZupanije();
        brojZupanija = (long) listaZupanija.size();

        textFields = new ArrayList<>(Arrays.asList(nazivZupanije, brStanovnikaZupanije, brZarazenihZupanije));

        prikaziStatus();

        nazivZupanije.textProperty()
                .addListener((obs, oldText, newText) -> validateTextField(nazivZupanije, newText));
        brStanovnikaZupanije.textProperty()
                .addListener((obs, oldText, newText) -> validateTextFieldNumber(brStanovnikaZupanije, newText));
        brZarazenihZupanije.textProperty()
                .addListener((obs, oldText, newText) -> validateTextFieldNumber(brZarazenihZupanije, newText));
    }



    public void izmijeniZupaniju(Zupanija zupanija) {
        idPromjenjenog = zupanija.getId().intValue();
        listaZupanija.remove(zupanija);

        nazivZupanije.setText(zupanija.getNaziv());
        brStanovnikaZupanije.setText(zupanija.getBrojStanovnika().toString());
        brZarazenihZupanije.setText(zupanija.getBrojZarazenih().toString());
    }

    /**
     * Dodaje novu županiju
     */
    public void dodaj() {
        String naziv = nazivZupanije.getText().toUpperCase();
        String brStanovnikaUnos = brStanovnikaZupanije.getText();
        String brZarazenihUnos = brZarazenihZupanije.getText();

        Boolean valIme = validateTextField(nazivZupanije, naziv);
        Boolean valBrStan = validateTextFieldNumber(brStanovnikaZupanije, brStanovnikaUnos);
        Boolean valBrojZar = validateTextFieldNumber(brZarazenihZupanije, brZarazenihUnos);

        if (!(valIme && valBrStan && valBrojZar)) {
            prikaziErrorUnosAlert("Unos županije", "Unijeli ste županiju s nedozvoljenim vrijednostima.");
            return;
        }

        Long id;
        if (idPromjenjenog != -1) {
            id = idPromjenjenog.longValue();
        } else {
            id = ++brojZupanija;
        }

        Integer brStanovnika = Integer.valueOf(brStanovnikaUnos);
        Integer brZarazenih = Integer.valueOf(brZarazenihUnos);

        Zupanija novaZupanija = new Zupanija(id, naziv, brStanovnika, brZarazenih);
        listaZupanija.add(id.intValue() - 1, novaZupanija);

        if (idPromjenjenog != -1) {
            UcitavanjePodataka.clearZupanije();
            listaZupanija.forEach(UcitavanjePodataka::zapisiZupaniju);
        } else {
            UcitavanjePodataka.zapisiZupaniju(novaZupanija);
        }

        prikaziSuccessUnosAlert(
                "Unos županije", "Županija dodana", "Unijeli ste županiju: " + novaZupanija);

        resetirajIdPromijenjenog();
        ocistiUnos();
        prikaziStatus();

    }

    /**
     * Postavlja id entiteta na -1, čime se označuje da nije došlo do izmjene podataka
     */
    public void resetirajIdPromijenjenog() {
        idPromjenjenog = -1;
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
        textFields.forEach(UnosController::makniErrorIndicator);
    }

}
