package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.hr.java.covidportal.model.Osoba;

public class PrikazOsobeController {

    @FXML
    Label imeOsobe;
    @FXML
    Label prezimeOsobe;
    @FXML
    Label starostOsobe;
    @FXML
    Label zupanijaOsobe;
    @FXML
    Label bolestOsobe;
    @FXML
    Label kontaktiOsobe;


    public void prikaziOsobu(Osoba osoba) {
        System.out.println(osoba.getIme());
        System.out.println(osoba.getPrezime());
        System.out.println(osoba.getStarost());
        System.out.println(osoba.getZupanija());
        System.out.println(osoba.getZarazenBolescu());
        System.out.println(osoba.getKontaktiraneOsobe());
        imeOsobe.setText(osoba.getIme());
        prezimeOsobe.setText(osoba.getPrezime());
        starostOsobe.setText(osoba.getStarost().toString());
        zupanijaOsobe.setText(osoba.getZupanija().toString());
        bolestOsobe.setText(osoba.getZarazenBolescu().toString());
        kontaktiOsobe.setText(osoba.getKontaktiraneOsobe().toString().replaceAll("[\\[\\]]", ""));
    }
}
