package main.java.hr.java.covidportal.model;

import java.util.List;

/**
 * Predstavlja entitet virusa definiranog nazivom i simtptomima
 *
 * @author Matija
 */
public class Virus extends Bolest implements Zarazno {


    /**
     * Inicijalizira podatak o nazivu i simptomima bolesti
     *
     * @param naziv    podatak o nazivu virusa
     * @param simptomi podatak o simptomima virusa
     */

    public Virus(String naziv, List<Simptom> simptomi) {
        super(naziv, simptomi);
        super.setJeVirus(true);
    }

    /**
     * Zaražuje novu osobu virusom trenutno zaražene osobe
     *
     * @param osoba objekt osobe
     */
    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }

}
