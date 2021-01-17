package main.java.sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatiSveBolestiNit;
import main.java.hr.java.covidportal.niti.DohvatiSveSimptomeNit;
import main.java.hr.java.covidportal.niti.SpremiBolestNit;
import main.java.hr.java.covidportal.niti.SpremiSimptomNit;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Kontroler unosa bolesti
 */
public class UnosBolestiController extends UnosController implements Initializable {

    private List<Simptom> listaSimptoma;
    private List<Bolest> listaBolesti;
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

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new DohvatiSveBolestiNit());

        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        listaSimptoma = BazaPodataka.getSimptomi();
        listaBolesti = ucitajSamoBolesti();

        if (listaCheckBoxa == null) {
            listaCheckBoxa = FXCollections.observableArrayList();
        }

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
    @Override
    public void dodaj() {
        String naziv = toTitleCaseFirstWord(nazivBolesti.getText());
        List<Simptom> odabraniSimptomi = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> listaSimptoma.stream()
                        .filter(simptom -> simptom.getId().equals(Long.parseLong(cb.getId())))
                        .findFirst()
                        .get())
                .collect(Collectors.toList());

        Boolean valNaziv = validateField(nazivBolesti, naziv);
        Boolean valSimptomi = validateMenuButton(simptomiMenuBtn, odabraniSimptomi);

        if (!(valNaziv && valSimptomi)) {

            prikaziErrorUnosAlert("Unos bolesti", "Unijeli ste bolest s nedozvoljenim vrijednostima.");
            return;
        }

        Bolest novaBolest = new Bolest(naziv, odabraniSimptomi);

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new SpremiBolestNit(novaBolest));

//        listaBolesti = ucitajSamoBolesti();


        prikaziSuccessUnosAlert(
                "Unos bolesti", "Bolest dodana", "Unijeli ste bolest: " + novaBolest);

        ocistiUnos();
//        prikaziStatus();
    }


    /**
     * Ucitava samo bolesti iz baze podataka (bez virusa)
     *
     * @return lista bolesti
     */
    private List<Bolest> ucitajSamoBolesti() {
        return BazaPodataka.getBolesti()
                .stream()
                .filter(bolest -> !bolest.getJeVirus())
                .collect(Collectors.toList());
    }

    /**
     * Prikazuje status
     */
    @Override
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + listaBolesti.size() + " bolesti");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    @Override
    public void ocistiUnos() {
        nazivBolesti.clear();
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }

    /**
     * Resetira error indikatore
     */
    @Override
    public void resetIndicators() {
        makniErrorIndicator(nazivBolesti);
        makniErrorIndicator(simptomiMenuBtn);
    }


    /**
     * Inicijalizira listenere
     */
    @Override
    public void inicijalizirajListenere() {
        nazivBolesti.textProperty().addListener((obs, oldText, newText) -> validateField(nazivBolesti, newText));
    }

}
