package main.java.sample;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

    private static final String FILE_NAME_ZUPANIJE = "./dat/zupanije.txt";

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacBrojStanovnikaZupanije.setCellValueFactory(new PropertyValueFactory<>("brojStanovnika"));
        stupacBrojZarazenihZupanije.setCellValueFactory(new PropertyValueFactory<>("brojZarazenih"));

        listaZupanija = ucitajZupanije();

        observableListZupanija = FXCollections.observableArrayList(listaZupanija);

        tablicaZupanija.setItems(observableListZupanija);
    }

    public static List<Zupanija> ucitajZupanije() {
        File zupanijeFile = new File(FILE_NAME_ZUPANIJE);
        List<Zupanija> zupanije = new ArrayList<>();

        if (zupanijeFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(zupanijeFile))) {

                String line;
                while ((line = br.readLine()) != null) {
                    Long id = Long.parseLong(line);
                    String naziv = br.readLine();
                    Integer brojStanovnika = Integer.parseInt(br.readLine());
                    Integer brojZarazenih = Integer.parseInt(br.readLine());

                    Zupanija zupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
                    zupanije.add(zupanija);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File " + FILE_NAME_ZUPANIJE + " not found.");
            }
        }

        return zupanije;
    }

    public void pretrazi() {
        String naziv = nazivZupanije.getText();

        List<Zupanija> filitriraneZupanije = listaZupanija.stream()
                .filter(zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuZupanija(filitriraneZupanije);
    }

    public void popuniObservableListuZupanija(List<Zupanija> zupanije) {
        observableListZupanija.clear();
        observableListZupanija.addAll(FXCollections.observableArrayList(zupanije));
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
