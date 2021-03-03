package main.java.sample.controllers;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler pretrage županija
 */
public class PretragaZupanijaController extends PretragaController implements Initializable {

    private static ObservableList<Zupanija> observableListZupanija;
    private static List<Zupanija> listaZupanija;

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TableView<Zupanija> tablicaZupanija;
    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnikaZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojZarazenihZupanije;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacBrojStanovnikaZupanije.setCellValueFactory(new PropertyValueFactory<>("brojStanovnika"));
        stupacBrojZarazenihZupanije.setCellValueFactory(new PropertyValueFactory<>("brojZarazenih"));

        listaZupanija = BazaPodataka.dohvatiSveZupanije();


        if (observableListZupanija == null) {
            observableListZupanija = FXCollections.observableArrayList();
        }

        popuniObservableListuZupanija(listaZupanija);

        tablicaZupanija.setItems(observableListZupanija);
        tablicaZupanija.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    /**
     * Pretražuje županije prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    @Override
    public void pretrazi() {
        String naziv = nazivZupanije.getText();

        Predicate<Zupanija> predNaziv = zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv.toLowerCase());

        List<Zupanija> filitriraneZupanije = listaZupanija.stream()
                .filter(predNaziv)
                .collect(Collectors.toList());

        popuniObservableListuZupanija(filitriraneZupanije);
    }

    /**
     * Popunjuje observable listu listom svih učitanih županija
     *
     * @param zupanije podatak o listi županija
     */
    public void popuniObservableListuZupanija(List<Zupanija> zupanije) {
        observableListZupanija.clear();
        observableListZupanija.addAll(zupanije);
    }

}
