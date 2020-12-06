package main.java.sample;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacBrojStanovnikaZupanije.setCellValueFactory(new PropertyValueFactory<>("brojStanovnika"));
        stupacBrojZarazenihZupanije.setCellValueFactory(new PropertyValueFactory<>("brojZarazenih"));

        listaZupanija = CitanjePodataka.ucitajZupanije();
        observableListZupanija = FXCollections.observableArrayList(listaZupanija);

        tablicaZupanija.setItems(observableListZupanija);
    }

    public void pretrazi() {
        String naziv = nazivZupanije.getText();

        List<Zupanija> filitriraneZupanije = listaZupanija.stream()
                .filter(zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuZupanija(filitriraneZupanije);
    }

    public void natragNaPocetni() throws IOException {
        Parent pocetniEkran = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Main.getMainStage().setTitle("Covid Tester 9000");
        Main.getMainStage().setScene(new Scene(pocetniEkran, 600, 400));
        Main.getMainStage().show();
    }

    public void popuniObservableListuZupanija(List<Zupanija> zupanije) {
        observableListZupanija.clear();
        observableListZupanija.addAll(FXCollections.observableArrayList(zupanije));
    }

    public static ObservableList<Zupanija> getObservableListZupanija() {
        return observableListZupanija;
    }

    public static void setObservableListZupanija(ObservableList<Zupanija> observableList) {
        observableListZupanija = observableList;
    }

    public static List<Zupanija> getListaZupanija() {
        return listaZupanija;
    }

    public static void setListaZupanija(List<Zupanija> zupanije) {
        listaZupanija = zupanije;
    }
}
