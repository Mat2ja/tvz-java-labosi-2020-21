package main.java.sample;

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
import main.java.hr.java.covidportal.model.Virus;

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
        tablicaVirusa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    public void pretrazi() {
        Main.getMainStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;
        });

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

    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
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
