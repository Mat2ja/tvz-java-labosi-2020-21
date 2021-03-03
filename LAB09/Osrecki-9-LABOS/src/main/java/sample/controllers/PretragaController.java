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

    // zadatak 1
    public ContextMenu kreirajContextMenu() {
        ContextMenu cm = new ContextMenu();

        MenuItem mi = new MenuItem("Izbriši");
        cm.getItems().add(mi);

        return cm;
    }

    public <T> void prikaziContextMenu(MouseEvent e, TableView<T> tablica, ContextMenu contextMenu) {
        contextMenu.show(tablica, e.getScreenX(), e.getScreenY());
    }
}
