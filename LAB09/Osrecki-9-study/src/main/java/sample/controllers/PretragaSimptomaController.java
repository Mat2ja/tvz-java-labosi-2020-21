package main.java.sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Kontroler pretrage simptoma
 */
public class PretragaSimptomaController extends PretragaController implements Initializable {

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

    private ContextMenu contextMenu;
    private static ObservableList<MenuItem> listaOpcija;


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

        listaSimptoma = BazaPodataka.dohvatiSveSimptome();


        if (observableListSimptoma == null) {
            observableListSimptoma = FXCollections.observableArrayList();
        }

        popuniObservableListuSimptoma(listaSimptoma);

        tablicaSimptoma.setItems(observableListSimptoma);
        tablicaSimptoma.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        contextMenu = kreirajContextMenu();

        tablicaSimptoma.setRowFactory(t -> {
            TableRow<Simptom> row = new TableRow();
            row.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY && !(row.isEmpty())) {
                    prikaziContextMenu(e, tablicaSimptoma, contextMenu);

                    ObservableList<MenuItem> items = contextMenu.getItems();

                } else if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            });
            return row;
        });
    }

    /**
     * Pretražuje simptome prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    @Override
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

}
