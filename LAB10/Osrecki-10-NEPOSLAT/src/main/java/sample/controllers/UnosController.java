package main.java.sample.controllers;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.sample.Main;

import java.util.List;

public abstract class UnosController {

    public abstract void dodaj();

    public abstract void prikaziStatus();

    public abstract void ocistiUnos();

    public abstract void resetIndicators();

    public abstract void inicijalizirajListenere();

    /**
     * Prikazuje alert
     *
     * @param title   naslov alerta
     * @param header  header alerta
     * @param content content alerta
     * @param type    type alerta
     */
    public static void prikaziAlert(String title, String header, String content, Alert.AlertType type) {
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

    /**
     * Validira generički field
     *
     * @param tf    field za validaciju
     * @param value podatak o unesenoj vrijednosti
     * @param <T>   input field
     * @return
     */
    public <T> boolean validateField(T tf, String value) {
        if (value.isBlank()) {
            prikaziErrorIndicator(tf);
            return false;
        } else {
            makniErrorIndicator(tf);
            return true;
        }
    }

    /**
     * Validira DatePicker
     *
     * @param dp    DatePicker za validaciju
     * @param value podatak o unesenoj vrijednosti
     * @return boolean
     */
    public boolean validateDatePicker(DatePicker dp, String value) {
        if (value.isBlank()) {
            prikaziErrorIndicator(dp.getEditor());
            return false;
        } else {
            makniErrorIndicator(dp.getEditor());
            return true;
        }
    }

    /**
     * Validira TextField
     *
     * @param tf    TextField za validaciju
     * @param value podatak o unesenoj vrijednosti
     * @return boolean
     */
    public boolean validateTextFieldNumber(TextField tf, String value) {
        if (!isValidInteger(value)) {
            prikaziErrorIndicator(tf);
            return false;
        } else {
            makniErrorIndicator(tf);
            return true;
        }
    }

    /**
     * Validira ChoiceBox
     *
     * @param cb    ChoiceBox za validaciju
     * @param value podatak o izabranoj vrijednosti
     * @param <T>   tip objekta ponuđenih izbora ChoiceBox-a
     * @return boolean
     */
    public <T> boolean validateChoiceBox(ChoiceBox<T> cb, T value) {
        if (value == null) {
            prikaziErrorIndicator(cb);
            return false;
        } else {
            makniErrorIndicator(cb);
            return true;
        }
    }

    /**
     * Validira MenuButton
     *
     * @param mb    MenuButton za validaciju
     * @param lista podatak o listi izabranih vrijednosti
     * @return boolean
     */
    public <T> boolean validateMenuButton(MenuButton mb, List<T> lista) {
        if (lista.isEmpty()) {
            prikaziErrorIndicator(mb);
            return false;
        } else {
            makniErrorIndicator(mb);
            return true;
        }
    }


    /**
     * Validira Slider
     *
     * @param slider Slider za validaciju
     * @return boolean
     */
    public boolean validateSlider(Slider slider) {
        if (slider.valueProperty().lessThanOrEqualTo(0).get()) {
            prikaziErrorIndicator(slider);
            return false;
        } else {
            makniErrorIndicator(slider);
            return true;
        }
    }

    public Boolean isValidInteger(String value) {
        return value.matches("\\d+");
    }

    /**
     * Pretvara riječi u Title Case
     *
     * @param givenString string koji želimo obraditi
     * @return string u Title Case obliku
     */
    public String toTitleCase(String givenString, String sep) {
        if (givenString.isBlank()) {
            return givenString;
        }
        String[] arr = givenString.split(sep);
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1).toLowerCase()).append(sep);
        }
        return sb.substring(0, sb.length() - 1).trim();
    }

    /**
     * Pretvara prvu riječ u Title Case
     *
     * @param givenString string koji želimo obraditi
     */
    public String toTitleCaseFirstWord(String givenString) {
        if (givenString.isBlank()) {
            return givenString;
        }

        return givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase();
    }

    /**
     * Prikazuje error indication na zadanom inputu
     *
     * @param input input za validaciju
     * @param <T>
     */
    public <T> void prikaziErrorIndicator(T input) {
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

    /**
     * Miče error indication na zadanom inputu
     *
     * @param input input za validaciju
     * @param <T>
     */
    public <T> void makniErrorIndicator(T input) {
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

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

}
