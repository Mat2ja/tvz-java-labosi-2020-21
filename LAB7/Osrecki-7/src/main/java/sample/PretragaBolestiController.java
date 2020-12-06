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
import main.java.hr.java.covidportal.model.Bolest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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

        if (observableListBolesti == null) {
            observableListBolesti = FXCollections.observableArrayList();
        }
        observableListBolesti.addAll(listaBolesti);

        tablicaBolesti.setItems(observableListBolesti);
        tablicaBolesti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    /**
     * Pretražuje bolesti prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    public void pretrazi() {
        Main.getMainStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;
        });

        String naziv = nazivBolesti.getText();

        List<Bolest> filitriraneBolesti = listaBolesti.stream()
                .filter(bolest -> bolest.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuBolesti(filitriraneBolesti);
    }

    /**
     * Popunjuje observable listu listom svih učitanih bolesti
     *
     * @param bolesti podatak o listi bolesti
     */
    public void popuniObservableListuBolesti(List<Bolest> bolesti) {
        observableListBolesti.clear();
        observableListBolesti.addAll(FXCollections.observableArrayList(bolesti));
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
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
