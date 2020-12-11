package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.hr.java.covidportal.model.Utility;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UnosSimptomaController implements Initializable {

    private static List<Simptom> listaSimptoma;
    private static Long brojSimptoma;

    @FXML
    private TextField nazivSimptoma;

    @FXML
    ToggleGroup vrijSimptomaGroup;
    @FXML
    public RadioButton vrijRijetko;
    @FXML
    public RadioButton vrijSrednje;
    @FXML
    public RadioButton vrijCesto;

    @FXML
    private Label status;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaSimptoma = UcitavanjePodataka.ucitajSimptome();
        brojSimptoma = Long.valueOf(listaSimptoma.size());

        vrijRijetko.setToggleGroup(vrijSimptomaGroup);
        vrijSrednje.setToggleGroup(vrijSimptomaGroup);
        vrijCesto.setToggleGroup(vrijSimptomaGroup);

        prikaziStatus();

    }

    public void dodaj() {
        String naziv = nazivSimptoma.getText().toUpperCase();
        RadioButton vrijednosatRadioBtn = (RadioButton) vrijSimptomaGroup.getSelectedToggle();


        if (naziv.isBlank() || vrijednosatRadioBtn == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Unos simptoma");
            errorAlert.setHeaderText("Pogrešan unos!");
            errorAlert.setContentText("Unijeli ste simptom s nedozvoljenim vrijednostima.");
            errorAlert.showAndWait();
            return;
        }

        VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(vrijednosatRadioBtn.getUserData().toString());

        Long id = ++brojSimptoma;
        Simptom noviSimptom = new Simptom(id, naziv, vrijednost);
        UcitavanjePodataka.zapisiSimptom(noviSimptom);
        listaSimptoma.add(noviSimptom);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Unos simptoma");
        successAlert.setHeaderText("Simptom dodan!");
        successAlert.setContentText("Unijeli ste simptom: " + noviSimptom);
        successAlert.showAndWait();

        prikaziStatus();
        ocistiUnos();
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + brojSimptoma + " simptoma");
    }

    public void ocistiUnos() {
        nazivSimptoma.clear();
        vrijSimptomaGroup.getSelectedToggle().setSelected(false);
    }
}
