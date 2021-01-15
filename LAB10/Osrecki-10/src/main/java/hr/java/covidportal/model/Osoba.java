package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Predstavlja entitet osobe definirane imenom, prezimenom, starosti, županijom,
 * zaraženom bolesti i listom kontaktiranih osoba
 *
 * @author Matija
 */
public class Osoba implements Serializable {
    private Long id;
    private String ime;
    private String prezime;
    private LocalDate datumRodjenja;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe = new ArrayList<>();

    /**
     * Predstavlja Builder Pattern kojim se inicijalizira nova osoba
     */
    public static class Builder {
        private Long id;
        private String ime;
        private String prezime;
        private LocalDate datumRodjenja;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        /**
         * Inicijalizira Builder
         *
         */
        public Builder() {
        }

        /**
         * Postavlja Builderu podatak o prezimenu osobe
         *
         * @param ime podatak o imenu osobe
         * @return objekt Buildera
         */
        public Builder hasIme(String ime) {
            this.ime = ime;
            return this;
        }

        /**
         * Postavlja Builderu podatak o prezimenu osobe
         *
         * @param prezime podatak o prezimenu osobe
         * @return objekt Buildera
         */
        public Builder hasPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        /**
         * Postavlja Builderu podatak o starosti osobe
         *
         * @param datumRodjenja podatak o datumu rođenja osobe
         * @return objekt Buildera
         */
        public Builder isBornAt(LocalDate datumRodjenja) {
            this.datumRodjenja = datumRodjenja;
            return this;
        }

        /**
         * Postavlja Builderu podatak o županiji osobe
         *
         * @param zupanija podatak o županiji osobe
         * @return objekt Buildera
         */
        public Builder atZupanija(Zupanija zupanija) {
            this.zupanija = zupanija;
            return this;
        }

        /**
         * Postavlja Builderu podatak o zaraženoj bolesti osobe
         *
         * @param zarazenBolescu podatak o zaraženoj bolesti osobe
         * @return objekt Buildera
         */
        public Builder withBolest(Bolest zarazenBolescu) {
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        /**
         * Postavlja Builderu podatak o kontaktiranim osoboma
         *
         * @param kontaktiraneOsobe podatak o listi kontaktiranih osoba
         * @return objekt Buildera
         */
        public Builder withKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        /**
         * Inicijalzira instancu osobe i postavlja podatke o imenu, prezimenu, starosti, županiji,
         * zaraženoj bolesti i kontaktiranim osobama osobe
         *
         * @return instanca osobe
         */
        public Osoba build() {
            Osoba osoba = new Osoba();
            osoba.id = id;
            osoba.ime = ime;
            osoba.prezime = prezime;
            osoba.datumRodjenja = datumRodjenja;
            osoba.zupanija = zupanija;
            osoba.zarazenBolescu = zarazenBolescu;
            osoba.kontaktiraneOsobe = kontaktiraneOsobe;

            if (osoba.zarazenBolescu instanceof Virus virus) {
                kontaktiraneOsobe.forEach(virus::prelazakZarazeNaOsobu);
            }

            return osoba;
        }

    }

    /**
     * Inicijalizira praznu instancu osobe
     */
    private Osoba() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    public Integer getStarost() {
        LocalDate now = LocalDate.now();
        return Period.between(datumRodjenja, now).getYears();
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(id, osoba.id) &&
                Objects.equals(ime, osoba.ime) &&
                Objects.equals(prezime, osoba.prezime) &&
                Objects.equals(datumRodjenja, osoba.datumRodjenja) &&
                Objects.equals(zupanija, osoba.zupanija) &&
                Objects.equals(zarazenBolescu, osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ime, prezime, datumRodjenja, zupanija, zarazenBolescu, kontaktiraneOsobe);
    }
}
