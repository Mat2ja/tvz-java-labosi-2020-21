package main.java.sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
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
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PretragaBolestiController implements Initializable {


    private static ObservableList<Bolest> observableListBolesti;
    private static List<Bolest> listaBolesti;

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;

    @FXML
    private TableColumn<Bolest, String> stupacSimptomiBolesti;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacSimptomiBolesti.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSimptomi().toString().replaceAll("[\\[\\]]", "")));

        listaBolesti = CitanjePodataka.ucitajBolesti();
        observableListBolesti = FXCollections.observableArrayList(listaBolesti);

        tablicaBolesti.setItems(observableListBolesti);
    }


    public void pretrazi() {
        String naziv = nazivBolesti.getText();

        List<Bolest> filitriraneBolesti = listaBolesti.stream()
                .filter(bolest -> bolest.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuBolesti(filitriraneBolesti);
    }

    public void popuniObservableListuBolesti(List<Bolest> bolesti) {
        observableListBolesti.clear();
        observableListBolesti.addAll(FXCollections.observableArrayList(bolesti));
    }


    public void natragNaPocetni() throws IOException {
        Parent pocetniEkran = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        Main.getMainStage().setTitle("Covid Tester 9000");
        Main.getMainStage().setScene(new Scene(pocetniEkran, 600, 400));
        Main.getMainStage().show();
    }

    public static ObservableList<Bolest> getObservableListBolesti() {
        return observableListBolesti;
    }

    public static void setObservableListBolesti(ObservableList<Bolest> observableList) {
        observableListBolesti = observableList;
    }

    public static List<Bolest> getListaBolesti() {
        return listaBolesti;
    }

    public static void setListaBolesti(List<Bolest> bolesti) {
        listaBolesti = bolesti;
    }
}
