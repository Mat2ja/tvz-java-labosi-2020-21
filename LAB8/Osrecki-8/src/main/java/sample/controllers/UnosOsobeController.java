package main.java.sample.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.hr.java.covidportal.model.*;
import main.java.sample.Main;

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
    private Label status;


    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaZupanija = UcitavanjePodataka.ucitajZupanije();
        listaSvihBolesti = UcitavanjePodataka.ucitajBolesti();
        List<Virus> listaVirusa = UcitavanjePodataka.ucitajViruse();
        listaSvihBolesti.addAll(listaVirusa);
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


        prikaziStatus();
    }

    /**
     *
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

        if (ime.isBlank() || prezime.isBlank() || starost == 0 || zupanija == null || bolest == null || kontakti.isEmpty()) {
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
        listaCheckBoxa.forEach(cb -> cb.setSelected(false));
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
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

}
