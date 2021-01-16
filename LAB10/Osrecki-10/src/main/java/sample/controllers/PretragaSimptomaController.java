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
    private List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TableView<Simptom> tablicaSimptoma;
    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;
    @FXML
    private TableColumn<Simptom, VrijednostSimptoma> stupacVrijednostSimptoma;

    private ContextMenu contextMenu;


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
                if (row.isEmpty()) return;

                Simptom simptom = row.getItem();

                if (e.getButton() == MouseButton.SECONDARY) {
                    prikaziContextMenu(e, tablicaSimptoma, contextMenu);
                    postaviContextMenuListenere(contextMenu, simptom);
                } else if (e.getClickCount() == 2) {
                    prikaziBolestiSaSimptomom(simptom);
                } else if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            });

            return row;
        });
    }

    /**
     * Prikazuje bolesti koje sadrže odabrani simptom
     *
     * @param simptom podatak o odabranom simptomu
     */
    private void prikaziBolestiSaSimptomom(Simptom simptom) {
        List<Bolest> bolesti = BazaPodataka.dohvatiBolestiSaSimptomom(simptom.getId());

        String content = "";

        if (bolesti.isEmpty()) {
            content = "Nema bolesti sa tim simptomom";
        } else {
            content = bolesti.toString().replaceAll("[\\[\\]]", "");
        }

        UnosController.prikaziAlert("Bolesti",
                "Bolesti sa simptomom: " + simptom.getNaziv(),
                content,
                Alert.AlertType.INFORMATION);
    }

    /**
     * Postavlja ponašanje na klik određene opcije kontekst menija
     *
     * @param contextMenu ContextMenu
     * @param simptom     odabarani simptom
     */
    private void postaviContextMenuListenere(ContextMenu contextMenu, Simptom simptom) {
        List<MenuItem> items = contextMenu.getItems();
        items.get(0).setOnAction(event -> makniSimptom(simptom));
    }

    /**
     * Miče simptom iz baze podataka i osvježava prikaz
     *
     * @param simptom odabrani simptom
     */
    private void makniSimptom(Simptom simptom) {
        BazaPodataka.izbrisiSimptom(simptom.getId());
        listaSimptoma = BazaPodataka.dohvatiSveSimptome();
        popuniObservableListuSimptoma(listaSimptoma);
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
