package main.java.sample.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import main.java.hr.java.covidportal.model.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvatiSveZupanijeNit;
import main.java.hr.java.covidportal.niti.NajviseZarazenaNit;
import main.java.sample.Main;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontoler pretrage županija
 */
public class PretragaZupanijaController extends PretragaController implements Initializable {

    private static ObservableList<Zupanija> observableListZupanija;
    public static List<Zupanija> listaZupanija;

    static {
        listaZupanija = new ArrayList<>();
    }

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

    private static Boolean nitPokrenuta = false;

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

        if (listaZupanija == null) {
            listaZupanija = new ArrayList<>();
        }

        ExecutorService executor = Executors.newCachedThreadPool();

        executor.execute(new DohvatiSveZupanijeNit());

//        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        listaZupanija = BazaPodataka.getZupanije();

        if (observableListZupanija == null) {
            observableListZupanija = FXCollections.observableArrayList();
        }


        popuniObservableListuZupanija(listaZupanija);

        tablicaZupanija.setItems(observableListZupanija);
        tablicaZupanija.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        if (!nitPokrenuta) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(new NajviseZarazenaNit(listaZupanija));
                    nitPokrenuta = true;
                }
            }, 0, 10000);
        }


        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Zupanija zupanija = getNajzarazenijaZupanija();
            Main.getMainStage().setTitle(zupanija.getNaziv());
        }), new KeyFrame(Duration.seconds(10)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * Dohavaća županiju sa najvećim postotkom zaraženosti
     *
     * @return podatak o županiji
     */
    private Zupanija getNajzarazenijaZupanija() {
        return listaZupanija.stream()
                .min((z1, z2) -> z2.getPostotakZarazenih().compareTo(z1.getPostotakZarazenih()))
                .get();
    }


    /**
     * Pretražuje županije prema zadanoj riječi i popunjuje listu filitriranim rezulatima
     */
    @Override
    @FXML
    public void pretrazi() {
        String naziv = nazivZupanije.getText();

        Predicate<Zupanija> predNaziv = zupanija -> zupanija.getNaziv().toLowerCase().contains(naziv.toLowerCase());
        List<Zupanija> filtriraneZupanije = listaZupanija.stream()
                .filter(predNaziv)
                .collect(Collectors.toList());

        popuniObservableListuZupanija(filtriraneZupanije);
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
