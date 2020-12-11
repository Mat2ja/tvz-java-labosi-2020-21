package main.java.sample.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UnosBolestiController implements Initializable {

    public ChoiceBox<String> simptomiChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Simptom> listaSimptoma = UcitavanjePodataka.ucitajSimptome();
        for (Simptom simptom : listaSimptoma) {
            CheckBox cb = new CheckBox(simptom.getNaziv());
            simptomiChoiceBox.getItems().add(cb.getText());
        }
    }

    public void dodaj() {

    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }
}
