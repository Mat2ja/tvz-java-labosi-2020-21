package main.java.sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**
 * Kontroler unosa osoba
 */
public class UnosOsobeController extends UnosController implements Initializable {

    private static List<Zupanija> listaZupanija;
    private static List<Bolest> listaSvihBolesti;
    private static List<Osoba> listaOsoba;

    private static ObservableList<CheckBox> listaCheckBoxa;

    @FXML
    public TextField imeOsobe;
    @FXML
    public TextField prezimeOsobe;
    @FXML
    public DatePicker datumRodjenjaOsobe;
    @FXML
    public ChoiceBox<Zupanija> zupanijaOsobe;
    @FXML
    public ChoiceBox<Bolest> bolestOsobe;
    @FXML
    public MenuButton kontaktiOsobeMenuBtn;

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

        listaZupanija = BazaPodataka.dohvatiSveZupanije();
        listaSvihBolesti = BazaPodataka.dohvatiSveBolesti();
        listaOsoba = BazaPodataka.dohvatiSveOsobe();

        zupanijaOsobe.getItems().addAll(listaZupanija);
        bolestOsobe.getItems().addAll(listaSvihBolesti);
        listaCheckBoxa = FXCollections.observableArrayList();

        listaOsoba.stream()
                .map(osoba -> {
                    CheckBox cb = new CheckBox(osoba.toString());
                    cb.setId(osoba.getId().toString());
                    return cb;
                })
                .forEach(cb -> {
                    listaCheckBoxa.add(cb);
                    CustomMenuItem menuItem = new CustomMenuItem(cb);
                    menuItem.setHideOnClick(false);
                    kontaktiOsobeMenuBtn.getItems().add(menuItem);
                });

        prikaziStatus();
        inicijalizirajListenere();
    }


    /**
     * Dodaje novu osobu
     */
    @Override
    public void dodaj() {
        String ime = toTitleCase(imeOsobe.getText(), " ");
        String prezime = toTitleCase(prezimeOsobe.getText(), " ");
        String datumRodjenjaString = datumRodjenjaOsobe.getEditor().getText();
        Zupanija zupanija = zupanijaOsobe.getValue();
        Bolest bolest = bolestOsobe.getValue();
        List<Osoba> kontakti = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> listaOsoba.stream()
                        .filter(osoba -> osoba.getId().equals(Long.parseLong(cb.getId())))
                        .findFirst()
                        .get())
                .collect(Collectors.toList());

        Boolean valIme = validateField(imeOsobe, ime);
        Boolean valPrezime = validateField(prezimeOsobe, prezime);
        Boolean valStarost = validateDatePicker(datumRodjenjaOsobe, datumRodjenjaString);
        Boolean valZupanija = validateChoiceBox(zupanijaOsobe, zupanija);
        Boolean valBolest = validateChoiceBox(bolestOsobe, bolest);
        Boolean valKontakti = validateMenuButton(kontaktiOsobeMenuBtn, kontakti);

        if (!(valIme && valPrezime && valStarost && valZupanija && valBolest && valKontakti)) {
            prikaziErrorUnosAlert("Unos osobe", "Unijeli ste osobu s nedozvoljenim vrijednostima.");
            return;
        }

        LocalDate datumRodjenja;
        try {
            datumRodjenja = LocalDate.parse(datumRodjenjaString, DateTimeFormatter.ofPattern("d/MM/yyyy"));
        } catch (DateTimeParseException e) {
            prikaziErrorUnosAlert("Unos osobe", "Unijeli ste osobu s nedozvoljenim vrijednostima.");
            return;
        }

        Osoba novaOsoba = new Osoba.Builder()
                .hasIme(ime)
                .hasPrezime(prezime)
                .isBornAt(datumRodjenja)
                .atZupanija(zupanija)
                .withBolest(bolest)
                .withKontaktiraneOsobe(kontakti)
                .build();
        
        BazaPodataka.spremiNovuOsobu(novaOsoba);

        listaOsoba = BazaPodataka.dohvatiSveOsobe();

        prikaziSuccessUnosAlert(
                "Unos osobe", "Osoba dodana!", "Unijeli ste osobu: " + novaOsoba);

        prikaziStatus();
        ocistiUnos();
    }

    /**
     * Prikazuje status
     */
    @Override
    public void prikaziStatus() {
        status.setText("U sustavu je trenutno " + listaOsoba.size() + " osoba");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    @Override
    public void ocistiUnos() {
        imeOsobe.clear();
        prezimeOsobe.clear();
        datumRodjenjaOsobe.getEditor().clear();
        zupanijaOsobe.getItems().clear();
        zupanijaOsobe.getItems().addAll(listaZupanija);
        bolestOsobe.getItems().clear();
        bolestOsobe.getItems().addAll(listaSvihBolesti);
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }

    /**
     * Resetira error indikatore
     */
    @Override
    public void resetIndicators() {
        makniErrorIndicator(imeOsobe);
        makniErrorIndicator(prezimeOsobe);
        makniErrorIndicator(datumRodjenjaOsobe.getEditor());
        makniErrorIndicator(zupanijaOsobe);
        makniErrorIndicator(bolestOsobe);
        makniErrorIndicator(kontaktiOsobeMenuBtn);
    }


    /**
     * Inicijalizira listenere
     */
    @Override
    public void inicijalizirajListenere() {
        imeOsobe.textProperty().addListener((obs, oldText, newText) -> validateField(imeOsobe, newText));
        prezimeOsobe.textProperty().addListener((obs, oldText, newText) -> validateField(prezimeOsobe, newText));
        datumRodjenjaOsobe.getEditor().textProperty().addListener((obs, oldText, newText) -> validateDatePicker(datumRodjenjaOsobe, newText));
        zupanijaOsobe.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldChoice, newChoice) -> validateChoiceBox(zupanijaOsobe, newChoice));
        bolestOsobe.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldChoice, newChoice) -> validateChoiceBox(bolestOsobe, newChoice));
    }
}
