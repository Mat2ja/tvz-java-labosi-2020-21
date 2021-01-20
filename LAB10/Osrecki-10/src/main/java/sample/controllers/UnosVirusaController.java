package main.java.sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatiSveBolestiNit;
import main.java.hr.java.covidportal.niti.SpremiBolestNit;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UnosVirusaController extends UnosController implements Initializable {

    private static ObservableList<CheckBox> listaCheckBoxa;
    private List<Simptom> listaSimptoma;
    private List<Virus> listaVirusa;

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

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new DohvatiSveBolestiNit());

        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        listaSimptoma = BazaPodataka.getSimptomi();
        listaVirusa = ucitajSamoViruse();

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

        inicijalizirajListenere();

    }

    /**
     * Dodaje novi virus
     */
    @Override
    public void dodaj() {
        String naziv = toTitleCaseFirstWord(nazivVirusa.getText());
        List<Simptom> odabraniSimptomi = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> listaSimptoma.stream()
                        .filter(simptom -> simptom.getId().equals(Long.parseLong(cb.getId()))).collect(Collectors.toList())
                        .get(0))
                .collect(Collectors.toList());


        Boolean valNaziv = validateField(nazivVirusa, naziv);
        Boolean valSimptomi = validateMenuButton(simptomiMenuBtn, odabraniSimptomi);

        if (!(valNaziv && valSimptomi)) {

            prikaziErrorUnosAlert("Unos virusa", "Unijeli ste virus s nedozvoljenim vrijednostima.");
            return;
        }

        Virus noviVirus = new Virus(naziv, odabraniSimptomi);

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new SpremiBolestNit(noviVirus));

        listaVirusa = ucitajSamoViruse();

        prikaziSuccessUnosAlert(
                "Unos virusa", "Virus dodan", "Unijeli ste virus: " + noviVirus);

        ocistiUnos();
    }

    /**
     * Ucitava samo viruse iz baze podataka (bez bolesti)
     *
     * @return lista virusa
     */
    private List<Virus> ucitajSamoViruse() {
        return BazaPodataka.getBolesti()
                .stream()
                .filter(Bolest::getJeVirus)
                .map(Virus.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * Prikazuje status
     */
    @Override
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + listaVirusa.size() + " virusa");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    @Override
    public void ocistiUnos() {
        nazivVirusa.clear();
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }

    /**
     * Resetira error indikatore
     */
    @Override
    public void resetIndicators() {
        makniErrorIndicator(nazivVirusa);
        makniErrorIndicator(simptomiMenuBtn);
    }


    /**
     * Inicijalizira listenere
     */
    @Override
    public void inicijalizirajListenere() {
        nazivVirusa.textProperty().addListener((obs, oldText, newText) -> validateField(nazivVirusa, newText));
    }

}
