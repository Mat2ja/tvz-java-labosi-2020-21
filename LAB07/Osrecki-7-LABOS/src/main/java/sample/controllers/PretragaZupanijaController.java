package main.java.sample.controllers;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.model.CitanjePodataka;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler za pretragu županije
 */
public class PretragaZupanijaController implements Initializable {

    private static ObservableList<Zupanija> observableListZupanija;
    private static List<Zupanija> listaZupanija;

    @FXML
    private TextField nazivZupanije;
    // zadatak 1
    @FXML
    private TextField sifraZupanije;

    @FXML
    private TableView<Zupanija> tablicaZupanija;
    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnikaZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojZarazenihZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacSifraZupanije;

    /**
     * Inicijalizira kontroler
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacBrojStanovnikaZupanije.setCellValueFactory(new PropertyValueFactory<>("brojStanovnika"));
        stupacBrojZarazenihZupanije.setCellValueFactory(new PropertyValueFactory<>("brojZarazenih"));
        // zadatak 1
        stupacSifraZupanije.setCellValueFactory(new PropertyValueFactory<>("sifra"));

        listaZupanija = CitanjePodataka.ucitajZupanije();

        if (observableListZupanija == null) {
            observableListZupanija = FXCollections.observableArrayList();
        }
        observableListZupanija.addAll(listaZupanija);

        tablicaZupanija.setItems(observableListZupanija);
        tablicaZupanija.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablicaZupanija.setRowFactory(t -> {
            TableRow<Zupanija> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !(row.isEmpty())) {
                    Zupanija zupanija = row.getItem();
                    prikaziZupaniju(zupanija);
                }
            });
            return row;
        });
    }

    // zadatak 2

    /**
     * Otvara novi prozor za prikaz kliknute zupanije
     *
     * @param zupanija podatak o zupaniji
     */
    public void prikaziZupaniju(Zupanija zupanija) {
        try {
            FXMLLoader pregledZupanije = new FXMLLoader(getClass().getClassLoader().getResource("prikazZupanije.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Pregled županije");
            stage.getIcons().add(new Image("file:images/coronavirus.png"));
            Scene scenaPrikazaZupanije = new Scene(pregledZupanije.load());
            stage.setScene(scenaPrikazaZupanije);
            stage.show();

            PrikazZupanijeController contoller = pregledZupanije.getController();
            contoller.prikaziZupaniju(zupanija);
        } catch (IOException e) {
            Main.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Pretražuje županije prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    public void pretrazi() {
        Main.getMainStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;
        });

        String naziv = nazivZupanije.getText();
        String sifra = sifraZupanije.getText();

        // zadatak 1
        Predicate<Zupanija> predNaziv = zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv.toLowerCase());
        Predicate<Zupanija> predSifra = zupanija -> zupanija.getSifra().toLowerCase().contains(sifra.toLowerCase());

        List<Zupanija> filitriraneZupanije = listaZupanija.stream()
                .filter(predNaziv)
                .filter(predSifra)
                .collect(Collectors.toList());

        popuniObservableListuZupanija(filitriraneZupanije);
    }

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }


    /**
     * Popunjuje observable listu listom svih učitanih županija
     *
     * @param zupanije podatak o listi županija
     */
    public void popuniObservableListuZupanija(List<Zupanija> zupanije) {
        observableListZupanija.clear();
        observableListZupanija.addAll(zupanije);
    }

    public static ObservableList<Zupanija> getObservableListZupanija() {
        return observableListZupanija;
    }

    public static void setObservableListZupanija(ObservableList<Zupanija> observableList) {
        observableListZupanija = observableList;
    }

    public static List<Zupanija> getListaZupanija() {
        return listaZupanija;
    }

    public static void setListaZupanija(List<Zupanija> zupanije) {
        listaZupanija = zupanije;
    }
}
