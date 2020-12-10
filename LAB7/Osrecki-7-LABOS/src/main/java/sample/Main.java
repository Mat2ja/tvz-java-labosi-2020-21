package main.java.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;
    private static Scene homeScene;

    /**
     * Započinje program i stvara prozor
     *
     * @param primaryStage podatak o primarnoj sceni
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.error("Pokrenuli smo program");
        mainStage = primaryStage;

        Parent pocetniEkran = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        homeScene = new Scene(pocetniEkran, 800, 500);
        prikaziPocetniEkran();

        primaryStage.getIcons().add(new Image("file:images/coronavirus.png"));
        primaryStage.show();
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
