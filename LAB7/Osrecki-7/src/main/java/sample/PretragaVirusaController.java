package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Virus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaVirusaController implements Initializable {

    private static final String FILE_NAME_VIRUSA = "./dat/virusi.txt";

    private static ObservableList<Virus> observableListVirusa;
    private static List<Virus> listaVirusa;
    private static List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Virus> tablicaVirusa;

    @FXML
    private TableColumn<Virus, String> stupacNazivVirusa;

    @FXML
    private TableColumn<Virus, List<Simptom>> stupacSimptomiVirusa;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacSimptomiVirusa.setCellValueFactory(new PropertyValueFactory<>("simptomi"));

        listaSimptoma = PretragaSimptomaController.ucitajSimptome();
        listaVirusa = ucitajViruse(listaSimptoma);

        observableListVirusa = FXCollections.observableArrayList(listaVirusa);

        tablicaVirusa.setItems(observableListVirusa);
    }

    public List<Virus> ucitajViruse(List<Simptom> simptomi) {
        File virusiFile = new File(FILE_NAME_VIRUSA);
        List<Virus> virusi = new ArrayList<>();

        if (virusiFile.exists()) {
            try (FileReader fileReader = new FileReader(FILE_NAME_VIRUSA);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    Long id = Long.parseLong(line);
                    String naziv = reader.readLine();

                    List<Simptom> simptomiVirusa = new ArrayList<>();
                    for (String sId : reader.readLine().split(",")) {
                        Long simptomId = Long.parseLong(sId);
                        simptomi.stream()
                                .filter(s -> s.getId().equals(simptomId))
                                .findAny()
                                .ifPresent(simptomiVirusa::add);
                    }

                    Virus virus = new Virus(id, naziv, simptomiVirusa);
                    virusi.add(virus);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File " + FILE_NAME_VIRUSA + " not found.");
            }
        }

        return virusi;
    }

    public void pretrazi() {
        String naziv = nazivVirusa.getText();

        List<Virus> filtriraniVirusi = listaVirusa.stream()
                .filter(virus -> virus.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuVirusa(filtriraniVirusi);
    }

    public void popuniObservableListuVirusa(List<Virus> virusi) {
        observableListVirusa.clear();
        observableListVirusa.addAll(FXCollections.observableArrayList(virusi));
    }

    public static ObservableList<Virus> getObservableListVirusa() {
        return observableListVirusa;
    }

    public static void setObservableListVirusa(ObservableList<Virus> observableList) {
        observableListVirusa = observableList;
    }

    public static List<Virus> getListaVirusa() {
        return listaVirusa;
    }

    public static void setListaVirusa(List<Virus> virusi) {
        listaVirusa = virusi;
    }
}
