package main.java.sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Kontroler pretrage simptoma
 */
public class PretragaSimptomaController implements Initializable {

    private static ObservableList<Simptom> observableListSimptoma;
    private static List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TableView<Simptom> tablicaSimptoma;
    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;
    @FXML
    private TableColumn<Simptom, VrijednostSimptoma> stupacVrijednostSimptoma;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacVrijednostSimptoma.setCellValueFactory(new PropertyValueFactory<>("vrijednost"));

        try {
            listaSimptoma = BazaPodataka.dohvatiSveSimptome();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        if (observableListSimptoma == null) {
            observableListSimptoma = FXCollections.observableArrayList();
        }

        popuniObservableListuSimptoma(listaSimptoma);

        tablicaSimptoma.setItems(observableListSimptoma);
        tablicaSimptoma.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Pretražuje simptome prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    public void pretrazi() {
        String naziv = nazivSimptoma.getText();

        Predicate<Simptom> predNaziv = simptom -> simptom.getNaziv().toLowerCase().contains(naziv.toLowerCase());
        List<Simptom> filitriraniSimptomi = listaSimptoma.stream()
                .filter(predNaziv)
                .collect(Collectors.toList());

        popuniObservableListuSimptoma(filitriraniSimptomi);
    }

    /**
     * Popunjuje observable listu listom svih učitanih simptoma
     *
     * @param simptomi podatak o listi simptoma
     */
    public void popuniObservableListuSimptoma(List<Simptom> simptomi) {
        observableListSimptoma.clear();
        observableListSimptoma.addAll(simptomi);
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

}
