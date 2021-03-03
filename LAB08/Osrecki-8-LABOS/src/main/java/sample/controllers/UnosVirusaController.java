package main.java.sample.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.Bolest;
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
    private Integer indexPromijenjenog;

    @FXML
    private TextField nazivVirusa;
    @FXML
    private TextField opisVirusa;
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
       indexPromijenjenog = -1;

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


        nazivVirusa.textProperty().addListener((obs, oldText, newText) -> validateTextField(nazivVirusa, newText));
        opisVirusa.textProperty().addListener((obs, oldText, newText) -> validateTextField(opisVirusa, newText));
    }

    // zadatak 3
    public void izmijeniVirus(Virus virus){
        indexPromijenjenog = virus.getId().intValue() - 100;
        listaVirusa.remove(virus);

        nazivVirusa.setText(virus.getNaziv());
        opisVirusa.setText(virus.getOpis());
        List<Long> simptomiIds = virus.getSimptomi().stream()
                .mapToLong(Simptom::getId)
                .boxed()
                .collect(Collectors.toList());

        listaCheckBoxa.stream()
                .filter(cb -> simptomiIds.contains(Long.parseLong(cb.getId())))
                .forEach(cb -> cb.setSelected(true));
    }

    /**
     * Dodaje novi virus
     */
    public void dodaj() {
        String naziv = nazivVirusa.getText().toUpperCase();
        String opis = opisVirusa.getText();
        List<Simptom> odabraniSimptomi = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> UcitavanjePodataka.dohvatiSimptomPrekoId(listaSimptoma, Long.parseLong(cb.getId())))
                .collect(Collectors.toList());

        Boolean valNaziv = validateTextField(nazivVirusa, naziv);
        Boolean valOpis = validateTextField(opisVirusa, opis);
        Boolean valSimptomi = validateMenuButton(simptomiMenuBtn, odabraniSimptomi);

        if (!(valNaziv && valOpis && valSimptomi)) {
            Main.prikaziErrorUnosAlert("Unos virusa", "Unijeli ste virus s nedozvoljenim vrijednostima.");
            return;
        }

        Long id = ++brojVirusa + 100;
        if (indexPromijenjenog != -1) {
            id = indexPromijenjenog.longValue() + 100;
        }

        Virus noviVirus = new Virus(id, naziv,opis, odabraniSimptomi );
        UcitavanjePodataka.zapisiVirus(noviVirus);
        if (indexPromijenjenog != -1) {
            listaVirusa.add(indexPromijenjenog, noviVirus);
        } else {
            listaVirusa.add(noviVirus);
        }

        UcitavanjePodataka.obrisiSveViruse();
        for (Virus v : listaVirusa) {
            UcitavanjePodataka.zapisiVirusBezNovogReda(v);
        }

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

    /**
     * Prikazuje status
     */
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + brojVirusa + " virusa");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        nazivVirusa.clear();
        opisVirusa.clear();
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }


    public void resetIndicators() {
        Main.makniErrorIndicator(nazivVirusa);
        Main.makniErrorIndicator(opisVirusa);
        Main.makniErrorIndicator(simptomiMenuBtn);
    }

    public static boolean validateTextField(TextField tf, String value) {
        if (value.isBlank()) {
            Main.prikaziErrorIndicator(tf);
            return false;
        } else {
            Main.makniErrorIndicator(tf);
            return true;
        }
    }

    public static <T> boolean validateMenuButton(MenuButton mb, List<T> lista) {
        if (lista.isEmpty()) {
            Main.prikaziErrorIndicator(mb);
            return false;
        } else {
            Main.makniErrorIndicator(mb);
            return true;
        }
    }

}
