package main.java.sample.controllers;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.sample.Main;

import java.util.List;

public abstract class UnosController {

    /**
     * Prikazuje error alert
     *
     * @param title   naslov alerta
     * @param content content alerta
     */
    public void prikaziErrorUnosAlert(String title, String content) {
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
    public void prikaziSuccessUnosAlert(String title, String header, String content) {
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

    public boolean validateTextField(TextField tf, String value) {
        if (value.isBlank()) {
            Main.prikaziErrorIndicator(tf);
            return false;
        } else {
            Main.makniErrorIndicator(tf);
            return true;
        }
    }

    public <T> boolean validateTextFieldNumber(TextField tf, T value) {
        if (value == null) {
            Main.prikaziErrorIndicator(tf);
            return false;
        } else {
            Integer broj = ucitajBroj(value.toString());
            if (broj == null) {
                Main.prikaziErrorIndicator(tf);
                return false;
            }
            Main.makniErrorIndicator(tf);
            return true;
        }
    }

    public <T> boolean validateChoiceBox(ChoiceBox<T> cb, T value) {
        if (value == null) {
            Main.prikaziErrorIndicator(cb);
            return false;
        } else {
            Main.makniErrorIndicator(cb);
            return true;
        }
    }

    public <T> boolean validateMenuButton(MenuButton mb, List<T> lista) {
        if (lista.isEmpty()) {
            Main.prikaziErrorIndicator(mb);
            return false;
        } else {
            Main.makniErrorIndicator(mb);
            return true;
        }
    }

    public <T extends Number> boolean validateSlider(Slider slider, T value) {
        if (value.intValue() == 0) {
            Main.prikaziErrorIndicator(slider);
            return false;
        } else {
            Main.makniErrorIndicator(slider);
            return true;
        }
    }

    public static Integer ucitajBroj(String value) {
        Integer num;
        try {
            num = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Main.logger.error("Unesen je podatak koji nije broj");
            return null;
        }
        return num;
    }

    /**
     * Pretvara riječi u Title Case
     *
     * @param givenString string koji želimo obraditi
     * @return string u Title Case obliku
     */
    public static String toTitleCase(String givenString) {
        if (givenString.isBlank()) {
            return givenString;
        }
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }
}
