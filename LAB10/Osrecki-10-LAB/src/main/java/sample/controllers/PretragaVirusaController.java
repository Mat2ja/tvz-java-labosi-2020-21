package main.java.sample.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.niti.DohvatiSveBolestiNit;
import main.java.sample.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler pretrage virusa
 */
public class PretragaVirusaController extends PretragaController implements Initializable {

    private static ObservableList<Virus> observableListVirusa;
    private List<Virus> listaVirusa;

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Virus> tablicaVirusa;
    @FXML
    private TableColumn<Virus, String> stupacNazivVirusa;
    @FXML
    private TableColumn<Virus, String> stupacSimptomiVirusa;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacSimptomiVirusa.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSimptomi().toString().replaceAll("[\\[\\]]", "")));


        if (listaVirusa == null) {
            listaVirusa = new ArrayList<>();
        }

        ExecutorService executor = Executors.newCachedThreadPool();

        executor.execute(new DohvatiSveBolestiNit());

        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        listaVirusa = BazaPodataka.getBolesti()
                .stream()
                .filter(Bolest::getJeVirus)
                .map(Virus.class::cast)
                .collect(Collectors.toList());


        if (observableListVirusa == null) {
            observableListVirusa = FXCollections.observableArrayList();
        }

        popuniObservableListuVirusa(listaVirusa);


        tablicaVirusa.setItems(observableListVirusa);
        tablicaVirusa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Pretražuje viruse prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    @Override
    @FXML
    public void pretrazi() {
        String naziv = nazivVirusa.getText();

        Predicate<Virus> predNaziv = virus -> virus.getNaziv().toLowerCase().contains(naziv.toLowerCase());

        List<Virus> filtriraniVirusi = listaVirusa.stream()
                .filter(predNaziv)
                .collect(Collectors.toList());

        popuniObservableListuVirusa(filtriraniVirusi);
    }

    /**
     * Popunjuje observable listu listom svih učitanih virusa
     *
     * @param virusi podatak o listi virusa
     */
    public void popuniObservableListuVirusa(List<Virus> virusi) {
        observableListVirusa.clear();
        observableListVirusa.addAll(virusi);
    }

}
