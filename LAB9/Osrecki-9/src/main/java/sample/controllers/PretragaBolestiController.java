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
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler pretrage bolesti
 */
public class PretragaBolestiController implements Initializable {

    private static ObservableList<Bolest> observableListBolesti;
    private static List<Bolest> listaBolesti;

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Bolest> tablicaBolesti;
    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;
    @FXML
    private TableColumn<Bolest, String> stupacSimptomiBolesti;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacSimptomiBolesti.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSimptomi().toString().replaceAll("[\\[\\]]", "")));

        listaBolesti = UcitavanjePodataka.ucitajBolesti();

        if (observableListBolesti == null) {
            observableListBolesti = FXCollections.observableArrayList();
        }

        popuniObservableListuBolesti(listaBolesti);

        tablicaBolesti.setItems(observableListBolesti);
        tablicaBolesti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    /**
     * Pretražuje bolesti prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    public void pretrazi() {
        String naziv = nazivBolesti.getText();

        Predicate<Bolest> predNaziv = bolest -> bolest.getNaziv().toLowerCase().contains(naziv.toLowerCase());

        List<Bolest> filitriraneBolesti = listaBolesti.stream()
                .filter(predNaziv)
                .collect(Collectors.toList());

        popuniObservableListuBolesti(filitriraneBolesti);
    }

    /**
     * Popunjuje observable listu listom svih učitanih bolesti
     *
     * @param bolesti podatak o listi bolesti
     */
    public void popuniObservableListuBolesti(List<Bolest> bolesti) {
        observableListBolesti.clear();
        observableListBolesti.addAll(bolesti);
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

}
