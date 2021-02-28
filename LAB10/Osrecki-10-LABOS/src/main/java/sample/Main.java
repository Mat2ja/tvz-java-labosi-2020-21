package main.java.sample;

import ch.qos.logback.core.util.Loader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.util.Duration;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.BrojSimptomaNit;
import main.java.sample.controllers.PocetniEkranController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Predstavlja glavnu klasu u kojoj se izvodi program
 */
public class Main extends Application {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;
    private static Scene homeScene;

    final public static PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    /**
     * Započinje program i stvara prozor
     *
     * @param primaryStage podatak o primarnoj sceni
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Pokrenuli smo program");

        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pocetniEkran.fxml"));

        homeScene = new Scene(loader.load(), 800, 500);
        homeScene.getStylesheets().add(Main.class.getClassLoader().getResource("application.css").toExternalForm());

        prikaziPocetniEkran();

        primaryStage.getIcons().add(new Image(String.valueOf(Main.class.getClassLoader().getResource("assets/virus.png"))));

        primaryStage.show();

        PocetniEkranController controller = loader.getController();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new BrojSimptomaNit());

        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Main.logger.error(e.getMessage());
        }

        controller.brSimptomaLabel.setText("Broj simptoma" + controller.getBrSimptoma().toString());


        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String format = dtf.format(now);
            System.out.println(format);
            controller.brSimptomaLabel.setText(controller.getBrSimptoma().toString() + " " + format);
        }), new KeyFrame(Duration.seconds(2)));

        clock.setCycleCount(Animation.INDEFINITE);

        clock.play();

    }

    /**
     * Postavlja scenu i naslov na zadani Stage
     */
    public static void prikaziPocetniEkran() {
        mainStage.setTitle("Covid Portal");
        mainStage.setScene(getHomeScene());


    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Scene getHomeScene() {
        return homeScene;
    }
}
