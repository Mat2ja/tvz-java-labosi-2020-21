package main.java.sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatiSveSimptomeNit;
import main.java.hr.java.covidportal.niti.DohvatiSveZupanijeNit;
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
 * Kontroler pretrage simptoma
 */
public class PretragaSimptomaController extends PretragaController implements Initializable {

    private static ObservableList<Simptom> observableListSimptoma;
    private List<Simptom> listaSimptoma;

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

        if (listaSimptoma == null) {
            listaSimptoma = new ArrayList<>();
        }

        ExecutorService executor = Executors.newCachedThreadPool();

        executor.execute(new DohvatiSveSimptomeNit());

        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        listaSimptoma = BazaPodataka.getSimptomi();


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
    @Override
    @FXML
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
