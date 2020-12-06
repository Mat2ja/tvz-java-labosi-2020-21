package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.Simptom;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {


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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacVrijednostSimptoma.setCellValueFactory(new PropertyValueFactory<>("vrijednost"));

        listaSimptoma = CitanjePodataka.ucitajSimptome();
        observableListSimptoma = FXCollections.observableArrayList(listaSimptoma);

        tablicaSimptoma.setItems(observableListSimptoma);
        tablicaSimptoma.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void pretrazi() {
        Main.getMainStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;
        });

        String naziv = nazivSimptoma.getText();

        List<Simptom> filitriraniSimptomi = listaSimptoma.stream()
                .filter(simptom -> simptom.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuSimptoma(filitriraniSimptomi);
    }


    public void popuniObservableListuSimptoma(List<Simptom> simptomi) {
        observableListSimptoma.clear();
        observableListSimptoma.addAll(FXCollections.observableArrayList(simptomi));
    }

    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public static ObservableList<Simptom> getObservableListSimptoma() {
        return observableListSimptoma;
    }

    public static void setObservableListSimptoma(ObservableList<Simptom> observableList) {
        observableListSimptoma = observableList;
    }

    public static List<Simptom> getListaSimptoma() {
        return listaSimptoma;
    }

    public static void setListaSimptoma(List<Simptom> listaSimptoma) {
        PretragaSimptomaController.listaSimptoma = listaSimptoma;
    }
}
