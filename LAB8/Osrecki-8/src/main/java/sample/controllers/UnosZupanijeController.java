package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.model.UcitavanjePodataka;
import main.java.hr.java.covidportal.model.Utility;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UnosZupanijeController implements Initializable {

    private static List<Zupanija> listaZupanija;
    private static Long brojZupanija;

    @FXML
    private TextField nazivZupanije;
    @FXML
    private TextField brStanovnikaZupanije;
    @FXML
    private TextField brZarazenihZupanije;
    @FXML
    private Label status;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaZupanija = UcitavanjePodataka.ucitajZupanije();
        brojZupanija = Long.valueOf(listaZupanija.size());

        prikaziStatus();
    }

    public void dodaj() {
        String naziv = nazivZupanije.getText().toUpperCase();
        Integer brStanovnika = Utility.ucitajBroj(brStanovnikaZupanije.getText());
        Integer brZarazenih = Utility.ucitajBroj(brZarazenihZupanije.getText());

        if (naziv.isBlank() || brStanovnika == null || brZarazenih == null) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Unos županije");
            errorAlert.setHeaderText("Pogrešan unos!");
            errorAlert.setContentText("Unijeli ste županiju s nedozvoljenim vrijednostima.");
            errorAlert.showAndWait();
            return;
        }

        Long id = ++brojZupanija;
        Zupanija novaZupanija = new Zupanija(id, naziv, brStanovnika, brZarazenih);
        UcitavanjePodataka.zapisiZupaniju(novaZupanija);
        listaZupanija.add(novaZupanija);


        Alert successAlert = new Alert(AlertType.INFORMATION);
        successAlert.setTitle("Unos županije");
        successAlert.setHeaderText("Županija dodana!");
        successAlert.setContentText("Unijeli ste županiju: " + novaZupanija);
        successAlert.showAndWait();

        ocistiUnos();
        prikaziStatus();
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public void prikaziStatus(){
        status.setText("U sustavu je trenutno " + brojZupanija + " županija");
    }

    public void ocistiUnos(){
        nazivZupanije.clear();
        brStanovnikaZupanije.clear();
        brZarazenihZupanije.clear();
    }
}
