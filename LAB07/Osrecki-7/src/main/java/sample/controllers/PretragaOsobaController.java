package main.java.sample.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.model.CitanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {

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
    private TableColumn<Osoba, Integer> stupacStarostOsobe;
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
        stupacStarostOsobe.setCellValueFactory(new PropertyValueFactory<>("starost"));
        stupacZupanijaOsobe.setCellValueFactory(new PropertyValueFactory<>("zupanija"));
        stupacBolestOsobe.setCellValueFactory(new PropertyValueFactory<>("zarazenBolescu"));
        stupacKontaktiOsobe.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getKontaktiraneOsobe().toString().replaceAll("[\\[\\]]", "")));

        listaOsoba = CitanjePodataka.ucitajOsobe();

        if (observableListOsoba == null) {
            observableListOsoba = FXCollections.observableArrayList();
        }
        observableListOsoba.addAll(listaOsoba);

        tablicaOsoba.setItems(observableListOsoba);
        tablicaOsoba.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Pretražuje osobe prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    public void pretrazi() {
        Main.getMainStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;
        });

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

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public static ObservableList<Osoba> getObservableListOsoba() {
        return observableListOsoba;
    }

    public static void setObservableListOsoba(ObservableList<Osoba> observableList) {
        observableListOsoba = observableList;
    }

    public static List<Osoba> getListaOsoba() {
        return listaOsoba;
    }

    public static void setListaOsoba(List<Osoba> osobe) {
        listaOsoba = osobe;
    }
}
