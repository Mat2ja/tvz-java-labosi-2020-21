package main.java.sample.controllers;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler pretrage županija
 */
public class PretragaZupanijaController extends PretragaController implements Initializable {

    private static ObservableList<Zupanija> observableListZupanija;
    private static List<Zupanija> listaZupanija;

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TableView<Zupanija> tablicaZupanija;
    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnikaZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojZarazenihZupanije;

    private ContextMenu contextMenu;

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

        listaZupanija = BazaPodataka.dohvatiSveZupanije();

        if (observableListZupanija == null) {
            observableListZupanija = FXCollections.observableArrayList();
        }

        popuniObservableListuZupanija(listaZupanija);

        tablicaZupanija.setItems(observableListZupanija);
        tablicaZupanija.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        contextMenu = kreirajContextMenu();

        tablicaZupanija.setRowFactory(t -> {
            TableRow<Zupanija> row = new TableRow();
            row.setOnMouseClicked(e -> {
                if (row.isEmpty()) return;

                Zupanija zupanija = row.getItem();

                if (e.getClickCount() == 2) {
                    prikaziEkranIzmjene(zupanija);
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    prikaziContextMenu(e, tablicaZupanija, contextMenu);
                    postaviContextMenuListenere(contextMenu, zupanija);
                } else if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            });
            return row;
        });
    }

    private void postaviContextMenuListenere(ContextMenu contextMenu, Zupanija zupanija) {
        List<MenuItem> items = contextMenu.getItems();
        items.get(0).setOnAction(event -> prikaziEkranIzmjene(zupanija));
        items.get(1).setOnAction(event -> makniZupaniju(zupanija));
    }

    private void makniZupaniju(Zupanija zupanija) {
        BazaPodataka.izbrisiZupaniju(zupanija.getId());
        listaZupanija = BazaPodataka.dohvatiSveZupanije();
        popuniObservableListuZupanija(listaZupanija);
    }

    /**
     * Prikazuje ekran za izmjenu izabrane županije
     *
     * @param zupanija podatak o županiji za izmjenu
     */
    private void prikaziEkranIzmjene(Zupanija zupanija) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("unosZupanije.fxml"));
            Scene scene = new Scene(loader.load());

            Main.getMainStage().setTitle("Unos županije");
            Main.getMainStage().setScene(scene);

            UnosZupanijeController controller = loader.getController();
            controller.izmijeniZupaniju(zupanija);
        } catch (IOException e) {
            Main.logger.error("Greška kod prikaza ekrana");
            e.printStackTrace();
        }
    }


    /**
     * Pretražuje županije prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    @Override
    public void pretrazi() {
        String naziv = nazivZupanije.getText();

        Predicate<Zupanija> predNaziv = zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv.toLowerCase());

        List<Zupanija> filitriraneZupanije = listaZupanija.stream()
                .filter(predNaziv)
                .collect(Collectors.toList());

        popuniObservableListuZupanija(filitriraneZupanije);
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

}
