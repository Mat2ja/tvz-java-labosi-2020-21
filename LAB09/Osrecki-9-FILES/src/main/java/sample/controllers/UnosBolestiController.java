package main.java.sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Kontroler unosa bolesti
 */
public class UnosBolestiController extends UnosController implements Initializable {

    private static List<Simptom> listaSimptoma;
    private static List<Bolest> listaBolesti;
    private static Long brojBolesti;
    private static ObservableList<CheckBox> listaCheckBoxa;

    @FXML
    private TextField nazivBolesti;
    @FXML
    private MenuButton simptomiMenuBtn;
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
        listaBolesti = UcitavanjePodataka.ucitajBolesti();
        brojBolesti = (long) listaBolesti.size();
        listaCheckBoxa = FXCollections.observableArrayList();

        listaSimptoma.stream()
                .map(simptom -> {
                    CheckBox cb = new CheckBox(simptom.getNaziv());
                    cb.setId(simptom.getId().toString());
                    return cb;
                })
                .forEach(cb -> {
                    listaCheckBoxa.add(cb);
                    CustomMenuItem menuItem = new CustomMenuItem(cb);
                    menuItem.setHideOnClick(false);
                    simptomiMenuBtn.getItems().add(menuItem);
                });

        prikaziStatus();

        nazivBolesti.textProperty().addListener((obs, oldText, newText) -> validateTextField(nazivBolesti, newText));
    }

    /**
     * Dodaje novu bolest
     */
    public void dodaj() {
        String naziv = nazivBolesti.getText().toUpperCase();
        List<Simptom> odabraniSimptomi = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> UcitavanjePodataka.dohvatiSimptomPrekoId(listaSimptoma, Long.parseLong(cb.getId())))
                .collect(Collectors.toList());

        Boolean valNaziv = validateTextField(nazivBolesti, naziv);
        Boolean valSimptomi = validateMenuButton(simptomiMenuBtn, odabraniSimptomi);

        if (!(valNaziv && valSimptomi)) {

            prikaziErrorUnosAlert("Unos bolesti", "Unijeli ste bolest s nedozvoljenim vrijednostima.");
            return;
        }

        Long id = ++brojBolesti;
        Bolest novaBolest = new Bolest(id, naziv, odabraniSimptomi);
        UcitavanjePodataka.zapisiBolest(novaBolest);
        listaBolesti.add(novaBolest);

        prikaziSuccessUnosAlert(
                "Unos bolesti", "Bolest dodana", "Unijeli ste bolest: " + novaBolest);

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
        status.setText("U sustavu je trenutno " + brojBolesti + " bolesti");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        nazivBolesti.clear();
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }

    public void resetIndicators() {
        makniErrorIndicator(nazivBolesti);
        makniErrorIndicator(simptomiMenuBtn);
    }

}
