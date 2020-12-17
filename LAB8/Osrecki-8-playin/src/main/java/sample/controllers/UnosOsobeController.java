package main.java.sample.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.*;
import main.java.sample.Main;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Kontroler unosa osoba
 */
public class UnosOsobeController implements Initializable {

    private static List<Zupanija> listaZupanija;
    private static List<Bolest> listaSvihBolesti;
    private static List<Osoba> listaOsoba;
    private static Long brojOsoba;

    private static List<CheckBox> listaCheckBoxa = new ArrayList<>();

    @FXML
    public TextField imeOsobe;
    @FXML
    public TextField prezimeOsobe;
    @FXML
    public Slider starostOsobe;
    @FXML
    public ChoiceBox<Zupanija> zupanijaOsobe;
    @FXML
    public ChoiceBox<Bolest> bolestOsobe;
    @FXML
    public MenuButton kontaktiOsobeMenuBtn;

    @FXML
    private Label starostVrijednost;
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
        listaZupanija = UcitavanjePodataka.ucitajZupanije();
        listaSvihBolesti = UcitavanjePodataka.ucitajBolesti();
        listaSvihBolesti.addAll(UcitavanjePodataka.ucitajViruse());
        listaOsoba = UcitavanjePodataka.ucitajOsobe();
        brojOsoba = (long) listaOsoba.size();

        zupanijaOsobe.getItems().addAll(listaZupanija);
        bolestOsobe.getItems().addAll(listaSvihBolesti);

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

        starostVrijednost.setText(String.valueOf(0));
        starostOsobe.valueProperty().addListener((observable, oldValue, newValue) -> {
            starostVrijednost.setText(String.format("%d", (int) starostOsobe.getValue()));
            
        });
        prikaziStatus();

        /* Action listeneri */
        imeOsobe.textProperty().addListener((obs, oldText, newText) -> validateTextField(imeOsobe, newText));
        prezimeOsobe.textProperty().addListener((obs, oldText, newText) -> validateTextField(prezimeOsobe, newText));
        zupanijaOsobe.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldChoice, newChoice) -> validateChoiceBox(zupanijaOsobe, newChoice));
        bolestOsobe.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldChoice, newChoice) -> validateChoiceBox(bolestOsobe, newChoice));
    }

    /**
     * Dodaje novu osobu
     */
    public void dodaj() {
        String ime = toTitleCase(imeOsobe.getText());
        String prezime = toTitleCase(prezimeOsobe.getText());
        Integer starost = (int) starostOsobe.getValue();
        Zupanija zupanija = zupanijaOsobe.getValue();
        Bolest bolest = bolestOsobe.getValue();
        List<Osoba> kontakti = listaCheckBoxa.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> UcitavanjePodataka.dohvatiOsobuPrekoId(listaOsoba, Long.parseLong(cb.getId())))
                .collect(Collectors.toList());


        Boolean valIme = validateTextField(imeOsobe, ime);
        Boolean valPrezime = validateTextField(prezimeOsobe, prezime);
        Boolean valStarost = validateSlider(starostOsobe, (int) starost);
        Boolean valZupanija = validateChoiceBox(zupanijaOsobe, zupanija);
        Boolean valBolest = validateChoiceBox(bolestOsobe, bolest);
        Boolean valKontakti = validateMenuButton(kontaktiOsobeMenuBtn, kontakti);

        /* If any doesn't pass validation, show error alert and return */
        if (!(valIme && valPrezime && valStarost && valZupanija && valBolest && valKontakti)) {
            Main.prikaziErrorUnosAlert("Unos osobe", "Unijeli ste osobu s nedozvoljenim vrijednostima.");
            return;
        }

        Long id = ++brojOsoba;
        Osoba novaOsoba = new Osoba.Builder(id)
                .hasIme(ime)
                .hasPrezime(prezime)
                .isAged(starost)
                .atZupanija(zupanija)
                .withBolest(bolest)
                .withKontaktiraneOsobe(kontakti)
                .build();
        UcitavanjePodataka.zapisiOsobu(novaOsoba);
        listaOsoba.add(novaOsoba);

        Main.prikaziSuccessUnosAlert(
                "Unos osobe", "Osoba dodana!", "Unijeli ste osobu: " + novaOsoba);

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
        status.setText("U sustavu je trenutno " + brojOsoba + " osoba");
    }

    /**
     * Resetira unose za upisivanje podataka
     */
    public void ocistiUnos() {
        imeOsobe.clear();
        prezimeOsobe.clear();
        imeOsobe.clear();
        zupanijaOsobe.getItems().clear();
        zupanijaOsobe.getItems().addAll(listaZupanija);
        bolestOsobe.getItems().clear();
        bolestOsobe.getItems().addAll(listaSvihBolesti);
        starostOsobe.setValue(0);
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
        resetIndicators();
    }

    public void resetIndicators() {
        Main.makniErrorIndicator(imeOsobe);
        Main.makniErrorIndicator(prezimeOsobe);
        Main.makniErrorIndicator(zupanijaOsobe);
        Main.makniErrorIndicator(bolestOsobe);
        Main.makniErrorIndicator(kontaktiOsobeMenuBtn);
    }

    /**
     * Pretvara riječi u Title Case
     *
     * @param givenString string koji želimo obraditi
     * @return string u Title Case obliku
     */
    public static String toTitleCase(String givenString) {
        if (givenString.isBlank()) {
            return givenString;
        }
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    public static boolean validateTextField(TextField tf, String value) {
        if (value.isBlank()) {
            Main.prikaziErrorIndicator(tf);
            return false;
        } else {
            Main.makniErrorIndicator(tf);
            return true;
        }
    }

    public static <T> boolean validateChoiceBox(ChoiceBox<T> cb, T value) {
        System.out.println("Value " + value);

        if (value == null) {
            Main.prikaziErrorIndicator(cb);
            return false;
        } else {
            Main.makniErrorIndicator(cb);
            return true;
        }
    }

    public static <T> boolean validateMenuButton(MenuButton mb, List<T> lista) {
        if (lista.isEmpty()) {
            Main.prikaziErrorIndicator(mb);
            return false;
        } else {
            Main.makniErrorIndicator(mb);
            return true;
        }
    }

    public static boolean validateSlider(Slider slider, Integer value) {
        if (value == 0) {
            Main.prikaziErrorIndicator(slider);
            return false;
        } else {
            Main.makniErrorIndicator(slider);
            return true;
        }
    }

}
