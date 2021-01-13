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
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontroler pretrage simptoma
 */
public class PretragaOsobaController extends PretragaController implements Initializable {

    private static ObservableList<Osoba> observableListOsoba;
    private static List<Osoba> listaOsoba;

    @FXML
    private TextField imeOsobe;
    @FXML
    private TextField prezimeOsobe;

    @FXML
    private TableView<Osoba> tablicaOsoba;
    @FXML
    private TableColumn<Osoba, String> stupacImeOsobe;
    @FXML
    private TableColumn<Osoba, String> stupacPrezimeOsobe;
    @FXML
    private TableColumn<Osoba, Integer> stupacStarost;
    @FXML
    private TableColumn<Osoba, Zupanija> stupacZupanijaOsobe;
    @FXML
    private TableColumn<Osoba, Bolest> stupacBolestOsobe;
    @FXML
    private TableColumn<Osoba, String> stupacKontaktiOsobe;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacImeOsobe.setCellValueFactory(new PropertyValueFactory<>("ime"));
        stupacPrezimeOsobe.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        stupacStarost.setCellValueFactory(new PropertyValueFactory<>("starost"));
        stupacZupanijaOsobe.setCellValueFactory(new PropertyValueFactory<>("zupanija"));
        stupacBolestOsobe.setCellValueFactory(new PropertyValueFactory<>("zarazenBolescu"));
        stupacKontaktiOsobe.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getKontaktiraneOsobe().toString().replaceAll("[\\[\\]]", "")));

        listaOsoba = BazaPodataka.dohvatiSveOsobe();

        if (observableListOsoba == null) {
            observableListOsoba = FXCollections.observableArrayList();
        }

        popuniObservableListuOsoba(listaOsoba);

        tablicaOsoba.setItems(observableListOsoba);
        tablicaOsoba.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Pretražuje osobe prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    @Override
    public void pretrazi() {
        String ime = imeOsobe.getText();
        String prezime = prezimeOsobe.getText();

        Predicate<Osoba> predIme = osoba -> osoba.getIme().toLowerCase().contains(ime.toLowerCase());
        Predicate<Osoba> predPrezime = osoba -> osoba.getPrezime().toLowerCase().contains(prezime.toLowerCase());

        List<Osoba> filtriraneOsobe = listaOsoba.stream()
                .filter(predIme)
                .filter(predPrezime)
                .collect(Collectors.toList());

        popuniObservableListuOsoba(filtriraneOsobe);

    }

    /**
     * Popunjuje observable listu listom svih učitanih osoba
     *
     * @param osobe podatak o listi osoba
     */
    public void popuniObservableListuOsoba(List<Osoba> osobe) {
        observableListOsoba.clear();
        observableListOsoba.addAll(osobe);
    }


}
