package main.java.sample.controllers;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import main.java.sample.Main;

public abstract class PretragaController {

    public abstract void pretrazi();

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }

    public ContextMenu kreirajContextMenu() {
        ContextMenu cm = new ContextMenu();

        MenuItem mi1 = new MenuItem("Uredi");
        MenuItem mi2 = new MenuItem("Izbriši");

        cm.getItems().add(mi1);
        cm.getItems().add(mi2);

        return cm;
    }

    public <T> void prikaziContextMenu(MouseEvent e, TableView<T> tablica, ContextMenu contextMenu) {
        contextMenu.show(tablica, e.getScreenX(), e.getScreenY());
    }
}
