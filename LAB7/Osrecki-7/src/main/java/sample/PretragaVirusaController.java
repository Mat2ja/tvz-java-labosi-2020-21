package main.java.sample;

import javafx.beans.property.SimpleStringProperty;
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
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Virus;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacSimptomiVirusa.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSimptomi().toString().replaceAll("[\\[\\]]", "")));

        listaVirusa = CitanjePodataka.ucitajViruse();
        observableListVirusa = FXCollections.observableArrayList(listaVirusa);

        tablicaVirusa.setItems(observableListVirusa);
    }


    public void pretrazi() {
        String naziv = nazivVirusa.getText();

        List<Virus> filtriraniVirusi = listaVirusa.stream()
                .filter(virus -> virus.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuVirusa(filtriraniVirusi);
    }

    public void popuniObservableListuVirusa(List<Virus> virusi) {
        observableListVirusa.clear();
        observableListVirusa.addAll(FXCollections.observableArrayList(virusi));
    }


    public void natragNaPocetni() throws IOException {
        Parent pocetniEkran = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Main.getMainStage().setTitle("Covid Tester 9000");
        Main.getMainStage().setScene(new Scene(pocetniEkran, 600, 400));
        Main.getMainStage().show();
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
