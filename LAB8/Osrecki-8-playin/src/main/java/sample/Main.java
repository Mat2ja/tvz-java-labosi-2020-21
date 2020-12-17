package main.java.sample;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Predstavlja glavnu klasu u kojoj se izvodi program
 */
public class Main extends Application {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;
    private static Scene homeScene;

    final public static PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    final public static PseudoClass successClass = PseudoClass.getPseudoClass("success");

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
        homeScene.getStylesheets().add(Main.class.getClassLoader().getResource("application.css").toExternalForm());
        prikaziPocetniEkran();

        primaryStage.getIcons().add(new Image(String.valueOf(Main.class.getClassLoader().getResource("assets/virus.png"))));

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
     *
     * @param title   naslov alerta
     * @param content content alerta
     */
    public static void prikaziErrorUnosAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Pogrešan unos!");
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(Main.class.getClassLoader().getResource("assets/virus.png"))));
        alert.getDialogPane()
                .getStylesheets().add(Main.class.getClassLoader().getResource("application.css").toExternalForm());
        alert.showAndWait();

    }

    /**
     * Prikazuje sucessful alert
     *
     * @param title   naslov alerta
     * @param header  header alerta
     * @param content content alerta
     */
    public static void prikaziSuccessUnosAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(Main.class.getClassLoader().getResource("assets/virus.png"))));
        alert.getDialogPane()
                .getStylesheets().add(Main.class.getClassLoader().getResource("application.css").toExternalForm());
        alert.showAndWait();
    }

    public static <T> void prikaziErrorIndicator(T input) {
        if (input instanceof TextField tf) {
            tf.pseudoClassStateChanged(Main.errorClass, true);
        } else if (input instanceof MenuButton mb) {
            mb.pseudoClassStateChanged(Main.errorClass, true);
        } else if (input instanceof ChoiceBox cb) {
            cb.pseudoClassStateChanged(Main.errorClass, true);
        } else if (input instanceof Slider sl) {
            sl.pseudoClassStateChanged(Main.errorClass, true);
        }
    }

    public static <T> void makniErrorIndicator(T input) {
        if (input instanceof TextField tf) {
            tf.pseudoClassStateChanged(Main.errorClass, false);
        } else if (input instanceof MenuButton mb) {
            mb.pseudoClassStateChanged(Main.errorClass, false);
        } else if (input instanceof ChoiceBox cb) {
            cb.pseudoClassStateChanged(Main.errorClass, false);
        } else if (input instanceof Slider sl) {
            sl.pseudoClassStateChanged(Main.errorClass, false);
        }
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
