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
import javafx.scene.input.KeyCode;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PretragaVirusaController implements Initializable {

    private static ObservableList<Virus> observableListVirusa;
    private static List<Virus> listaVirusa;

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

        listaVirusa = UcitavanjePodataka.ucitajViruse();

        if (observableListVirusa == null) {
            observableListVirusa = FXCollections.observableArrayList();
        }
        observableListVirusa.addAll(listaVirusa);

        tablicaVirusa.setItems(observableListVirusa);
        tablicaVirusa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Pretražuje viruse prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
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

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public static ObservableList<Virus> getObservableListVirusa() {
        return observableListVirusa;
    }

    public static void setObservableListVirusa(ObservableList<Virus> observableList) {
        observableListVirusa = observableList;
    }

    public static List<Virus> getListaVirusa() {
        return listaVirusa;
    }

    public static void setListaVirusa(List<Virus> virusi) {
        listaVirusa = virusi;
    }
}
