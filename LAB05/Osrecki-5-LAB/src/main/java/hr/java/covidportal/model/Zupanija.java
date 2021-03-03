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
     * @param naziv          podatak o nazivu županije
     * @param brojStanovnika podatak o broju stanovnika županije
     * @param brojZarazenih  podatak o broju zaraženih osoba
     */
    public Zupanija(String naziv, Integer brojStanovnika, Integer brojZarazenih) {
        super(naziv);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Zupanija zupanija = (Zupanija) o;
        return Objects.equals(brojStanovnika, zupanija.brojStanovnika) &&
                Objects.equals(brojZarazenih, zupanija.brojZarazenih) &&
                Objects.equals(postotakZarazenih, zupanija.postotakZarazenih);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brojStanovnika, brojZarazenih, postotakZarazenih);
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
