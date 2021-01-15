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

    /**
     * Kreira reusable ContextMenu
     *
     * @return ContextMenu
     */
    public ContextMenu kreirajContextMenu() {
        ContextMenu cm = new ContextMenu();

        MenuItem mi = new MenuItem("Izbriši");
        cm.getItems().add(mi);

        return cm;
    }

    /**
     * Prikazuje element kontkest menija
     *
     * @param e           MouseEvent
     * @param tablica     tablica na kojoj se prikazuje menu
     * @param contextMenu ContextMenu
     * @param <T>         tip podatkta u tablici
     */
    public <T> void prikaziContextMenu(MouseEvent e, TableView<T> tablica, ContextMenu contextMenu) {
        contextMenu.show(tablica, e.getScreenX(), e.getScreenY());
    }
}
