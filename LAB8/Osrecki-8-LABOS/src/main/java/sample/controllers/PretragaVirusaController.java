package main.java.sample.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler pretrage virusa
 */
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
    private TableColumn<Virus, String> stupacOpisVirusa;
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
        stupacOpisVirusa.setCellValueFactory(new PropertyValueFactory<>("opis"));
        stupacSimptomiVirusa.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSimptomi().toString().replaceAll("[\\[\\]]", "")));

        listaVirusa = UcitavanjePodataka.ucitajViruse();

        if (observableListVirusa == null) {
            observableListVirusa = FXCollections.observableArrayList();
        }

        popuniObservableListuVirusa(listaVirusa);


        tablicaVirusa.setItems(observableListVirusa);
        tablicaVirusa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablicaVirusa.setRowFactory(t -> {
            TableRow<Virus> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !(row.isEmpty())) {
                    Virus virus = row.getItem();
                    prikaziEkranPromjeneVirusa(virus);
                }
            });
            return row;
        });
    }

    // zadatak 3
    public void prikaziEkranPromjeneVirusa(Virus virus){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("unosVirusa.fxml"));
            Scene scene = new Scene(loader.load());

            Main.getMainStage().setTitle("Unos virusa");
            Main.getMainStage().setScene(scene);

            UnosVirusaController controller = loader.getController();
            controller.izmijeniVirus(virus);

        } catch (IOException e) {
            Main.logger.error("Greška kod prikaza ekrana 'Unos virusa'" );
            e.printStackTrace();
        }
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

}
