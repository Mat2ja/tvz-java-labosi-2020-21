package main.java.hr.java.covidportal.model;

/**
 * Predstavlja entitet županije definirane nazivom i brojem stanovnika
 *
 * @author Matija
 */
public class Zupanija extends ImenovaniEntitet {
    private Integer brojStanovnika;

    /**
     * Inicijalizira podatak o nazivu i broju stanovnika županije
     * @param naziv podatak o nazivu županije
     * @param brojStanovnika podatak o broju stanovnika županije
     */
    public Zupanija(String naziv, Integer brojStanovnika) {
        super(naziv);
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }
}
