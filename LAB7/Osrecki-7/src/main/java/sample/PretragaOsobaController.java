package main.java.sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {

    private static ObservableList<Osoba> observableListOsoba;
    private static List<Osoba> listaOsoba;


    @FXML
    private TextField imePrezimeOsobe;

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
        observableListOsoba = FXCollections.observableArrayList(listaOsoba);

        tablicaOsoba.setItems(observableListOsoba);
    }

    public void pretrazi() {
        String imePrezime = imePrezimeOsobe.getText();

        List<Osoba> filtriraneOsobe = listaOsoba.stream()
                .filter(o ->(o.toString()).toLowerCase().contains(imePrezime.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuOsoba(filtriraneOsobe);
    }

    public void popuniObservableListuOsoba(List<Osoba> osobe) {
        observableListOsoba.clear();
        observableListOsoba.addAll(osobe);
    }


    public void natragNaPocetni() throws IOException {
        Parent pocetniEkran = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Main.getMainStage().setTitle("Covid Tester 9000");
        Main.getMainStage().setScene(new Scene(pocetniEkran, 600, 400));
        Main.getMainStage().show();
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
