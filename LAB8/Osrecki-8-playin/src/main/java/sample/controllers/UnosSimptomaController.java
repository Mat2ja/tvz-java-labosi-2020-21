package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler unosa simptoma
 */
public class UnosSimptomaController implements Initializable {

    private static List<Simptom> listaSimptoma;
    private static Long brojSimptoma;

    @FXML
    private TextField nazivSimptoma;

    @FXML
    public ToggleGroup vrijSimptomaGroup;
    @FXML
    public RadioButton vrijRijetko;
    @FXML
    public RadioButton vrijSrednje;
    @FXML
    public RadioButton vrijCesto;

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

        listaSimptoma = UcitavanjePodataka.ucitajSimptome();
        brojSimptoma = (long) listaSimptoma.size();

        vrijRijetko.setToggleGroup(vrijSimptomaGroup);
        vrijSrednje.setToggleGroup(vrijSimptomaGroup);
        vrijCesto.setToggleGroup(vrijSimptomaGroup);

        prikaziStatus();

    }

    /**
     * Dodaje novu osobu
     */
    public void dodaj() {
        String naziv = nazivSimptoma.getText().toUpperCase();
        RadioButton vrijednosatRadioBtn = (RadioButton) vrijSimptomaGroup.getSelectedToggle();

        resetIndicators();

        if (naziv.isBlank() || vrijednosatRadioBtn == null) {
            if (naziv.isBlank()) Main.prikaziErrorIndicator(nazivSimptoma);

            Main.prikaziErrorUnosAlert("Unos simptoma", "Unijeli ste simptom s nedozvoljenim vrijednostima.");
            return;
        }


        VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(vrijednosatRadioBtn.getUserData().toString());

        Long id = ++brojSimptoma;
        Simptom noviSimptom = new Simptom(id, naziv, vrijednost);
        UcitavanjePodataka.zapisiSimptom(noviSimptom);
        listaSimptoma.add(noviSimptom);

        Main.prikaziSuccessUnosAlert(
                "Unos simptoma", "Simptom dodan!", "Unijeli ste simptom: " + noviSimptom);

        prikaziStatus();
        ocistiUnos();
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + brojSimptoma + " simptoma");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        nazivSimptoma.clear();
        vrijSimptomaGroup.getSelectedToggle().setSelected(false);
        resetIndicators();

    }

    public void resetIndicators() {
        Main.makniErrorIndicator(nazivSimptoma);
    }
}
