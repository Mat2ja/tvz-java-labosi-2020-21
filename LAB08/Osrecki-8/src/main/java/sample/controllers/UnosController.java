package main.java.sample.controllers;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.sample.Main;

import java.util.List;

public abstract class UnosController {

    /**
     * Prikazuje alert
     *
     * @param title naslov alerta
     * @param header header alerta
     * @param content content alerta
     * @param type type alerta
     */
    private static void prikaziAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(Main.class.getClassLoader().getResource("assets/virus.png"))));
        alert.getDialogPane()
                .getStylesheets().add(Main.class.getClassLoader().getResource("application.css").toExternalForm());
        alert.showAndWait();
    }

    /**
     * Prikazuje error alert
     *
     * @param title   naslov alerta
     * @param content content alerta
     */
    public void prikaziErrorUnosAlert(String title, String content) {
        prikaziAlert(title, "Pogrešan unos!", content, Alert.AlertType.ERROR);
    }

    /**
     * Prikazuje sucessful alert
     *
     * @param title   naslov alerta
     * @param header  header alerta
     * @param content content alerta
     */
    public void prikaziSuccessUnosAlert(String title, String header, String content) {
        prikaziAlert(title, header, content, Alert.AlertType.INFORMATION);
    }

    public boolean validateTextField(TextField tf, String value) {
        if (value.isBlank()) {
            prikaziErrorIndicator(tf);
            return false;
        } else {
            makniErrorIndicator(tf);
            return true;
        }
    }

    public <T> boolean validateTextFieldNumber(TextField tf, T value) {
        if (value == null) {
            prikaziErrorIndicator(tf);
            return false;
        } else {
            Integer broj = ucitajBroj(value.toString());
            if (broj == null) {
                prikaziErrorIndicator(tf);
                return false;
            }
            makniErrorIndicator(tf);
            return true;
        }
    }

    public <T> boolean validateChoiceBox(ChoiceBox<T> cb, T value) {
        if (value == null) {
            prikaziErrorIndicator(cb);
            return false;
        } else {
            makniErrorIndicator(cb);
            return true;
        }
    }

    public <T> boolean validateMenuButton(MenuButton mb, List<T> lista) {
        if (lista.isEmpty()) {
            prikaziErrorIndicator(mb);
            return false;
        } else {
            makniErrorIndicator(mb);
            return true;
        }
    }

//    public <T extends Number> boolean validateSlider(Slider slider, T value) {
//        if (value.intValue() == 0) {
//            prikaziErrorIndicator(slider);
//            return false;
//        } else {
//            makniErrorIndicator(slider);
//            return true;
//        }
//    }

    public  boolean validateSlider(Slider slider) {
        if (slider.valueProperty().lessThanOrEqualTo(0).get()) {
            prikaziErrorIndicator(slider);
            return false;
        } else {
            makniErrorIndicator(slider);
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

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
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
}
