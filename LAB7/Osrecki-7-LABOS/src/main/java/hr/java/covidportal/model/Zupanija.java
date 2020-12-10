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
    private String sifra;

    /**
     * Inicijalizira podatak o nazivu i broju stanovnika županije
     *
     * @param id             podatak o id županije
     * @param naziv          podatak o nazivu županije
     * @param brojStanovnika podatak o broju stanovnika županije
     * @param brojZarazenih  podatak o broju zaraženih osoba
     * @param sifra          podatak o sifri zupanije
     */
    public Zupanija(Long id, String naziv, Integer brojStanovnika, Integer brojZarazenih, String sifra) {
        super(id, naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
        this.postotakZarazenih = postotakZarazenih;
        this.sifra = sifra;
    }


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

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    @Override
    public String toString() {
        return this.getNaziv();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Zupanija zupanija = (Zupanija) o;
        return Objects.equals(brojStanovnika, zupanija.brojStanovnika) &&
                Objects.equals(brojZarazenih, zupanija.brojZarazenih) &&
                Objects.equals(postotakZarazenih, zupanija.postotakZarazenih) &&
                Objects.equals(sifra, zupanija.sifra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brojStanovnika, brojZarazenih, postotakZarazenih, sifra);
    }
}
