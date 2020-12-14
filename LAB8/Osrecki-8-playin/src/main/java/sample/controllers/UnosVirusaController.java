package main.java.sample.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UnosVirusaController implements Initializable {

    private static List<Simptom> listaSimptoma;
    private static List<Virus> listaVirusa;
    private static Long brojVirusa;
    private static List<CheckBox> listaCheckBoxa = new ArrayList<>();

    @FXML
    private TextField nazivVirusa;
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
        listaVirusa = UcitavanjePodataka.ucitajViruse();
        brojVirusa = (long) listaVirusa.size();

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
    }

    /**
     * Dodaje novi virus
     */
    public void dodaj() {
        String naziv = nazivVirusa.getText().toUpperCase();
        List<Simptom> odabraniSimptomi = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> UcitavanjePodataka.dohvatiSimptomPrekoId(listaSimptoma, Long.parseLong(cb.getId())))
                .collect(Collectors.toList());

        resetIndicators();

        if (naziv.isBlank() || odabraniSimptomi.isEmpty()) {
            if (naziv.isBlank()) Main.prikaziErrorIndicator(nazivVirusa);
            if (odabraniSimptomi.isEmpty()) Main.prikaziErrorIndicator(simptomiMenuBtn);

            Main.prikaziErrorUnosAlert("Unos virusa", "Unijeli ste virus s nedozvoljenim vrijednostima.");
            return;
        }

        Long id = ++brojVirusa + 100;
        Virus noviVirus = new Virus(id, naziv, odabraniSimptomi);
        UcitavanjePodataka.zapisiVirus(noviVirus);
        listaVirusa.add(noviVirus);

        Main.prikaziSuccessUnosAlert(
                "Unos virusa", "Virus dodan", "Unijeli ste virus: " + noviVirus);

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
        status.setText("U sustavu je trenutno " + brojVirusa + " virusa");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        nazivVirusa.clear();
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }

    public void resetIndicators() {
        Main.makniErrorIndicator(nazivVirusa);
        Main.makniErrorIndicator(simptomiMenuBtn);
    }
}
