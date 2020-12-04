package main.java.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    private static final String FILE_NAME_SIMPTOMI = "./dat/simptomi.txt";
    private static final String FILE_NAME_BOLESTI = "./dat/bolesti.txt";
    private static final String FILE_NAME_VIRUSI = "./dat/virusi.txt";
    private static final String FILE_NAME_OSOBE = "./dat/osobe.txt";

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        Parent pocetniEkran = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Covid Tester 9000");
        primaryStage.setScene(new Scene(pocetniEkran, 600, 400));
        primaryStage.show();
    }


    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
