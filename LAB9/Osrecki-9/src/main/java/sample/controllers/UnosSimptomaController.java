package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * Kontroler unosa simptoma
 */
public class UnosSimptomaController extends UnosController implements Initializable {

    private static List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivSimptoma;

    public ToggleGroup vrijSimptomaGroup;

    @FXML
    public GridPane vrijednostiGrid;

    @FXML
    private Label status;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            listaSimptoma = BazaPodataka.dohvatiSveSimptome();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        vrijSimptomaGroup = new ToggleGroup();

        List<RadioButton> listaRb = new ArrayList<>();
        Arrays.stream(VrijednostSimptoma.values())
                .forEach(vrijednost -> {
                    RadioButton rb = new RadioButton();
                    rb.setToggleGroup(vrijSimptomaGroup);
                    rb.setText(vrijednost.getVrijednost());
                    rb.setUserData(vrijednost);
                    listaRb.add(rb);
                }
        );

        Integer numCols = VrijednostSimptoma.values().length;
        IntStream.range(0, numCols).forEach(col -> {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            vrijednostiGrid.getColumnConstraints().add(colConst);

            vrijednostiGrid.add(listaRb.get(col), col, 0);
        });

        prikaziStatus();

        inicijalizirajListenere();
    }


    /**
     * Dodaje novu osobu
     */
    public void dodaj() {
        String naziv = toTitleCase(nazivSimptoma.getText(), " ");
        RadioButton vrijednosatRadioBtn = (RadioButton) vrijSimptomaGroup.getSelectedToggle();

        resetIndicators();

        Boolean valNaziv = validateTextField(nazivSimptoma, naziv);

        if (!valNaziv || vrijednosatRadioBtn == null) {
            prikaziErrorUnosAlert("Unos simptoma", "Unijeli ste simptom s nedozvoljenim vrijednostima.");
            return;
        }

        VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(vrijednosatRadioBtn.getUserData().toString());

        Simptom noviSimptom = new Simptom(naziv, vrijednost);

        try {
            BazaPodataka.spremiNoviSimptom(noviSimptom);
        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod spremanja novog simptoma");
            e.printStackTrace();
        }

        prikaziSuccessUnosAlert(
                "Unos simptoma", "Simptom dodan!", "Unijeli ste simptom: " + noviSimptom);

        prikaziStatus();
        ocistiUnos();
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    /**
     * Prikazuje status
     */
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + listaSimptoma.size() + " simptoma");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        nazivSimptoma.clear();
        vrijSimptomaGroup.getSelectedToggle().setSelected(false);
        resetIndicators();

    }

    public void resetIndicators() {
        makniErrorIndicator(nazivSimptoma);
    }

    private void inicijalizirajListenere() {
        nazivSimptoma.textProperty().addListener((obs, oldText, newText) -> validateTextField(nazivSimptoma, newText));
    }
}
