package main.java.hr.java.covidportal.model;

import java.util.Objects;

/**
 * Predstavlja entitet županije definirane nazivom i brojem stanovnika
 *
 * @author Matija
 */
public class Zupanija extends ImenovaniEntitet {
    private Integer brojStanovnika;
    private Integer brojZarazenih;
    private Float postotakZarazenih;


    /**
     * Inicijalizira podatak o nazivu i broju stanovnika županije
     *
     * @param id podatak o id županije
     * @param naziv          podatak o nazivu županije
     * @param brojStanovnika podatak o broju stanovnika županije
     * @param brojZarazenih  podatak o broju zaraženih osoba
     */
    public Zupanija(Long id, String naziv, Integer brojStanovnika, Integer brojZarazenih) {
        super(id, naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
        this.postotakZarazenih = ((float) this.getBrojZarazenih() / this.getBrojStanovnika()) * 100;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojZarazenih() {
        return brojZarazenih;
    }

    public void setBrojZarazenih(Integer brojZarazenih) {
        this.brojZarazenih = brojZarazenih;
    }

    public Float getPostotakZarazenih() {
        return postotakZarazenih;
    }


    @Override
    public String toString() {
        return this.getNaziv();
    }
}
