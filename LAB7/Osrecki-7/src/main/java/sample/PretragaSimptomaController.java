package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {

    private static final String FILE_NAME_SIMPTOMI = "./dat/simptomi.txt";

    private static ObservableList<Simptom> observableListSimptoma;
    private static List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;

    @FXML
    private TableColumn<Simptom, VrijednostSimptoma> stupacVrijednostSimptoma;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        stupacVrijednostSimptoma.setCellValueFactory(new PropertyValueFactory<>("vrijednost"));


        listaSimptoma = ucitajSimptome();

        observableListSimptoma = FXCollections.observableArrayList(listaSimptoma);

        tablicaSimptoma.setItems(observableListSimptoma);
    }

    public static List<Simptom> ucitajSimptome() {

        File simptomiFile = new File(FILE_NAME_SIMPTOMI);
        List<Simptom> simptomi = new ArrayList<>();

        if (simptomiFile.exists()) {

            try (FileReader fileReader = new FileReader(FILE_NAME_SIMPTOMI);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    Long id = Long.parseLong(line);
                    String naziv = reader.readLine();
                    VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(reader.readLine());

                    Simptom simptom = new Simptom(id, naziv, vrijednost);
                    simptomi.add(simptom);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File " + FILE_NAME_SIMPTOMI + " not found.");
            }
        }

        return simptomi;
    }

    public void pretrazi() {
        String naziv = nazivSimptoma.getText();

        List<Simptom> filitriraniSimptomi = listaSimptoma.stream()
                .filter(simptom -> simptom.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .collect(Collectors.toList());

        popuniObservableListuSimptoma(filitriraniSimptomi);
    }


    public void popuniObservableListuSimptoma(List<Simptom> simptomi) {
        observableListSimptoma.clear();
        observableListSimptoma.addAll(FXCollections.observableArrayList(simptomi));
    }

    public static ObservableList<Simptom> getObservableListSimptoma() {
        return observableListSimptoma;
    }

    public static void setObservableListSimptoma(ObservableList<Simptom> observableList) {
        observableListSimptoma = observableList;
    }

    public static List<Simptom> getListaSimptoma() {
        return listaSimptoma;
    }

    public static void setListaSimptoma(List<Simptom> listaSimptoma) {
        PretragaSimptomaController.listaSimptoma = listaSimptoma;
    }
}
