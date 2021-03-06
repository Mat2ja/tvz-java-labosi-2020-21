package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.DohvatiSveSimptomeNit;
import main.java.hr.java.covidportal.niti.DohvatiSveZupanijeNit;
import main.java.hr.java.covidportal.niti.SpremiSimptomNit;
import main.java.hr.java.covidportal.niti.SpremiZupanijuNit;
import main.java.sample.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Kontroler unosa simptoma
 */
public class UnosSimptomaController extends UnosController implements Initializable {

    private List<Simptom> listaSimptoma;

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
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new DohvatiSveSimptomeNit());

        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        listaSimptoma = BazaPodataka.getSimptomi();

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

        inicijalizirajListenere();
    }


    /**
     * Dodaje novu osobu
     */
    @Override
    public void dodaj() {
        String naziv = toTitleCaseFirstWord(nazivSimptoma.getText());
        RadioButton vrijednosatRadioBtn = (RadioButton) vrijSimptomaGroup.getSelectedToggle();

        resetIndicators();

        Boolean valNaziv = validateField(nazivSimptoma, naziv);

        if (!valNaziv || vrijednosatRadioBtn == null) {
            prikaziErrorUnosAlert("Unos simptoma", "Unijeli ste simptom s nedozvoljenim vrijednostima.");
            return;
        }

        VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(vrijednosatRadioBtn.getUserData().toString());

        Simptom noviSimptom = new Simptom(naziv, vrijednost);

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new SpremiSimptomNit(noviSimptom));

        prikaziSuccessUnosAlert(
                "Unos simptoma", "Simptom dodan!", "Unijeli ste simptom: " + noviSimptom);

        ocistiUnos();
    }

    /**
     * Prikazuje status
     */
    @Override
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + listaSimptoma.size() + " simptoma");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    @Override
    public void ocistiUnos() {
        nazivSimptoma.clear();
        vrijSimptomaGroup.getSelectedToggle().setSelected(false);
        resetIndicators();

    }

    /**
     * Resetira error indikatore
     */
    @Override
    public void resetIndicators() {
        makniErrorIndicator(nazivSimptoma);
    }


    /**
     * Inicijalizira listenere
     */
    @Override
    public void inicijalizirajListenere() {
        nazivSimptoma.textProperty().addListener((obs, oldText, newText) -> validateField(nazivSimptoma, newText));
    }
}
