package main.java.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Predstavlja glavnu klasu u kojoj se izvodi program
 */
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

    /**
     * Prikazuje error alert
     * @param title naslov alerta
     * @param content content alerta
     */
    public static void prikaziErrorUnosAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Pogrešan unos!");
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:images/coronavirus.png"));
        alert.showAndWait();

    }

    /**
     * Prikazuje sucessful alert
     * @param title naslov alerta
     * @param header header alerta
     * @param content content alerta
     */
    public static void prikaziSuccessUnosAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:images/coronavirus.png"));
        alert.showAndWait();
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
