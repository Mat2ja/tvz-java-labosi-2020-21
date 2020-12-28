package main.java.sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Kontroler unosa bolesti
 */
public class UnosBolestiController extends UnosController implements Initializable {

    private static List<Simptom> listaSimptoma;
    private static List<Bolest> listaBolesti;
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

        try {
            listaSimptoma = BazaPodataka.dohvatiSveSimptome();
            listaBolesti = BazaPodataka.dohvatiSveBolesti()
                    .stream()
                    .filter(bolest -> !bolest.getJeVirus())
                    .collect(Collectors.toList());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

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

        inicijalizirajListenere();

    }


    /**
     * Dodaje novu bolest
     */
    public void dodaj() {
        String naziv = toTitleCase(nazivBolesti.getText().toUpperCase()," ");
        List<Simptom> odabraniSimptomi = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> listaSimptoma.stream()
                        .filter(simptom -> simptom.getId().equals(Long.parseLong(cb.getId()))).collect(Collectors.toList())
                        .get(0))
                .collect(Collectors.toList());

        Boolean valNaziv = validateField(nazivBolesti, naziv);
        Boolean valSimptomi = validateMenuButton(simptomiMenuBtn, odabraniSimptomi);

        if (!(valNaziv && valSimptomi)) {

            prikaziErrorUnosAlert("Unos bolesti", "Unijeli ste bolest s nedozvoljenim vrijednostima.");
            return;
        }

        Bolest novaBolest = new Bolest(naziv, odabraniSimptomi);
        try {
            BazaPodataka.spremiNovuBolest(novaBolest);
        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod spremanja nove bolesti");
            e.printStackTrace();
        }

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
        status.setText("U sustavu je trenutno " + listaBolesti.size() + " bolesti");
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


    private void inicijalizirajListenere() {
        nazivBolesti.textProperty().addListener((obs, oldText, newText) -> validateField(nazivBolesti, newText));
    }

}
