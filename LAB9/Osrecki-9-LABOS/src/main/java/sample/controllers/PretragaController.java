package main.java.sample.controllers;

import main.java.sample.Main;

public abstract class PretragaController {

    public abstract void pretrazi();

    /**
     * Postavlja početnu scenu
     */
    public void natragNaPocetni() {
        Main.prikaziPocetniEkran();
    }
}
