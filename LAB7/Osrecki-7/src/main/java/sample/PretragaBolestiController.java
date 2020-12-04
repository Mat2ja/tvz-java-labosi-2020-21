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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaBolestiController implements Initializable {

    private static final String FILE_NAME_BOLESTI = "./dat/bolesti.txt";

    private static ObservableList<Bolest> observableListBolesti;
    private static List<Bolest> listaBolesti;
    private static List<Simptom> listaSimptoma;


    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;

    @FXML
    private TableColumn<Bolest, List<Simptom>> stupacSimptomiBolesti;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacSimptomiBolesti.setCellValueFactory(new PropertyValueFactory<>("simptomi"));

        listaSimptoma = PretragaSimptomaController.ucitajSimptome();
        listaBolesti = ucitajBolesti(listaSimptoma);

        observableListBolesti = FXCollections.observableArrayList(listaBolesti);

        tablicaBolesti.setItems(observableListBolesti);
    }

    public static List<Bolest> ucitajBolesti(List<Simptom> simptomi) {

        File bolestiFile = new File(FILE_NAME_BOLESTI);

        List<Bolest> bolesti = new ArrayList<>();

        if (bolestiFile.exists()) {
            try (FileReader fileReader = new FileReader(FILE_NAME_BOLESTI);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    Long id = Long.parseLong(line);
                    String naziv = reader.readLine();

                    List<Long> simptomiIds = Arrays.stream(reader.readLine()
                            .split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    List<Simptom> simptomiBolesti = simptomi.stream()
                            .filter(s -> simptomiIds.contains(s.getId()))
                            .collect(Collectors.toList());

                    Bolest bolest = new Bolest(id, naziv, simptomiBolesti);
                    bolesti.add(bolest);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File " + FILE_NAME_BOLESTI + " not found.");
            }
        }

        return bolesti;
    }

    public void pretrazi() {
        String naziv = nazivBolesti.getText();


        List<Bolest> filitriraneBolesti = listaBolesti.stream()
                .filter(bolest -> bolest.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuBolesti(filitriraneBolesti);
    }

    public void popuniObservableListuBolesti(List<Bolest> bolesti) {
        observableListBolesti.clear();
        observableListBolesti.addAll(FXCollections.observableArrayList(bolesti));
    }

    public static ObservableList<Bolest> getObservableListBolesti() {
        return observableListBolesti;
    }

    public static void setObservableListBolesti(ObservableList<Bolest> observableList) {
        observableListBolesti = observableList;
    }

    public static List<Bolest> getListaBolesti() {
        return listaBolesti;
    }

    public static void setListaBolesti(List<Bolest> bolesti) {
        listaBolesti = bolesti;
    }
}
